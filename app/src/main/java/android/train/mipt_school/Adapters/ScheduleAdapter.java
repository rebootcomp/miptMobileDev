package android.train.mipt_school.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.R;
import android.train.mipt_school.SchedulePageFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.Date;
import java.util.List;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {

    private List<ScheduleItem> listItems;

    public ScheduleAdapter(List<ScheduleItem> listItems) {
        this.listItems = listItems;
    }

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
        ScheduleItem item = listItems.get(i);
        scheduleHolder.bind(item);
    }

    @Override
    public int getItemCount() {
        /*int size = 0;
        for (int i = 0; i < User.getInstance().getSchedule().size(); i++) {
            ScheduleItem item = User.getInstance().getSchedule().get(i);
            // Форматирование времени как "день.месяц.год"
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            String dateText1 = dateFormat.format(item.getStartDate());
            String dateText2 = dateFormat.format(date);
            if (dateText1.equals(dateText2)) {
                size++;
            }
        }
        Log.i("Size", size + "");*/
        return listItems.size();
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
