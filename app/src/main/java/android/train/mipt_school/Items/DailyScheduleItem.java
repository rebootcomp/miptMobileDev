package android.train.mipt_school.Items;

import java.util.ArrayList;

public class DailyScheduleItem {
    private String date;
    private ArrayList<ScheduleItem> schedule;

    public DailyScheduleItem(String date, ArrayList<ScheduleItem> schedule) {
        this.date = date;
        this.schedule = schedule;
    }

    public ArrayList<ScheduleItem> getSchedule() {
        return schedule;
    }

    public String getDate() {
        return date;
    }
}
