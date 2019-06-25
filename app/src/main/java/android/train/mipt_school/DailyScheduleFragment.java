package android.train.mipt_school;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.Adapters.ScheduleAdapter;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.ScheduleItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.train.mipt_school.R;

import java.util.ArrayList;

public class DailyScheduleFragment extends Fragment {

    private String date;
    private int position;
    private RecyclerView scheduleList;

    public static final String BUNLDE_DATE = "DATE";
    public static final String BUNDLE_POS = "POSITION";

    public static DailyScheduleFragment newInstance(String date, int position) {
        DailyScheduleFragment instance = new DailyScheduleFragment();
        Bundle args = new Bundle();

        instance.date = date;
        instance.position = position;

        args.putString(BUNLDE_DATE, date);
        args.putInt(BUNDLE_POS, position);

        instance.setArguments(args);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_daily_schedule, container, false);
        scheduleList = view.findViewById(R.id.schedule_view);

        Bundle bundle = getArguments();
        position = bundle.getInt(BUNDLE_POS);
        date = bundle.getString(BUNLDE_DATE);


        scheduleList.setAdapter(
                new ScheduleAdapter(User
                        .getInstance()
                        .getDailySchedule()
                        .get(position)
                        .getSchedule()));

        scheduleList.setLayoutManager(new LinearLayoutManager(this.getContext()));


        return view;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
