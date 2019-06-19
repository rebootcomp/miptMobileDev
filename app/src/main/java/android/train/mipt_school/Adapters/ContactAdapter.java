package android.train.mipt_school.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.MainActivity;
import android.train.mipt_school.ProfilePageFragment;
import android.train.mipt_school.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private OnItemClickListener listener;

    public void setData(ArrayList<ContactItem> data) {
        this.data = data;
    }

    private ArrayList<ContactItem> data;

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder contactHolder, int i) {
        ContactItem currentItem = data.get(i);
        contactHolder.bind(currentItem);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private TextView userName;

        private View profileButton;
        private long userId;

        public ContactHolder(@NonNull final View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.contact_profile_image);
            userName = itemView.findViewById(R.id.contact_profile_name);
            profileButton = itemView.findViewById(R.id.contact_button_profile);

            profileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }

        public void bind(ContactItem item) {

            if (item.getImage() != null) {
                userImage.setImageBitmap(item.getImage());
            }
            userId = item.getUserId();
            userName.setText(item.getName());
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

}
