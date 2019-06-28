package android.train.mipt_school.Items;


import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class ScheduleItem {
    private Date startDate;
    private Date endDate;
    private String name;
    private String place;
    private String eventHours;
    private String comment;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date date) {
        this.startDate = date;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date date) {
        this.endDate = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment ) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getEventHours() {
        return eventHours;
    }

    public void setEventHours(String eventHours) {
        this.eventHours = eventHours;
    }



    public ScheduleItem(Long startDate, Long endDate, String name, String place, String comment) {
        this.name = name;
        this.place = place;
        this.comment = comment;
        this.startDate = new Date(startDate * 1000L);
        this.endDate = new Date(endDate * 1000L);
        //TODO: нормально посчитать eventHours
        Locale local = new Locale("ru","RU");
        DateFormat df = DateFormat.getTimeInstance(DateFormat.DEFAULT, local);
        this.eventHours = df.format(this.startDate).substring(0, 5) + " - " + df.format(this.endDate).substring(0, 5);
    }

}
