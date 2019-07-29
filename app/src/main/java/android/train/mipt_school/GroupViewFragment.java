package android.train.mipt_school;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.Adapters.ContactAdapter;
import android.train.mipt_school.DataHolders.Group;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class GroupViewFragment extends Fragment implements SceneFragment {

    private String title;
    private Group displayedGroup;
    private RecyclerView adminList;
    private RecyclerView userList;
    private TextView groupName;
    private TextView groupInfo;
    private TextView groupDirection;
    private TextView groupEvent;
    private FloatingActionButton fab;

    private int groupPosition; // позиция группы в списке групп

    public static final String BUNDLE_POS = "POSITION";


    public static GroupViewFragment newInstance(int groupPosition) {

        GroupViewFragment fragment = new GroupViewFragment();

        Bundle args = new Bundle();
        args.putInt(BUNDLE_POS, groupPosition);

        fragment.setArguments(args);

        return fragment;
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group_view, container, false);

        // setting up actionbar
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        adminList = view.findViewById(R.id.group_admin_list);
        userList = view.findViewById(R.id.group_user_list);
        groupInfo = view.findViewById(R.id.fragment_group_info_text);
        groupName = view.findViewById(R.id.group_view_group_name);
        groupDirection = view.findViewById(R.id.group_view_direction);
        groupEvent = view.findViewById(R.id.group_view_event);
        fab = view.findViewById(R.id.fab);

        if (User.getInstance().getApprole() == 1) {
            fab.setVisibility(View.VISIBLE);
        }
        else {
            fab.setVisibility(View.INVISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).loadFragment(SendMessageFragment.newInstance(displayedGroup));
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        groupPosition = args.getInt(BUNDLE_POS);
        displayedGroup = User.getInstance().getGroups().get(groupPosition);


        userList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adminList.setLayoutManager(new LinearLayoutManager(getActivity()));

        final ContactAdapter userAdapter = new ContactAdapter(null, false);
        userAdapter.setData(displayedGroup.getUsers());

        userAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                final ProfilePageFragment pf = ProfilePageFragment.newInstance();
                ResponseCallback responseCallback = new ResponseCallback() {
                    @Override
                    public void onResponse(String data) {
                        if (pf.loadUser(data, User.getInstance())) {
                            ((MainActivity) getActivity()).loadFragment(pf);
                        } else
                            Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                };
                Integer itmid = position;
                ContactItem ci = userAdapter.getData().get(itmid);

                Long selectedUser = ci.getUserId();
                User.getInstance().userInfoRequest(selectedUser, responseCallback);
            }

            @Override
            public void onItemUnchecked(ContactItem item) {

            }

            @Override
            public void onItemChecked(ContactItem item) {

            }
        });


        userList.setAdapter(userAdapter);
        userList.setNestedScrollingEnabled(false);


        final ContactAdapter adminAdapter = new ContactAdapter(null, false);
        adminAdapter.setData(displayedGroup.getAdmins());

        adminAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                final ProfilePageFragment pf = ProfilePageFragment.newInstance();
                ResponseCallback responseCallback = new ResponseCallback() {
                    @Override
                    public void onResponse(String data) {
                        if (pf.loadUser(data, User.getInstance())) {
                            ((MainActivity) getActivity()).loadFragment(pf);
                        } else
                            Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                };
                Integer itmid = position;
                ContactItem ci = adminAdapter.getData().get(itmid);

                Long selectedUser = ci.getUserId();
                User.getInstance().userInfoRequest(selectedUser, responseCallback);
            }

            @Override
            public void onItemUnchecked(ContactItem item) {

            }

            @Override
            public void onItemChecked(ContactItem item) {

            }
        });

        adminList.setAdapter(adminAdapter);
        adminList.setNestedScrollingEnabled(false);

        groupName.setText(displayedGroup.getName());
        groupEvent.setText(displayedGroup.getEvent());
        groupDirection.setText(displayedGroup.getDirection());

        int members = displayedGroup.getUsers().size() + displayedGroup.getAdmins().size();

        if (members % 10 > 1 && members % 10 < 5) {
            groupInfo.setText(Integer.toString(members) + " участника");
        } else if (members % 10 == 1) {
            groupInfo.setText(Integer.toString(members) + " участник");
        } else {
            groupInfo.setText(Integer.toString(members) + " участников");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackButtonPressed();
                return true;
            case R.id.group_edit_button:
                return ((MainActivity) getActivity())
                        .loadFragment(GroupEditFragment.newInstance(groupPosition));
        }
        return false;
    }

    @Override
    public void onBackButtonPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = context.getString(R.string.groupview_page_title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (User.getInstance().getApprole() == 1) {
            inflater.inflate(R.menu.group_view_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }
}