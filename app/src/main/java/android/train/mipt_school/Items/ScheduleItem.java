package android.train.mipt_school.Items;


import android.train.mipt_school.Tools.DateTools;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ScheduleItem {
    private Date startDate;
    private Date endDate;
    private String name;
    private String place;
    private String comment;

    private Long groupId;

    private Long scheduleId;

    public ScheduleItem(long groupId, Date dayStartDate) {
        long timeSinceMidnight = DateTools.getTimeSinceMidnight();
        this.name = "";
        this.place = "";
        this.comment = "";
        this.startDate = new Date(dayStartDate.getTime() + timeSinceMidnight);
        this.endDate = new Date(dayStartDate.getTime() + timeSinceMidnight);
        this.groupId = groupId;
    }

    public ScheduleItem(Date startDate,
                        Date endDate,
                        String name,
                        String place,
                        String comment,
                        Long groupId,
                        Long scheduleId) {

        this.name = name;
        this.place = place;
        this.comment = comment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.groupId = groupId;
        this.scheduleId = scheduleId;
    }

    public ScheduleItem(ScheduleItem other) { // для клонирования
        this(other.startDate,
                other.endDate,
                other.name,
                other.place,
                other.comment,
                other.groupId,
                other.scheduleId);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return this.startDate.equals(((ScheduleItem) o).getStartDate()) &&
                this.endDate.equals(((ScheduleItem) o).getEndDate()) &&
                this.name.equals(((ScheduleItem) o).getName()) &&
                this.place.equals(((ScheduleItem) o).getPlace()) &&
                this.comment.equals(((ScheduleItem) o).getComment());
    }

    public String getEventHours() {
        String startTime = DateTools.hourMinuteFormat(getStartDate());
        String endTime = DateTools.hourMinuteFormat(getEndDate());

        if (startTime.equals(endTime)) {
            return startTime;
        }

        return String.format("%s - %s", startTime, endTime);
    }

    public Date getStartDate() {
        return startDate;
    }

    public Long getScheduleId() {
        return scheduleId;
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

    public void setComment(String comment) {
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

    public Long getGroupId() {
        return groupId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }


}
