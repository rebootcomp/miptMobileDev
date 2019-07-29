package android.train.mipt_school;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.train.mipt_school.DataHolders.Group;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SendMessageFragment extends Fragment implements SceneFragment {

    private MaterialButton materialButton;
    private Button sendButton;
    private TextView titleField;
    private TextView bodyField;

    private Long groupId;
    private static final String BUNDLE_GROUP_ID = "GROUP_ID";

    public static SendMessageFragment newInstance(Group group) {
        SendMessageFragment instance = new SendMessageFragment();
        instance.setRetainInstance(true);
        Bundle args = new Bundle();
        args.putLong(BUNDLE_GROUP_ID, group.getId());
        instance.setArguments(args);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_send_message, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Отправить сообщение");

        Bundle bundle = getArguments();
        groupId = bundle.getLong(BUNDLE_GROUP_ID);

        materialButton = view.findViewById(R.id.back_button);
        sendButton = view.findViewById(R.id.send);
        titleField = view.findViewById(R.id.title);
        bodyField = view.findViewById(R.id.body);

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonPressed();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleField.getText().toString();
                String body = bodyField.getText().toString();
                onSend(title, body);
            }
        });

        return view;
    }

    public void onSend(String title, String body) {
        User.getInstance().sendMessage(title, body, "topic",
                "group_" + groupId, new ResponseCallback() {
                    @Override
                    public void onResponse(String data) {
                        Toast.makeText(getContext(), "Сообщение успешно отправлено", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onBackButtonPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public String getTitle() {
        return "Отправить сообщение";
    }
}
