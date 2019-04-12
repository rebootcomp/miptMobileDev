package android.train.mipt_school;


import android.content.Context;
import android.os.Bundle;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuestionPageFragment extends Fragment implements SceneFragment {


    private EditText questionText;
    private Spinner dropdownTopic;
    private String title;

    public interface ResponseCallback {
        void call(String s);
    }

    private void sendRequest(long userId, String fio, String fromEmail, String toEmail, String subject, String body, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .sendQuestion(userId, fio, fromEmail, toEmail, subject, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                if (response.code() == 200 || response.code() == 201) {
                    s = "Отправлено успешно";
                } else
                    s = "Ошибка сервера";
                rc.call(s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = "Проверьте соединение с интернетом";
                rc.call(s);
            }
        });
    }

    public static QuestionPageFragment newInstance() {
        QuestionPageFragment fragment = new QuestionPageFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // setting up actionbar
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        View view = inflater.inflate(R.layout.fragment_question_page, container, false);

        dropdownTopic = view.findViewById(R.id.question_dropdown);
        questionText = view.findViewById(R.id.question_text);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            case R.id.send_message:
                title = dropdownTopic.getSelectedItem().toString();
                ResponseCallback rc = new ResponseCallback() {
                    @Override
                    public void call(String s) {
                        if (s.equals("Отправлено успешно"))
                            questionText.setText("");
                        Toast.makeText((MainActivity)getActivity(), s, Toast.LENGTH_LONG).show();
                    }
                };
                User user = User.getInstance();
                sendRequest(user.getUserId(), user.getLastName() + " " + user.getFirstName() + " " + user.getLastName(), user.getEmail(), "technical-support@it-edu.com", title, questionText.getText().toString(), rc);
                return true;
        }
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = context.getString(R.string.question_page_title);
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
