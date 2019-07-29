package android.train.mipt_school;

import android.content.Context;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateGroupFragment extends Fragment implements SceneFragment {

    private MaterialButton cancelButton;
    private Button createButton;
    private TextView groupNameField;
    private TextView directionField;

    public static CreateGroupFragment newInstance() {
        CreateGroupFragment fragment = new CreateGroupFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_create_group, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Создание новой группы");

        cancelButton = view.findViewById(R.id.back_button);
        groupNameField = view.findViewById(R.id.name);
        directionField = view.findViewById(R.id.direction);
        createButton = view.findViewById(R.id.create);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonPressed();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = groupNameField.getText().toString();
                String directionIdString = directionField.getText().toString();
                try {
                    Long directionId = Long.parseLong(directionIdString);
                    onAddGroup(groupName, directionId);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Введите числовое значение id", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void onAddGroup(String groupName, Long directionId) {
        User.getInstance().addGroupRequest(groupName, directionId, new ResponseCallback() {
            @Override
            public void onResponse(String data) {
                Long id = User.getInstance().getAddedGroupId(data);
                if (id != 0) {
                    ArrayList<Long> al = new ArrayList<>();
                    al.add(User.getInstance().getUserId());
                    User.getInstance().addUsersIntoGroupRequest(id, al, new ResponseCallback() {
                        @Override
                        public void onResponse(String data) {
                            Toast.makeText(getContext(), "Группа успешно создана", Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackButtonPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public String getTitle() {
        return "Создание новой группы";
    }
}
