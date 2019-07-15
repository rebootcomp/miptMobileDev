package android.train.mipt_school.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SmallContactAdapter extends RecyclerView.Adapter<SmallContactAdapter.ContactHolder> {

    private ArrayList<ContactItem> data;

    public SmallContactAdapter(ArrayList<ContactItem> data) {
        this.data = data;
    }


    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.contact_item_small, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactHolder contactHolder, int i) {
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
        private TextView shortName;

        public ContactHolder(@NonNull final View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.contact_profile_image);
            userName = itemView.findViewById(R.id.contact_profile_name);
            shortName = itemView.findViewById(R.id.short_name_contact_item);
        }

        public void bind(ContactItem item) {

            if (item.getImage() != null) {
                userImage.setImageBitmap(item.getImage());
            }
            userName.setText(item.getName());
            shortName.setText(item.getFirstName().substring(0, 1) + item.getLastName().substring(0, 1));
        }

    }


    public void setData(ArrayList<ContactItem> data) {
        this.data = data;
    }

    public ArrayList<ContactItem> getData() {
        return data;
    }
}
