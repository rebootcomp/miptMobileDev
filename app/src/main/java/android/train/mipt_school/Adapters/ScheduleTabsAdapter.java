package android.train.mipt_school.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.train.mipt_school.DailyScheduleFragment;
import android.train.mipt_school.Items.ScheduleItem;

import java.util.ArrayList;

public class ScheduleTabsAdapter extends FragmentPagerAdapter {


    private ArrayList<DailyScheduleFragment> dailySchedules;

    public ScheduleTabsAdapter(FragmentManager fm, ArrayList<DailyScheduleFragment> dailySchedules) {
        super(fm);
        this.dailySchedules = dailySchedules;
    }


    @Override
    public Fragment getItem(int i) {
        return dailySchedules.get(i);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return dailySchedules.get(position).getDateString();
    }

    @Override
    public int getCount() {
        return dailySchedules.size();
    }
}
