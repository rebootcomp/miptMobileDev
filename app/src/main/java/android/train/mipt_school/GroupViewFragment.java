package android.train.mipt_school;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.Adapters.ContactAdapter;
import android.train.mipt_school.DataHolders.Group;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.LayoutInflater;
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

    public static final String BUNDLE_POS = "POSITION";


    public static GroupViewFragment newInstance(int groupPosition) {

        GroupViewFragment fragment = new GroupViewFragment();

        Bundle args = new Bundle();
        args.putInt(BUNDLE_POS, groupPosition);

        fragment.setArguments(args);

        return fragment;
    }

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

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        final int position = args.getInt(BUNDLE_POS);
        displayedGroup = User.getInstance().getGroups().get(position);


        userList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adminList.setLayoutManager(new LinearLayoutManager(getActivity()));

        final ContactAdapter userAdapter = new ContactAdapter();
        userAdapter.setData(displayedGroup.getUsers());

        userAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                final ProfilePageFragment pf = ProfilePageFragment.newInstance();
                ResponseCallback responseCallback = new ResponseCallback() {
                    @Override
                    public void onResponse(String data) {
                        if (pf.loadUser(data)) {
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
        });


        userList.setAdapter(userAdapter);


        final ContactAdapter adminAdapter = new ContactAdapter();
        adminAdapter.setData(displayedGroup.getAdmins());

        adminAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                final ProfilePageFragment pf = ProfilePageFragment.newInstance();
                ResponseCallback responseCallback = new ResponseCallback() {
                    @Override
                    public void onResponse(String data) {
                        if (pf.loadUser(data)) {
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
        });

        adminList.setAdapter(adminAdapter);

        groupName.setText(displayedGroup.name);

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
        if (item.getItemId() == android.R.id.home) {
            onBackButtonPressed();
            return true;
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
}