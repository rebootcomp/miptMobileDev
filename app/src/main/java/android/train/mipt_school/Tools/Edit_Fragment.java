package android.train.mipt_school.Tools;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.train.mipt_school.AllUsersPageFragment;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.MainActivity;
import android.train.mipt_school.ProfilePageFragment;
import android.train.mipt_school.QuestionPageFragment;
import android.train.mipt_school.R;
import android.train.mipt_school.ResponseCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Edit_Fragment extends Fragment implements SceneFragment {

    public static Edit_Fragment newInstance() {
        Edit_Fragment fragment = new Edit_Fragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_view, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Редактирование профиля");

        MaterialButton back = view.findViewById(R.id.back_button);
        Button save = view.findViewById(R.id.save);
        final EditText NewVK = view.findViewById(R.id.vk);
        final EditText Phone = view.findViewById(R.id.phone);

        if (User.getInstance().getVK() != null && !User.getInstance().getVK().equals("null")) {
            NewVK.setText(User.getInstance().getVK());
        }

        if (User.getInstance().getPhoneNumber() != null && !User.getInstance().getPhoneNumber().equals("null")) {
            Phone.setText(User.getInstance().getPhoneNumber());
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResponseCallback responseCallback = new ResponseCallback() {
                    @Override
                    public void onResponse(String data) {
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                };

                User.getInstance().editUserRequest(Phone.getText().toString(), NewVK.getText().toString(), responseCallback);
                User.getInstance().setVK(NewVK.getText().toString());
                User.getInstance().setPhoneNumber(Phone.getText().toString());
                ProfilePageFragment pf = ProfilePageFragment.newInstance();
                pf.loadUser(User.getInstance());
                ((MainActivity) getActivity()).loadFragment(pf);
            }
        });
        return view;
    }


    @Override
    public void onBackButtonPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public String getTitle() {
        return "Редактирование профиля";
    }
}
