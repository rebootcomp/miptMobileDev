package android.train.mipt_school;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.train.mipt_school.Adapters.ScheduleTabsAdapter;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.DailyScheduleItem;
import android.train.mipt_school.Tools.DateTools;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;


public class SchedulePageFragment extends Fragment implements SceneFragment {


    private String title;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<DailyScheduleFragment> schedule;


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

        ((MainActivity) getActivity()).setLoadingScreenState(true);

        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        swipeRefreshLayout = view.findViewById(R.id.refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //TODO: обновление расписания

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // setting up actionbar
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        schedule = new ArrayList<>();

        // инициализация расписания на каждый день

        int selectedPagePosition = 0;

        for (int i = 0; i < User.getInstance().getDailySchedule().size(); i++) {
            DailyScheduleItem item = User.getInstance().getDailySchedule().get(i);
            schedule.add(DailyScheduleFragment.newInstance(item.getDate().getTime(), i));
            if (item.getDate().getTime() < DateTools.getCurrentDayStart().getTime()) {
                selectedPagePosition++;
            }
        }

        selectedPagePosition = Math.min(selectedPagePosition,
                User.getInstance().getDailySchedule().size() - 1);


        ViewPager viewPager = view.findViewById(R.id.schedule_pager);
        PagerAdapter pagerAdapter =
                new ScheduleTabsAdapter(getChildFragmentManager(), schedule);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.schedule_tablayout);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(selectedPagePosition);

        ((MainActivity) getActivity()).setLoadingScreenState(false);
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
}
