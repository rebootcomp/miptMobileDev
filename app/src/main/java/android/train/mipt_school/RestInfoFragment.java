package android.train.mipt_school;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.train.mipt_school.Tools.FragmentTransitionCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RestInfoFragment extends Fragment {


    private FragmentTransitionCallback interfaceCallback;

    public static RestInfoFragment newInstance(FragmentTransitionCallback callback) {
        RestInfoFragment fragment = new RestInfoFragment();

        fragment.setInterfaceCallback(callback);
        return fragment;
    }

    private View questionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rest_info, container, false);

        questionButton = view.findViewById(R.id.button_question);

        questionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceCallback.onTransition(QuestionPageFragment.newInstance(interfaceCallback));
            }
        });

        return view;
    }

    public void setInterfaceCallback(FragmentTransitionCallback interfaceCallback) {
        this.interfaceCallback = interfaceCallback;
    }

}
