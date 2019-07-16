package android.train.mipt_school.DataHolders;

import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.Items.UpdatableScheduleItem;
import android.train.mipt_school.ResponseCallback;
import android.train.mipt_school.RetrofitClient;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupData {
    public static void updateGroupSchedule(final int pos, final ArrayList<UpdatableScheduleItem> forUpdate,
                                           final AsyncResponseCallback callback) {
        if (pos == forUpdate.size()) {
            callback.onLoadingFinished();
            return;
        }
        UpdatableScheduleItem updatableItem = forUpdate.get(pos);
        final User user = User.getInstance();
        final ScheduleItem event = updatableItem.getEventForUpdate();

        switch (updatableItem.getUpdateReason()) {

            case UpdatableScheduleItem.ADD:
                user.addScheduleRequest(1,
                        3,
                        event.getStartDate().getTime() / 1000L,
                        event.getEndDate().getTime() / 1000L,
                        event.getComment(),
                        event.getName(),
                        new ResponseCallback() {
                            @Override
                            public void onResponse(String data) {
                                Long scheduleId = User.getInstance().updateAddedSchedule(data);
                                if (scheduleId != -1) {
                                    ScheduleItem tmpItem = new ScheduleItem(event);
                                    tmpItem.setScheduleId(scheduleId);
                                    User.getInstance().getScheduleById().put(scheduleId, tmpItem);
                                    User.getInstance().getSchedule().add(tmpItem);
                                }
                                Log.d("schedule_event_log", "event added : " + scheduleId);
                                updateGroupSchedule(pos + 1, forUpdate, callback);
                            }

                            @Override
                            public void onFailure(String message) {
                                Log.d("schedule_event_log", "event not added :( ");
                                updateGroupSchedule(pos + 1, forUpdate, callback);
                            }
                        });
                break;


            case UpdatableScheduleItem.DELETE:
                user.deleteScheduleRequest(event.getScheduleId(), new ResponseCallback() {
                    @Override
                    public void onResponse(String data) {
                        Log.d("schedule_event_log", "event deleted id=" +
                                event.getScheduleId() +
                                " : " + data);
                        user.getSchedule().remove(user.getSchedule().indexOf(event));
                        user.getScheduleById().remove(event.getScheduleId());
                        updateGroupSchedule(pos + 1, forUpdate, callback);
                    }

                    @Override
                    public void onFailure(String message) {

                        Log.d("schedule_event_log", "event not deleted :( ");
                        updateGroupSchedule(pos + 1, forUpdate, callback);
                    }
                });
                break;


            case UpdatableScheduleItem.CHANGE:
                ScheduleItem originalItem = user.getScheduleById().get(event.getScheduleId());
                int schedulePos = user.getSchedule().indexOf(originalItem);
                user.getSchedule().set(schedulePos, event);
                user.updateScheduleRequest(event.getScheduleId(),
                        event.getStartDate().getTime(),
                        event.getEndDate().getTime(),
                        event.getComment(),
                        event.getName(),
                        1,
                        new ResponseCallback() {
                            @Override
                            public void onResponse(String data) {
                                Log.d("schedule_event_log", "event changed : " + data);
                                updateGroupSchedule(pos + 1, forUpdate, callback);
                            }

                            @Override
                            public void onFailure(String message) {
                                Log.d("schedule_event_log", "event not changed :( ");
                                updateGroupSchedule(pos + 1, forUpdate, callback);
                            }
                        });
                break;


            default:
                updateGroupSchedule(pos + 1, forUpdate, callback);
                break;
        }
    }

    public interface AsyncResponseCallback {
        void onLoadingFinished();
    }
}
