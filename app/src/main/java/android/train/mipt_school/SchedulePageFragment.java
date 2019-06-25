package android.train.mipt_school;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.Adapters.ScheduleAdapter;
import android.train.mipt_school.Adapters.ScheduleTabsAdapter;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.DailyScheduleItem;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.Tools.OnSwipeTouchListener;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SchedulePageFragment extends Fragment implements SceneFragment {


    private String title;
    private static ArrayList<DailyScheduleFragment> schedule;
    private static Date date = new Date();


    public static SchedulePageFragment newInstance() {
        SchedulePageFragment fragment = new SchedulePageFragment();
        //fragment.setRetainInstance(true);
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


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).loadingScreen(true);

        schedule = new ArrayList<>();

        // инициализация расписания на каждый день

        ((MainActivity) getActivity()).loadingScreen(true);

        for (int i = 0; i < User.getInstance().getDailySchedule().size(); i++) {
            DailyScheduleItem item = User.getInstance().getDailySchedule().get(i);
            schedule.add(DailyScheduleFragment.newInstance(item.getDate(), i));
        }


        ViewPager viewPager = view.findViewById(R.id.schedule_pager);
        PagerAdapter pagerAdapter =
                new ScheduleTabsAdapter(getChildFragmentManager(), schedule);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        TabLayout tabLayout = view.findViewById(R.id.schedule_tablayout);

        tabLayout.setupWithViewPager(viewPager);

        ((MainActivity) getActivity()).loadingScreen(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = context.getString(R.string.schedule_page_title);
    }

    @Override
    public void onBackButtonPressed() {
        ((MainActivity) getActivity()).loadFragment(MainPageFragment.newInstance());
    }

    @Override
    public String getTitle() {
        return title;
    }


    public static Date getDate() {
        return date;
    }

    //Возварщает к сегодняшнему дню
    /*public void today() {
        date = new Date();
        scheduleForDate();
    }

    //Обновляем расписание на выбранный день
    public void scheduleForDate() {
        List<ScheduleItem> listItems = check();

        String dateText = dateFormat.format(date);
        title = "Расписание на " + dateText;
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        scheduleList = getView().findViewById(R.id.schedule_view);
        scheduleList.setAdapter(new ScheduleAdapter(listItems));
        scheduleList.setLayoutManager(new LinearLayoutManager(getContext()));
    }*/

    //Создаёт массив нужных ScheduleItem
    /*public List<ScheduleItem> check() {
        List<ScheduleItem> listItems = new ArrayList<ScheduleItem>();
        for (int i = 0; i < User.getInstance().getSchedule().size(); i++) {
            ScheduleItem item = User.getInstance().getSchedule().get(i);
            String dateText1 = dateFormat.format(item.getStartDate());
            String dateText2 = dateFormat.format(date);
            if (dateText1.equals(dateText2)) {
                listItems.add(item);
            }
        }
        return listItems;
    }*/
}
