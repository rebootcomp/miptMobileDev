package android.train.mipt_school;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SendMessageFragment extends Fragment implements SceneFragment {

    private MaterialButton materialButton;

    public static SendMessageFragment newInstance() {
        SendMessageFragment fragment = new SendMessageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_send_message, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Отправить сообщение");

        materialButton = view.findViewById(R.id.back_button);

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonPressed();
            }
        });

        return view;
    }


    @Override
    public void onBackButtonPressed() {
        ((MainActivity) getActivity()).loadFragment(MainPageFragment.newInstance());
    }

    @Override
    public String getTitle() {
        return "Отправить сообщение";
    }
}
