package android.train.mipt_school;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.Adapters.ScheduleAdapter;
import android.train.mipt_school.Items.ScheduleItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {


    private static ArrayList<ScheduleItem> data;
    private RecyclerView list;


    public static void setData(ArrayList<ScheduleItem> data) {
        ScheduleFragment.data = data;
    }

    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
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
        list = view.findViewById(R.id.schedule_view);


        ScheduleAdapter adapter = new ScheduleAdapter();

        adapter.setData(data);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;
    }


}
