package android.train.mipt_school;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class ScheduleEditFragment extends Fragment implements SceneFragment {


    private String title;
    private boolean changesSaved = true;

    public static ScheduleEditFragment newInstance() {
        ScheduleEditFragment fragment = new ScheduleEditFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_edit, container, false);

        // setting up actionbar
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackButtonPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackButtonPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (!changesSaved) {
            builder.setTitle("Вы точно хотите выйти?")
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
    public void onAttach(Context context) {
        super.onAttach(context);
        title = context.getString(R.string.contacts_page_title);
    }

    @Override
    public String getTitle() {
        return title;
    }

}