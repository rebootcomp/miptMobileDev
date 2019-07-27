package android.train.mipt_school;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.train.mipt_school.Adapters.GroupListAdapter;
import android.train.mipt_school.DataHolders.Group;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.GroupItem;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupListFragment extends Fragment implements SceneFragment {

    private String title;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView groupList;
    private FloatingActionButton fab;

    public static GroupListFragment newInstance() {
        GroupListFragment fragment = new GroupListFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_group_list, container, false);

        swipeRefreshLayout = view.findViewById(R.id.refresh);
        fab = view.findViewById(R.id.fab);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO:: обновление групп!!!!

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if (User.getInstance().getApprole() == 1) {
            fab.setVisibility(View.VISIBLE);
        }
        else {
            fab.setVisibility(View.INVISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) getActivity()).loadFragment(CreateGroupFragment.newInstance());


            }
        });

        // setting up actionbar
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        groupList = view.findViewById(R.id.group_list);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GroupListAdapter adapter = new GroupListAdapter(User.getInstance().getAllGroups());
        adapter.setListener(new GroupListAdapter.onItemClickedListener() {
            @Override
            public void onItemClicked(final GroupItem item) {
                if (User.getInstance().getGroupPosById().containsKey(item.getGroupId())) {
                    int groupPosition = User.getInstance().getGroupPosById().get(item.getGroupId());
                    ((MainActivity) getActivity())
                            .loadFragment(GroupViewFragment.newInstance(groupPosition));
                } else {
                    ((MainActivity) getActivity()).setLoadingScreenState(true);
                    User.getInstance().groupInfoRequest(item.getGroupId(), new ResponseCallback() {
                        @Override
                        public void onResponse(String data) {
                            Group group;
                            User user = User.getInstance();
                            group = user.updateGroupInfo(data);

                            //Toast.makeText(getActivity(), group.getEvent(), Toast.LENGTH_LONG).show();
                            user.getGroups().add(group);
                            int groupPosition = user.getGroups().size() - 1;
                            user.getGroupPosById().put(group.getId(), groupPosition);
                            ((MainActivity) getActivity())
                                    .loadFragment(GroupViewFragment.newInstance(groupPosition));
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(getActivity(),
                                    "Что-то пошло не так", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        groupList.setAdapter(adapter);
        groupList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = context.getString(R.string.groups_page_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
    }

    @Override
    public void onBackButtonPressed() {
        ((MainActivity) getActivity()).loadFragment(MainPageFragment.newInstance());
    }

    @Override
    public String getTitle() {
        return title;
    }
}
