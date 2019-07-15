package android.train.mipt_school;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.train.mipt_school.Adapters.ContactAdapter;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.Tools.Edit_Fragment;
import android.train.mipt_school.Tools.SceneFragment;
import android.transition.Fade;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ProfilePageFragment extends Fragment implements SceneFragment {

    private String title;
    private ImageView profileImage;
    private TextView profileName;
    private TextView emailField;
    private TextView phoneField;
    private TextView VKField;
    private TextView userStatus;
    private TextView groupNameField;
    private LinearLayout editButton;
    private CardView editButtonCard;

    private String lastname;
    private String firtsname;
    private String email;
    private String groupName;
    private int vis;
    private String status;
    private long userId;
    private String phoneNumber;
    private String VK;

    private Button addToContacts;

    public String getLastname() {
        return lastname;
    }

    public boolean loadUser(String s, User user) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("data")) {
                JSONObject data = jsonObject.getJSONObject("data");
                String lastname = data.getString("lastname");
                String firstname = data.getString("firstname");
                String email = data.getString("email");
                String approle = data.getString("approle");
                String groupName = "Нет данных";
                String vkId = data.getString("vk_id");
                String phone = data.getString("phone");
                long id = data.getLong("id");
//                JSONArray groups = data.getJSONArray("groups");
//                JSONObject lastGroup = null;
//                for (int i = 0; i < groups.length(); i++)
//                    lastGroup = groups.getJSONObject(i);
//                if (lastGroup != null) {
//                    groupName = lastGroup.getString("group_name");
//                }
                this.setFirtsname(firstname);
                this.setLastname(lastname);
                this.setId(id);
                this.groupName = groupName;
                if (user.getApprole() == 1) {
                    this.setPhone(phone);
                    this.setVK(vkId);
                    this.setEmail(email);
                }
                else {
                    this.setVK("Нет доступа");
                    this.setPhone("Нет доступа");
                    this.setEmail("Нет доступа");
                }
                if (id == user.getUserId())
                    vis = View.INVISIBLE;
                else
                    vis = View.VISIBLE;
                if (approle.equals("admin"))
                    this.status = "Администратор";
                else
                    this.status = "Ученик";
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loadUser(User user) {
        long id = user.getUserId();
        this.setFirtsname(user.getFirstName());
        this.setLastname(user.getLastName());
        this.setId(id);
        this.groupName = "Нет данных";
        vis = View.VISIBLE;
        setEmail(user.getEmail());
        if (user.getApprole() == 1)
            this.status = "Администратор";
        else
            this.status = "Ученик";
        setPhone(user.getPhoneNumber());
        setVK(user.getVK());
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirtsname() {
        return firtsname;
    }

    public void setFirtsname(String firtsname) {
        this.firtsname = firtsname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getUserId() {
        return userId;
    }

    public void setVK(String VK) {
        if (VK == null || VK.equals("null")) {
            this.VK = "Нет данных";
        }
        else {
            this.VK = VK;
        }
    }

    public void setPhone(String phone) {
        if (phone == null || phone.equals("null")) {
            this.phoneNumber = "Нет данных";
        }
        else {
            this.phoneNumber = phone;
        }
    }



    public void setId(long userId) {
        this.userId = userId;
    }

    private View rootView;

    public static ProfilePageFragment newInstance() {
        ProfilePageFragment fragment = new ProfilePageFragment();

        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        // setting up actionbar
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        phoneField = view.findViewById(R.id.telephone_field);
        VKField = view.findViewById(R.id.vk_field);
        profileImage = view.findViewById(R.id.fragment_profile_image);
        profileName = view.findViewById(R.id.fragment_profile_name);
        emailField = view.findViewById(R.id.mail_field);
        userStatus = view.findViewById(R.id.user_status);
        groupNameField = view.findViewById(R.id.group_id_field);
        editButton = view.findViewById(R.id.button_editProfile);
        editButtonCard = view.findViewById(R.id.button_editProfileView);
        if (User.getInstance().getUserId() == userId) {
            editButtonCard.setVisibility(View.VISIBLE);
        }
        else {
            editButtonCard.setVisibility(View.INVISIBLE);
        }
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).loadFragment(Edit_Fragment.newInstance());

            }
        });
        userStatus.setText(status);

        profileName.setText(firtsname + " " + lastname);
        emailField.setText(email);
        phoneField.setText(phoneNumber);
        VKField.setText(VK);
        groupNameField.setText(groupName);

        setHasOptionsMenu(true);

        rootView = view;
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_page_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = context.getString(R.string.profile_page_title);
    }

    @Override
    public void onBackButtonPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public String getTitle() {
        return title;
    }
}