package android.train.mipt_school;


import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.train.mipt_school.Adapters.ScheduleAdapter;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.DailyScheduleItem;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.Tools.DataSavingFragment;
import android.train.mipt_school.Tools.DateTools;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;


public class ScheduleEditFragment extends Fragment implements SceneFragment, DataSavingFragment {


    private static final String BUNDLE_DATE = "DATE";
    private static final String BUNDLE_GROUPID = "GROUPID";

    private RecyclerView scheduleList;
    private View noScheduleMessage;
    private FloatingActionButton addEventButton;

    private String title;
    private Date date; // время 0:00 дня расписания
    private Long groupId;

    private ArrayList<ScheduleItem> scheduleForEditing;
    private HashMap<Long, ScheduleItem> scheduleItemById; // расписание до изменения

    private ArrayList<ScheduleItem> changedItems;
    private ArrayList<ScheduleItem> deletedItems;
    private ArrayList<ScheduleItem> addedItems;

    public static ScheduleEditFragment newInstance(long date, long groupId) {
        ScheduleEditFragment fragment = new ScheduleEditFragment();

        Bundle args = new Bundle();

        args.putLong(BUNDLE_DATE, date);
        args.putLong(BUNDLE_GROUPID, groupId);

        fragment.setRetainInstance(true);
        fragment.setArguments(args);
        fragment.loadSchedule(date, groupId);

        return fragment;
    }

    private void loadSchedule(long todayDate, long selectedGroupId) {

        changedItems = new ArrayList<>();
        deletedItems = new ArrayList<>();
        addedItems = new ArrayList<>();

        scheduleItemById = new HashMap<>();
        scheduleForEditing = new ArrayList<>();

        for (DailyScheduleItem item : User.getInstance().getDailySchedule()) {
            if (item.getSchedule().isEmpty()) {
                continue;
            }
            Date currentDate = item.getSchedule().get(0).getStartDate();

            if (DateTools.sameDay(new Date(todayDate), currentDate)) {
                for (ScheduleItem e : item.getSchedule()) {
                    if (e.getGroupId() == selectedGroupId) {
                        scheduleForEditing.add(new ScheduleItem(e));
                        scheduleItemById.put(e.getScheduleId(), new ScheduleItem(e));
                    }
                }
            }
        }
    }

    @Override
    public boolean onSave() {

        for (ScheduleItem e : scheduleForEditing) {
            if (e.getScheduleId() == null) {
                addedItems.add(e);
            } else if (!e.equals(scheduleItemById.get(e.getScheduleId()))) {
                changedItems.add(e);
            }
        }

        User user = User.getInstance();

        for (ScheduleItem item : changedItems) {
            ScheduleItem originalItem = user.getScheduleById().get(item.getScheduleId());
            int pos = user.getSchedule().indexOf(originalItem);
            user.getSchedule().set(pos, item);
            user.updateScheduleRequest(item.getScheduleId(), item.getStartDate().getTime(), item.getEndDate().getTime(), item.getComment(), item.getName(), 1, new ResponseCallback() {
                @Override
                public void onResponse(String data) {
                    // todo: сделать какую-нибудь проверку
                }

                @Override
                public void onFailure(String message) {
                    // todo: сделать какую-нибудь проверку
                }
            });
        }

        for (ScheduleItem item : deletedItems) {
            ScheduleItem originalItem = user.getScheduleById().get(item.getScheduleId());
            int pos = user.getSchedule().indexOf(originalItem);
            Long scheduleId = user.getSchedule().get(pos).getScheduleId();
            user.deleteScheduleRequest(scheduleId, new ResponseCallback() {
                @Override
                public void onResponse(String data) {
                    // todo: сделать какую-нибудь проверку
                }

                @Override
                public void onFailure(String message) {
                    // todo: сделать какую-нибудь проверку
                }
            });
            user.getSchedule().remove(pos);
            user.getScheduleById().remove(item.getScheduleId());
        }

        for (final ScheduleItem item : addedItems) {
            user.addScheduleRequest(1, 3, item.getStartDate().getTime(), item.getEndDate().getTime(), item.getComment(), item.getName(), new ResponseCallback() {
                @Override
                public void onResponse(String data) {
                    Long scheduleId = User.getInstance().updateAddedSchedule(data);
                    if (scheduleId != -1) {
                        ScheduleItem tmpItem = new ScheduleItem(item);
                        tmpItem.setScheduleId(scheduleId);
                        User.getInstance().getScheduleById().put(scheduleId, tmpItem);
                        User.getInstance().getSchedule().add(tmpItem);
                    }
                }

                @Override
                public void onFailure(String message) {
                    // а че поделаешь
                }
            });

            // для дебага
//            long scheduleId = new Random().nextLong();
//            item.setScheduleId(scheduleId);
//            user.getScheduleById().put(scheduleId, item);
            // для дебага


//            user.getSchedule().add(item);

        }

        user.prepareSchedule();

        getActivity().getSupportFragmentManager().popBackStack();

        /*
                 todo в onSave нужен callback который будет уведомлять о том что изменения загружены на сервер,
                 и по нему будет делаться popbackstack
                 */

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_edit, container, false);

