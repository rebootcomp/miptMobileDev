package android.train.mipt_school;


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
import java.util.GregorianCalendar;

public class GroupEditFragment extends Fragment implements SceneFragment, DataSavingFragment {


    private String title;
    private Group editGroup;

    private View scheduleEditButton;
    private View adminsEditButton;
    private View usersEditButon;

    private EditText groupNameField;

    private String groupName;

    private static final String BUNDLE_POS = "POSITION";
    private static final String BUNLDE_GROUP_NAME = "NAME";

    public static GroupEditFragment newInstance(int groupPosition) {

        GroupEditFragment fragment = new GroupEditFragment();
        Group GroupForEditing = User.getInstance().getGroups().get(groupPosition);

        Bundle args = new Bundle();
        args.putInt(BUNDLE_POS, groupPosition);
        args.putString(BUNLDE_GROUP_NAME, GroupForEditing.getName());


        fragment.setArguments(args);
        fragment.setRetainInstance(true);

        return fragment;
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

        scheduleEditButton = view.findViewById(R.id.schedule_edit_button);
        adminsEditButton = view.findViewById(R.id.admins_edit_button);
        usersEditButon = view.findViewById(R.id.users_edit_button);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();

        final int position = args.getInt(BUNDLE_POS);
        groupName = args.getString(BUNLDE_GROUP_NAME);

        editGroup = User.getInstance().getGroups().get(position);

        groupNameField.setText(groupName);

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

        adminsEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] choices = new String[]{"Добавить", "Удалить"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(Html.fromHtml("<b>Выберите действие</b>"))
                        .setItems(choices, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    ((MainActivity) getActivity()).loadFragment(UsersAddFragment
                                            .newInstance(UsersAddFragment.ADD_ADMINS,
                                                    editGroup));
                                } else {
                                    ((MainActivity) getActivity()).loadFragment(UsersDeleteFragment
                                            .newInstance(UsersDeleteFragment.DELETE_ADMINS,
                                                    editGroup));
                                }
                            }
                        });

                builder.show();
            }
        });

        usersEditButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] choices = new String[]{"Добавить", "Удалить"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(Html.fromHtml("<b>Выберите действие</b>"))
                        .setItems(choices, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    ((MainActivity) getActivity()).loadFragment(UsersAddFragment
                                            .newInstance(UsersAddFragment.ADD_USERS,
                                                    editGroup));
                                } else {
                                    ((MainActivity) getActivity()).loadFragment(UsersDeleteFragment
                                            .newInstance(UsersDeleteFragment.DELETE_USERS,
                                                    editGroup));
                                }
                            }
                        });

                builder.show();
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

        // TODO обновлять название группы на сервере

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
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
    }

    @Override
    public boolean canSwitch() {
        return groupNameField.getText().toString().equals(editGroup.getName());
    }
}
