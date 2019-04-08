package android.train.mipt_school;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.Adapters.ContactAdapter;
import android.train.mipt_school.Adapters.ScheduleAdapter;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class ContactPageFragment extends Fragment implements SceneFragment {

    private String title;
    private RecyclerView contactList;

    public static ContactPageFragment newInstance() {
        ContactPageFragment fragment = new ContactPageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts_page, container, false);
        setHasOptionsMenu(true);

        contactList = view.findViewById(R.id.contacts_view);


        // setting up actionbar
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        ContactAdapter adapter = new ContactAdapter();
        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView) {
                ((MainActivity) getActivity()).loadFragment(ProfilePageFragment
                        .newInstance());
            }
        });
        contactList.setAdapter(adapter);

        contactList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;

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
        title = context.getString(R.string.contacts_page_title);
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
