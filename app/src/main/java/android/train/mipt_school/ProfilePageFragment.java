package android.train.mipt_school;


import android.content.Context;
import android.os.Bundle;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.Tools.SceneFragment;
import android.transition.Fade;
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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfilePageFragment extends Fragment implements SceneFragment {

    private String title;
    private ImageView profileImage;
    private TextView profileName;
    private TextView emailField;
    private TextView userStatus;

    private String lastname;
    private String firtsname;
    private String email;
    private int vis;
    private String status;
    private long userId;

    private Button addToContacts;

    public String getLastname() {
        return lastname;
    }

    public boolean loadUser(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("data")) {
                JSONObject data = jsonObject.getJSONObject("data");
                String lastname = data.getString("lastname");
                String firstname = data.getString("firstname");
                String email = data.getString("email");
                String approle = data.getString("approle");
                long id = data.getLong("id");
                this.setFirtsname(firstname);
                this.setLastname(lastname);
                this.setId(id);
                if (id == User.getInstance().getUserId())
                    vis = View.INVISIBLE;
                else
                    vis = View.VISIBLE;
                this.setEmail(email);
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
        this.setEmail(user.getEmail());
        vis = View.INVISIBLE;
        this.setEmail(email);
        if (user.getApprole() == 1)
            this.status = "Администратор";
        else
            this.status = "Ученик";
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

    public void setId(long userId) {
        this.userId = userId;
    }

    private View rootView;

    public static ProfilePageFragment newInstance() {
        ProfilePageFragment fragment = new ProfilePageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        // setting up actionbar
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        profileImage = view.findViewById(R.id.fragment_profile_image);
        profileName = view.findViewById(R.id.fragment_profile_name);
        emailField = view.findViewById(R.id.mail_field);
        userStatus = view.findViewById(R.id.user_status);
//        addToContacts = view.findViewById(R.id.add_to_contacts);
//
//        addToContacts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                User.getInstance().getFriends().add(new ContactItem(userId, firtsname + " " + lastname));
//            }
//        });
//
//        addToContacts.setVisibility(vis);
        userStatus.setText(status);

        profileName.setText(firtsname + " " + lastname);
        emailField.setText(email);

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
