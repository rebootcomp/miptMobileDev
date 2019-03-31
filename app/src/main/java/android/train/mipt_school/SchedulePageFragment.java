package android.train.mipt_school;


import android.content.Context;
import android.os.Bundle;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.Adapters.ScheduleAdapter;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SchedulePageFragment extends Fragment implements SceneFragment {


    private RecyclerView scheduleList;
    private String title;


    public static SchedulePageFragment newInstance() {
        SchedulePageFragment fragment = new SchedulePageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        scheduleList = view.findViewById(R.id.schedule_view);


        scheduleList.setAdapter(new ScheduleAdapter());
        scheduleList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = context.getString(R.string.schedule_page_title);
    }

    @Override
    public void onBackButtonPressed() {
        ((MainActivity) getActivity()).loadFragment(MainPageFragment.newInstance(), true);
    }

    @Override
    public String getTitle() {
        return title;
    }
}
