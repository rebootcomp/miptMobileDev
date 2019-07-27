package android.train.mipt_school;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CreateGroupFragment extends Fragment implements SceneFragment {

    public static CreateGroupFragment newInstance() {
        CreateGroupFragment fragment = new CreateGroupFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_create_group, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Создание новой группы");

        return view;
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
