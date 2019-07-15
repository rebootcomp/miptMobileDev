package android.train.mipt_school.Items;

public class UpdatableScheduleItem {
    public static final int CHANGE = 1;
    public static final int ADD = 2;
    public static final int DELETE = 3;

    private ScheduleItem eventForUpdate;
    private int updateReason;

    public UpdatableScheduleItem(ScheduleItem eventForUpdate, int updateReason) {
        this.eventForUpdate = eventForUpdate;
        this.updateReason = updateReason;
    }

    public ScheduleItem getEventForUpdate() {
        return eventForUpdate;
    }

    public int getUpdateReason() {
        return updateReason;
    }

}
