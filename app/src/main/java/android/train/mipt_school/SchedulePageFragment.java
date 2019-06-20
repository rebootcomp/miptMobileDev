package android.train.mipt_school;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.Adapters.ScheduleAdapter;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SchedulePageFragment extends Fragment implements SceneFragment {


    private RecyclerView scheduleList;
    private String title;
    // Текущее время
    private static Date date = new Date();
    // Форматирование времени как "день.месяц.год"
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    private String dateText = dateFormat.format(date);


    public static SchedulePageFragment newInstance() {
        SchedulePageFragment fragment = new SchedulePageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        // setting up actionbar
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        scheduleList = view.findViewById(R.id.schedule_view);


        scheduleList.setAdapter(new ScheduleAdapter());
        scheduleList.setLayoutManager(new LinearLayoutManager(this.getContext()));


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = "Расписание на " + dateText;
    }

    @Override
    public void onBackButtonPressed() {
        ((MainActivity) getActivity()).loadFragment(MainPageFragment.newInstance());
    }

    @Override
    public String getTitle() {
        return title;
    }


    public static Date getDate(){
        return date;
    }

    //на 1 день назад
    //TODO: анимация при переходе
    public void onLeftSwipe() {
        date = new Date(date.getTime() - 24*3600*1000l);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(date);
        title = "Расписание на " + dateText;
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

    }

    //на 1 день вперёд
    //TODO: анимация при переходе
    public void onRightSwipe() {
        date = new Date(date.getTime() - 24*3600*1000l);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(date);
        title = "Расписание на " + dateText;
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

    }
}
