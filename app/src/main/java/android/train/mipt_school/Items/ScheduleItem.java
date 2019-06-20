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
    private String comment;
    private Long day;

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getComment() {
        return comment;
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

    /*public ScheduleItem(Date startDate, Date endDate, String name, String place) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.place = place;

        SimpleDateFormat date = new SimpleDateFormat("hh:mm");

        if (endDate != null) {
            eventHours = date.format(startDate) + " - " + date.format(endDate);
        } else {
            eventHours = date.format(startDate);
        }
    }*/

    public ScheduleItem(Long startDate, Long endDate, String name, String place, String comment) {
        this.name = name;
        this.place = place;
        this.comment = comment;
        this.startDate = new Date(startDate*1000l);
        this.endDate = new Date(endDate*1000l);
//        if (endDate != null) {
//            eventHours = startDate.substring(11, 16) + " - " + endDate.substring(11, 16);
//        } else {
//            eventHours = startDate.substring(11, 16);
//        }
    }

}
