package android.train.mipt_school;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.train.mipt_school.DataHolders.Group;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Tools.DataSavingFragment;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GroupEditFragment extends Fragment implements SceneFragment, DataSavingFragment {


    private String title;
    private EditText groupNameField;
    private Group editGroup;

    private View scheduleEditButton;
    private EditText eventNameField;
    private EditText directionNameField;

    private String groupName;
    private String eventName;
    private String directionName;

    public static final String BUNDLE_POS = "POSITION";

    public static GroupEditFragment newInstance(int groupPosition) {

        GroupEditFragment fragment = new GroupEditFragment();

        Bundle args = new Bundle();
        args.putInt(BUNDLE_POS, groupPosition);

        Group GroupForEditing = User.getInstance().getGroups().get(groupPosition);

        fragment.setArguments(args);
        fragment.setRetainInstance(true);

        fragment.setEventName(GroupForEditing.getEvent());
        fragment.setGroupName(GroupForEditing.getName());
        fragment.setDirectionName(GroupForEditing.getDirection());

        return fragment;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group_edit, container, false);

        // setting up actionbar
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        groupNameField = view.findViewById(R.id.group_name_edit_field);
        eventNameField = view.findViewById(R.id.group_event_name_field);
        directionNameField = view.findViewById(R.id.group_direction_name_field);

        scheduleEditButton = view.findViewById(R.id.schedule_edit_button);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();

        final int position = args.getInt(BUNDLE_POS);
        editGroup = User.getInstance().getGroups().get(position);

        groupNameField.setText(groupName);
        eventNameField.setText(eventName);
        directionNameField.setText(directionName);

        Calendar dateAndTime = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                long pickedDate = new GregorianCalendar(year, month, dayOfMonth).getTime().getTime();
                ((MainActivity) getActivity()).loadFragment(
                        ScheduleEditFragment.newInstance(pickedDate, editGroup.getId()));
            }
        };

        final DatePickerDialog tpd = new DatePickerDialog(
                getActivity(),
                listener,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));

        scheduleEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tpd.show();
            }
        });
    }

    @Override
    public void onBackButtonPressed() {

        if (canSwitch()) {
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.group_edit_menu, menu);
    }

    @Override
    public boolean onSave() {

        editGroup.setName(groupNameField.getText().toString());
        editGroup.setEvent(eventNameField.getText().toString());
        editGroup.setDirection(directionNameField.getText().toString());

        // TODO обновлять название, направление и мероприятие группы на сервере

        getActivity().getSupportFragmentManager().popBackStack();
        return true;
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
    public String getTitle() {
        return title;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = context.getString(R.string.group_edit_page_title);
    }

    @Override
    public boolean canSwitch() {
        return groupNameField.getText().toString().equals(editGroup.getName()) &&
                eventNameField.getText().toString().equals(editGroup.getEvent()) &&
                directionNameField.getText().toString().equals(editGroup.getDirection());
    }
}
