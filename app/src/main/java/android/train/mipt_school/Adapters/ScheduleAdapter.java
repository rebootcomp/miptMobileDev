package android.train.mipt_school.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {

    private ArrayList<ScheduleItem> data;


    @NonNull
    @Override
    public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);

        return new ScheduleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleHolder scheduleHolder, int i) {
        ScheduleItem item = data.get(i);
        scheduleHolder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<ScheduleItem> data) {
        this.data = data;
    }

    public ArrayList<ScheduleItem> getData() {
        return data;
    }

    class ScheduleHolder extends RecyclerView.ViewHolder {

        private TextView eventTime;
        private TextView eventName;
        private TextView eventPlace;


        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
            eventTime = itemView.findViewById(R.id.schedule_event_time);
            eventName = itemView.findViewById(R.id.schedule_event_name);
            eventPlace = itemView.findViewById(R.id.schedule_event_place);
        }

        public void bind(ScheduleItem item) {
            eventName.setText(item.getName());
            eventPlace.setText(item.getPlace());
            eventTime.setText(item.getEventHours());
        }


    }
}
