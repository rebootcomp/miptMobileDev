package android.train.mipt_school.Items;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ScheduleItem {
    private Date startDate;
    private Date endDate;
    private String name;
    private String place;
    private String eventHours;

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public String getEventHours() {
        return eventHours;
    }

    public ScheduleItem(Date startDate, Date endDate, String name, String place) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.place = place;

        if (endDate != null) {
            SimpleDateFormat date = new SimpleDateFormat("hh:mm");
            eventHours = date.format(startDate) + " - " + date.format(endDate);
        } else {
            SimpleDateFormat date = new SimpleDateFormat("hh:mm");
            eventHours = date.format(startDate);
        }
    }

}
