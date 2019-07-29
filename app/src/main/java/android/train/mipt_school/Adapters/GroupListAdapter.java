package android.train.mipt_school.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.GroupViewFragment;
import android.train.mipt_school.Items.GroupItem;
import android.train.mipt_school.MainActivity;
import android.train.mipt_school.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupHolder> {

    private ArrayList<GroupItem> data;

    private onItemClickedListener listener;

    public GroupListAdapter(ArrayList<GroupItem> data) {
        this.data = data;
    }

    public void refresh() {
        data.clear();
        data = User.getInstance().getAllGroups();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.group_item, viewGroup, false);
        return new GroupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder groupHolder, int i) {
        GroupItem item = data.get(i);
        groupHolder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class GroupHolder extends RecyclerView.ViewHolder {
        private TextView groupName;
        private TextView eventName;
        private TextView countOfUsers;
        private long groupId;
        private View groupViewButton;

        public GroupHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_name);
            eventName = itemView.findViewById(R.id.event_name);
            countOfUsers = itemView.findViewById(R.id.count_of_users);
            groupViewButton = itemView.findViewById(R.id.group_view_button);
        }

        public void bind(final GroupItem item) {
            groupId = item.getGroupId();
            groupName.setText(item.getName());
            eventName.setText(item.getEventName());

            long members = item.getCountOfUsers();

            if (members % 10 > 1 && members % 10 < 5) {
                countOfUsers.setText(members + " участника");
            } else if (members % 10 == 1) {
                countOfUsers.setText(members + " участник");
            } else {
                countOfUsers.setText(members + " участников");
            }

            groupViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClicked(item);
                    }
                }
            });
        }
    }

    public void setListener(onItemClickedListener listener) {
        this.listener = listener;
    }

    public interface onItemClickedListener {
        void onItemClicked(GroupItem item);
    }
}
