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
import java.util.HashSet;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {


    private OnItemClickListener listener;

    private List<ContactItem> data;

    private HashSet<ContactItem> selectedItems;

    private boolean selectOnClick;

    public ContactAdapter(ArrayList<ContactItem> data, boolean selectOnClick) {
        this.selectOnClick = selectOnClick;
        selectedItems = new HashSet<>();
        this.data = data;
    }

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
        private TextView shortName;
        private View checkedCircle;

        private View profileButton;
        private ContactItem item;

        public ContactHolder(@NonNull final View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.contact_profile_image);
            userName = itemView.findViewById(R.id.contact_profile_name);
            profileButton = itemView.findViewById(R.id.contact_button_profile);
            shortName = itemView.findViewById(R.id.short_name_contact_item);
            checkedCircle = itemView.findViewById(R.id.contact_item_checked_circle);


            profileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectOnClick) {
                        ContactItem itemForAdd = data.get(getAdapterPosition());
                        if (selectedItems.contains(itemForAdd)) {
                            selectedItems.remove(itemForAdd);
                            setChecked(false);
                        } else {
                            setChecked(true);
                            selectedItems.add(itemForAdd);
                        }
                    }

                    if (listener != null) {
                        listener.onItemClick(itemView, getAdapterPosition());
                    }
                }
            });
        }

        private void setChecked(boolean checked) {
            if (checked) {
                if (listener != null) {
                    listener.onItemChecked(item);
                }
                checkedCircle.setVisibility(View.VISIBLE);
            } else {
                if (listener != null) {
                    listener.onItemUnchecked(item);
                }
                checkedCircle.setVisibility(View.INVISIBLE);
            }
        }

        public void bind(ContactItem item) {

            this.item = item;
            if (item.getImage() != null) {
                userImage.setImageBitmap(item.getImage());
            }
            if (selectedItems.contains(item)) {
                setChecked(true);
            } else {
                setChecked(false);
            }

            userName.setText(item.getName());
            shortName.setText(item.getFirstName().substring(0, 1) + item.getLastName().substring(0, 1));
        }

    }


    public void setData(List<ContactItem> data) {
        this.data = data;
    }

    public List<ContactItem> getData() {
        return data;
    }

    public HashSet<ContactItem> getSelectedItems() {
        return selectedItems;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);

        void onItemUnchecked(ContactItem item);

        void onItemChecked(ContactItem item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
