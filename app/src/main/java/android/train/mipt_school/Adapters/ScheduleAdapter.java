package android.train.mipt_school.Adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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


import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {

    private List<ScheduleItem> listItems;
    private OnItemClickListener listener;
    private boolean itemsClickable;

    public ScheduleAdapter(List<ScheduleItem> listItems, boolean itemsClickable) {
        this.listItems = listItems;
        this.itemsClickable = itemsClickable;
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
        return listItems.size();
    }

    public List<ScheduleItem> getListItems() {
        return listItems;
    }

    public void sortItemsByTime() {
        Collections.sort(listItems, new Comparator<ScheduleItem>() {
            @Override
            public int compare(ScheduleItem o1, ScheduleItem o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
    }

    class ScheduleHolder extends RecyclerView.ViewHolder {

        private TextView eventTime;
        private TextView eventName;
        private TextView eventPlace;
        private ConstraintLayout eventLayout;


        public ScheduleHolder(@NonNull final View itemView) {
            super(itemView);
            eventTime = itemView.findViewById(R.id.schedule_event_time);
            eventName = itemView.findViewById(R.id.schedule_event_name);
            eventPlace = itemView.findViewById(R.id.schedule_event_place);
            eventLayout = itemView.findViewById(R.id.schedule_item_layout);

            eventLayout.setClickable(itemsClickable);

            if (listener != null) {
                eventLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        listener.onItemClick(itemView, listItems.get(pos), pos);
                    }
                });
            }
        }

        public void bind(ScheduleItem item) {
            eventName.setText(item.getName());
            eventPlace.setText(item.getPlace());
            eventTime.setText(item.getEventHours());
        }


    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, ScheduleItem item, int pos);
    }

    public void setOnItemClickListener(ScheduleAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}
