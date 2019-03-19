package android.train.mipt_school;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.train.mipt_school.Tools.FragmentTransitionCallback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;


public class QuestionPageFragment extends Fragment {


    private EditText questionText;
    private Spinner dropdownTopic;

    private FragmentTransitionCallback interfaceCallback;

    public void setInterfaceCallback(FragmentTransitionCallback interfaceCallback) {
        this.interfaceCallback = interfaceCallback;
    }

    public static QuestionPageFragment newInstance(FragmentTransitionCallback callback) {
        QuestionPageFragment fragment = new QuestionPageFragment();
        fragment.setInterfaceCallback(callback);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO:починить баг с переворотом экрана (фрагмент destroyится и сообщение пропадает, и фрагмент тоже)

        View view = inflater.inflate(R.layout.fragment_question_page, container, false);

        setHasOptionsMenu(true);

        dropdownTopic = view.findViewById(R.id.question_dropdown);
        questionText = view.findViewById(R.id.question_text);

        //((MainActivity) getActivity()).getSupportActionBar().(R.layout.question_toolbar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.dropdown_basic_questions, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownTopic.setAdapter(adapter);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.question_page_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
