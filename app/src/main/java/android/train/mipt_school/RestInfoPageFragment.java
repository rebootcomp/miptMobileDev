package android.train.mipt_school;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RestInfoPageFragment extends Fragment implements SceneFragment {

    private View questionButton;
    private View profileButton;
    private View contactsButton;
    private View mapsButton;
    private View cultButton;
    private View logOutButton;

    private ImageView profileImage;
    private TextView profileName;
    SharedPreferences mSP;
    SharedPreferences msp;

    private ResponseCallback responseCallback;

    private String title;

    public static RestInfoPageFragment newInstance() {
        RestInfoPageFragment fragment = new RestInfoPageFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_rest_info, container, false);


        // setting up actionbar
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        questionButton = view.findViewById(R.id.button_question);
        profileButton = view.findViewById(R.id.button_profile);
        contactsButton = view.findViewById(R.id.button_contacts);
        mapsButton = view.findViewById(R.id.button_maps);
        cultButton = view.findViewById(R.id.button_cultural_events);
        logOutButton = view.findViewById(R.id.button_logout);


        profileImage = view.findViewById(R.id.profile_image);
        profileName = view.findViewById(R.id.profile_name);

        profileName.setText(User.getInstance().getFirstName() + " " + User.getInstance().getLastName());

        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(QuestionPageFragment.newInstance());
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilePageFragment pf = ProfilePageFragment.newInstance();
                pf.loadUser(User.getInstance());
                ((MainActivity) getActivity()).loadFragment(pf);
            }
        });

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(AllUsersPageFragment.newInstance());
            }
        });

        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                test();
                ((MainActivity) getActivity()).loadFragment(MapsPageFragment.newInstance());
            }
        });

        cultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(CulturalPageFragment.newInstance());
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(Html.fromHtml("</b>Подтвердите действие</b>").toString())
                        .setMessage("Вы точно хотите выйти?")
                        .setCancelable(false)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                responseCallback = new ResponseCallback() {
                                    @Override
                                    public void onResponse(String data) {
                                        mSP = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor ed  = mSP.edit();
                                        ed.putString("signed", "");
                                        ed.putString("login", "");
                                        ed.putString("pass", "");
                                        ed.putString("token", "");
                                        ed.commit();
                                        startActivity(new Intent(getContext(), LoginActivity.class));
                                        getActivity().finish();
                                        try {
                                            JSONObject jsonObject = new JSONObject(data);
                                            if (jsonObject.has("success"))
                                                Toast.makeText(getContext(), jsonObject.getString("success"), Toast.LENGTH_LONG).show();
                                            else
                                                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                        } catch (JSONException e) {
                                            Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                                        }
                                        Toast.makeText(getActivity(), data, Toast.LENGTH_LONG).show();
                                        User.getInstance().setNullInstance();
                                    }

                                    @Override
                                    public void onFailure(String message) {
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                    }
                                };

                                User.getInstance().logOut(responseCallback);


                            }
                        })
                        .setNegativeButton("Нет", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = context.getString(R.string.rest_info_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
    }

    @Override
    public void onBackButtonPressed() {
        ((MainActivity) getActivity()).loadFragment(MainPageFragment.newInstance());
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void test() {
        responseCallback = new ResponseCallback() {
            @Override
            public void onResponse(String data) {
                ((MainActivity) getActivity()).loadFragment(MapsPageFragment.newInstance());
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        };

        User.getInstance().sendMessage("Проверка", "Рассылка по группам", "topic",
                "group_" + User.getInstance().groupID() , responseCallback);
    }
}
