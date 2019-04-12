package android.train.mipt_school;


import android.content.Context;
import android.os.Bundle;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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
import android.widget.ImageView;
import android.widget.TextView;


public class ProfilePageFragment extends Fragment implements SceneFragment {

    private String title;
    private ImageView profileImage;
    private TextView profileName;


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
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        profileImage = view.findViewById(R.id.fragment_profile_image);
        profileName = view.findViewById(R.id.fragment_profile_name);

        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
