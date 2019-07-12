package android.train.mipt_school.Items;

import android.train.mipt_school.Tools.DateTools;

import java.util.ArrayList;
import java.util.Date;

public class DailyScheduleItem {
    private Date date;
    private ArrayList<ScheduleItem> schedule;

    public DailyScheduleItem(Date date, ArrayList<ScheduleItem> schedule) {
        this.date = date;
        this.schedule = schedule;
    }

    public ArrayList<ScheduleItem> getSchedule() {
        return schedule;
    }

    public Date getDate() {
        return date;
    }
}
