package android.train.mipt_school;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.Adapters.ScheduleAdapter;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.Tools.DateTools;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.train.mipt_school.R;

import java.util.ArrayList;
import java.util.Date;

public class DailyScheduleFragment extends Fragment {

    private Date date;
    private int position;
    private RecyclerView scheduleList;
    public static final String BUNLDE_DATE = "DATE";
    public static final String BUNDLE_POS = "POSITION";

    public static DailyScheduleFragment newInstance(long date, int position) {
        DailyScheduleFragment instance = new DailyScheduleFragment();
        Bundle args = new Bundle();

        args.putLong(BUNLDE_DATE, date);
        args.putInt(BUNDLE_POS, position);

        instance.setDate(new Date(date));
        instance.setPosition(position);

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
        date = new Date(bundle.getLong(BUNLDE_DATE));


        scheduleList.setAdapter(
                new ScheduleAdapter(User
                        .getInstance()
                        .getDailySchedule()
                        .get(position)
                        .getSchedule(), false));

        scheduleList.setLayoutManager(new LinearLayoutManager(this.getContext()));


        return view;
    }


    public Date getDate() {
        return date;
    }

    public String getDateString() {
        return String.format("%s %s",
                DateTools.dayOfWeekFormat(date),
                DateTools.dayMonthFormat(date));
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
