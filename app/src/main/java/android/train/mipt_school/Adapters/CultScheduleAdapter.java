package android.train.mipt_school.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CultScheduleAdapter extends RecyclerView.Adapter<CultScheduleAdapter.ScheduleHolder> {

    @NonNull
    @Override
    public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.cultschedule_item, parent, false);

        return new ScheduleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleHolder scheduleHolder, int i) {
        ScheduleItem item = User.getInstance().getSchedule().get(i);
        if (item.getComment() != null)
            scheduleHolder.bind(item);
    }

    @Override
    public int getItemCount() {
        return User.getInstance().getSchedule().size();
    }

    class ScheduleHolder extends RecyclerView.ViewHolder {

        private TextView eventTime;
        private TextView eventName;
        private TextView eventPlace;
        private TextView comment;


        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
            eventTime = itemView.findViewById(R.id.schedule_event_time);
            eventName = itemView.findViewById(R.id.schedule_event_name);
            eventPlace = itemView.findViewById(R.id.schedule_event_place);
            comment = itemView.findViewById(R.id.comment);
        }

        public void bind(ScheduleItem item) {
            eventName.setText(item.getName());
            eventPlace.setText(item.getPlace());
            eventTime.setText(item.getEventHours());
            comment.setText(item.getComment());
        }


    }

}
