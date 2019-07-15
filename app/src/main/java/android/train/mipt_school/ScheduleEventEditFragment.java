package android.train.mipt_school;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ScheduleEventEditFragment extends Fragment implements SceneFragment, DataSavingFragment {

    private String title;
    private static final String BUNDLE_ISADD = "ADD";

    private ScheduleItem eventForEditing;

    private Date currentDate; // текущая дата, то есть 0:00 дня расписания
    private Date pickedStartDate;
    private Date pickedEndDate;

    private EditText eventName;
    private EditText eventPlace;
    private EditText eventComment;
    private TextView eventStart;
    private TextView eventEnd;

    private ScheduleEventSaveCallback saveCallback;
    private boolean isAddAction;


    public static ScheduleEventEditFragment
    newInstance(ScheduleItem event, Date date, ScheduleEventSaveCallback callback, boolean isDelete) {

        ScheduleEventEditFragment instance = new ScheduleEventEditFragment();


        instance.setRetainInstance(true);
        Bundle args = new Bundle();

        args.putBoolean(BUNDLE_ISADD, isDelete);

        instance.setArguments(args);
        instance.setEventForEditing(event);
        instance.setCurrentDate(date);
        instance.setSaveCallback(callback);

        return instance;
    }


    public void setEventForEditing(ScheduleItem eventForEditing) {
        this.eventForEditing = new ScheduleItem(eventForEditing);
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =
                inflater.inflate(R.layout.fragment_schedule_event_edit, container, false);
        // setting up actionbar

        Bundle args = getArguments();

        isAddAction = args.getBoolean(BUNDLE_ISADD);

        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        eventName = view.findViewById(R.id.event_edit_event_name);
        eventPlace = view.findViewById(R.id.event_edit_event_place);
        eventComment = view.findViewById(R.id.event_edit_event_comment);

        eventStart = view.findViewById(R.id.event_edit_event_start);
        eventEnd = view.findViewById(R.id.event_edit_event_end);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventName.setText(eventForEditing.getName());
        eventPlace.setText(eventForEditing.getPlace());
        eventComment.setText(eventForEditing.getComment());

        eventStart.setText(DateTools.hourMinuteFormat(eventForEditing.getStartDate()));
        eventEnd.setText(DateTools.hourMinuteFormat(eventForEditing.getEndDate()));


        pickedStartDate = eventForEditing.getStartDate();
        pickedEndDate = eventForEditing.getEndDate();

        eventStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener timeSetListener =
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                long offset =
                                        ((long) hourOfDay * 3600L + (long) minute * 60L) * 1000L;

                                pickedStartDate = new Date(currentDate.getTime() + offset);

                                eventStart.setText(DateTools.hourMinuteFormat(pickedStartDate));
                            }
                        };

                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(pickedStartDate);

                TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                        timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true);
                tpd.show();
            }
        });


        eventEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener timeSetListener =
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                long offset =
                                        ((long) hourOfDay * 3600L + (long) minute * 60L) * 1000L;

                                pickedEndDate = new Date(currentDate.getTime() + offset);

                                eventEnd.setText(DateTools.hourMinuteFormat(pickedEndDate));
                            }
                        };

                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(pickedEndDate);

                TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                        timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true);

                tpd.show();
            }
        });
    }

    public void onDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(Html.fromHtml("<b>Вы точно хотите удалить событие ?</b>"))
                .setMessage("Восстановить событие будет невозможно")
                .setCancelable(false)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveCallback.onDelete();
                        getActivity().getSupportFragmentManager().popBackStack();

                    }
                })
                .setNegativeButton("Нет", null);
        builder.show();
    }

    @Override
    public boolean canSwitch() {
        return eventForEditing.getComment().equals(eventComment.getText().toString()) &&
                eventForEditing.getName().equals(eventName.getText().toString()) &&
                eventForEditing.getPlace().equals(eventPlace.getText().toString()) &&
                eventForEditing.getStartDate().equals(pickedStartDate) &&
                eventForEditing.getEndDate().equals(pickedEndDate);
    }

    @Override
    public boolean onSave() {
        eventForEditing.setComment(eventComment.getText().toString());
        eventForEditing.setName(eventName.getText().toString());
        eventForEditing.setPlace(eventPlace.getText().toString());

        eventForEditing.setStartDate(pickedStartDate);
        eventForEditing.setEndDate(pickedEndDate);
        return true;
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
            ((MainActivity) getActivity()).hideKeyboard();
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = context.getString(R.string.schedule_event_edit_page_title);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackButtonPressed();
                return true;
            case R.id.event_edit_save_button:
                onSave();
                saveCallback.onSave(eventForEditing);
                ((MainActivity) getActivity()).hideKeyboard();
                onBackButtonPressed();
                return true;
            case R.id.event_edit_delete_button:
                onDelete();

        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (isAddAction) {
            inflater.inflate(R.menu.event_edit_menu_add, menu);
        } else {
            inflater.inflate(R.menu.event_edit_menu, menu);
        }
    }

    public void setSaveCallback(ScheduleEventSaveCallback saveCallback) {
        this.saveCallback = saveCallback;
    }

    interface ScheduleEventSaveCallback {
        void onSave(ScheduleItem item);

        void onDelete();
    }
}
