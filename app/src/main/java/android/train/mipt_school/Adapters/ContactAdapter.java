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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private OnItemClickListener listener;

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
        ContactItem currentItem = User.getInstance().getContacts().get(i);
        contactHolder.bind(currentItem);
    }

    @Override
    public int getItemCount() {
        return User.getInstance().getContacts().size();
    }

    class ContactHolder extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private TextView userName;

        private View rootView;
        private View profileButton;
        private long userId;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView;
            userImage = rootView.findViewById(R.id.contact_profile_image);
            userName = rootView.findViewById(R.id.contact_profile_name);
            profileButton = rootView.findViewById(R.id.contact_button_profile);
        }

        public void bind(ContactItem item) {

            if (item.getImage() != null) {
                userImage.setImageBitmap(item.getImage());
            }
            userId = item.getUserId();
            userName.setText(item.getName());

            profileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(rootView);
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View itemView);
    }
}