        Bundle args = getArguments();
        date = new Date(args.getLong(BUNDLE_DATE));
        groupId = args.getLong(BUNDLE_GROUPID);


        // setting up actionbar
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        title = DateTools.dayMonthToString(day, month);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        scheduleList = view.findViewById(R.id.schedule_edit_list);
        noScheduleMessage = view.findViewById(R.id.no_schedule_message);
        addEventButton = view.findViewById(R.id.add_event_button);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (scheduleForEditing.isEmpty()) {
            noScheduleMessage.setVisibility(View.VISIBLE);
        }

        final ScheduleAdapter adapter = new ScheduleAdapter(scheduleForEditing, true);
        adapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, final ScheduleItem item, final int pos) {

                ScheduleEventEditFragment.ScheduleEventSaveCallback saveCallback =
                        new ScheduleEventEditFragment.ScheduleEventSaveCallback() {
                            @Override
                            public void onSave(ScheduleItem changedItem) {
                                scheduleForEditing.set(pos, changedItem);
                                adapter.sortItemsByTime();
                                adapter.notifyItemChanged(pos, changedItem);
                            }

                            @Override
                            public void onDelete() {
                                if (item.getScheduleId() != null) {
                                    deletedItems.add(item);
                                }
                                scheduleForEditing.remove(pos);
                                adapter.notifyItemRemoved(pos);
                            }
                        };
                ((MainActivity) getActivity()).loadFragment(
                        ScheduleEventEditFragment.newInstance(item, date, saveCallback, false));
            }
        });

        scheduleList.setLayoutManager(new LinearLayoutManager(getActivity()));
        scheduleList.setAdapter(adapter);


        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScheduleEventEditFragment.ScheduleEventSaveCallback saveCallback =
                        new ScheduleEventEditFragment.ScheduleEventSaveCallback() {
                            @Override
                            public void onSave(ScheduleItem changedItem) {
                                scheduleForEditing.add(changedItem);
                                adapter.sortItemsByTime();
                                adapter.notifyItemChanged(adapter.getItemCount() - 1);
                            }

                            @Override
                            public void onDelete() {

                            }
                        };

                ScheduleItem item = new ScheduleItem(groupId, date);
                ((MainActivity) getActivity()).loadFragment(
                        ScheduleEventEditFragment.newInstance(item, date, saveCallback, true));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackButtonPressed();
                return true;
            case R.id.group_edit_save_button:
                return onSave();
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.group_edit_menu, menu);
    }

    @Override
    public void onBackButtonPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (!canSwitch()) {
            builder.setTitle(Html.fromHtml("<b>Вы точно хотите выйти ?</b>"))
                    .setMessage("Вы не сохранили изменения")
                    .setCancelable(false)
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    })
                    .setNegativeButton("Нет", null);
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean canSwitch() {
        if (!deletedItems.isEmpty()) {
            return false;
        }

        for (ScheduleItem e : scheduleForEditing) {
            if (e.getScheduleId() == null) {
                return false;
            } else if (!e.equals(scheduleItemById.get(e.getScheduleId()))) {
                return false;
            }
        }
        return true;
    }
}