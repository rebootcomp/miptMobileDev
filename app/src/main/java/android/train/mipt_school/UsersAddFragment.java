package android.train.mipt_school;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.train.mipt_school.Adapters.ContactAdapter;
import android.train.mipt_school.Adapters.SmallContactAdapter;
import android.train.mipt_school.DataHolders.Group;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.Items.GroupItem;
import android.train.mipt_school.Tools.AsyncLoadCallback;
import android.train.mipt_school.Tools.AsyncLoadingFragment;
import android.train.mipt_school.Tools.DataSavingFragment;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class UsersAddFragment extends Fragment implements
        SceneFragment, DataSavingFragment, AsyncLoadingFragment {

    private AsyncLoadCallback loadCallback;
    private boolean dataLoaded = false;

    private static final String BUNDLE_LOAD_TYPE = "LOAD_TYPE";
    private static final String BUNDLE_GROUP_ID = "GROUP_ID";

    public static final int ADD_ADMINS = 1;
    public static final int ADD_USERS = 2;

    private ContactAdapter allUsersAdapter;
    private SmallContactAdapter addedUsersAdapter;
    private RecyclerView allUsersList;
    private RecyclerView addedUsersList;
    private TextView addMembersText;
    private TextView noDataMessage;
    private FloatingActionButton saveDataButton;

    private View clearSearch;
    private EditText searchField;

    private Long groupId;

    private ArrayList<ContactItem> itemsForFind = new ArrayList<>();

    public static UsersAddFragment newInstance(int loadType, Group groupForEditing) {

        UsersAddFragment instance = new UsersAddFragment();
        instance.setRetainInstance(true);
        Bundle args = new Bundle();
        args.putInt(BUNDLE_LOAD_TYPE, loadType);
        args.putLong(BUNDLE_GROUP_ID, groupForEditing.getId());
        instance.setArguments(args);

        if (loadType == ADD_ADMINS) {
            instance.loadUsers(groupForEditing.getAdmins());
        } else if (loadType == ADD_USERS) {
            instance.loadUsers(groupForEditing.getUsers());
        }

        instance.setAddedUsersAdapter(new SmallContactAdapter(new ArrayList<ContactItem>()));
        return instance;
    }

    private ArrayList<ContactItem> excludeItems(ArrayList<ContactItem> allItems,
                                                ArrayList<ContactItem> toExclude) {
        HashSet<ContactItem> excludedItems = new HashSet<>();
        for (ContactItem item : toExclude) {
            excludedItems.add(item);
        }
        ArrayList<ContactItem> result = new ArrayList<>();

        for (ContactItem item : allItems) {
            if (!excludedItems.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }

    private void loadUsers(final ArrayList<ContactItem> exclude) {
        if (User.getInstance().getAllUsers() != null
                && !User.getInstance().getAllUsers().isEmpty()) {
            dataLoaded = true;
            itemsForFind = excludeItems(User.getInstance().getAllUsers(), exclude);
            setAllUsersAdapter(
                    new ContactAdapter(excludeItems(User.getInstance().getAllUsers(), exclude), true));
            if (loadCallback != null) {
                loadCallback.onDataLoaded();
            }
            return;
        }


        ResponseCallback responseCallback = new ResponseCallback() {
            @Override
            public void onResponse(String data) {
                if (User.getInstance().updateAllUsers(data)) {
                    dataLoaded = true;
                    itemsForFind = excludeItems(User.getInstance().getAllUsers(), exclude);
                    setAllUsersAdapter(
                            new ContactAdapter(excludeItems(User.getInstance().getAllUsers(), exclude), true));
                    if (loadCallback != null) {
                        loadCallback.onDataLoaded();
                    }
                } else {
                    Toast.makeText(getContext(),
                            "Что-то пошло не так", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        };
        User.getInstance().allUsersRequest(responseCallback);
    }

    public void setAllUsersAdapter(ContactAdapter allUsersAdapter) {
        this.allUsersAdapter = allUsersAdapter;
    }

    public void setAddedUsersAdapter(SmallContactAdapter addedUsersAdapter) {
        this.addedUsersAdapter = addedUsersAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_add, container, false);
        allUsersList = view.findViewById(R.id.all_users_list);
        addedUsersList = view.findViewById(R.id.added_users_list);
        addMembersText = view.findViewById(R.id.add_members_text);
        noDataMessage = view.findViewById(R.id.no_data_message);
        saveDataButton = view.findViewById(R.id.save_changes_button);

        Bundle bundle = getArguments();
        groupId = bundle.getLong(BUNDLE_GROUP_ID);

        ActionBar toolbar = ((MainActivity) getActivity()).getSupportActionBar();
        // setting up actionbar

        setHasOptionsMenu(true);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowTitleEnabled(false);
        toolbar.setDisplayShowCustomEnabled(true);

        View toolbarLayout = inflater.inflate(R.layout.contact_page_toolbar, null);

        clearSearch = toolbarLayout.findViewById(R.id.clear_search_button);
        searchField = toolbarLayout.findViewById(R.id.search_field);

        toolbar.setCustomView(toolbarLayout);

        searchField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    performSearch(searchField.getText().toString());
                    return true;
                }
                return false;
            }
        });

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                performSearch(s.toString());
                clearSearch.setVisibility(View.VISIBLE);
                if (s.toString().length() == 0) {
                    clearSearch.setVisibility(View.INVISIBLE);
                    allUsersAdapter.setData(itemsForFind);
                    allUsersAdapter.notifyDataSetChanged();
                }
            }
        });

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchField.setText("");
                clearSearch.setVisibility(View.INVISIBLE);
            }
        });

        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        allUsersList.setAdapter(allUsersAdapter);
        allUsersList.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (addedUsersAdapter.getItemCount() > 0) {
            addMembersText.setVisibility(View.GONE);
            if (saveDataButton.getVisibility() != View.VISIBLE) {
                saveDataButton.show();
            }
        } else {
            addMembersText.setVisibility(View.VISIBLE);
            if (saveDataButton.getVisibility() != View.GONE) {
                saveDataButton.hide();
            }
        }

        allUsersAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

            }

            @Override
            public void onItemUnchecked(ContactItem item) {
                int idx = addedUsersAdapter.getData().indexOf(item);
                if (idx != -1) {
                    addedUsersAdapter.getData().remove(idx);
                    addedUsersAdapter.notifyItemRemoved(idx);
                    if (addedUsersAdapter.getItemCount() == 0) {
                        addMembersText.setVisibility(View.VISIBLE);
                        if (saveDataButton.getVisibility() != View.GONE) {
                            saveDataButton.hide();
                        }
                    }
                }
            }

            @Override
            public void onItemChecked(ContactItem item) {
                int idx = addedUsersAdapter.getData().indexOf(item);
                if (idx == -1) {
                    addedUsersAdapter.getData().add(item);
                    addMembersText.setVisibility(View.INVISIBLE);
                    if (saveDataButton.getVisibility() != View.VISIBLE) {
                        saveDataButton.show();
                    }

                    int lastPos = addedUsersAdapter.getItemCount() - 1;

                    ((LinearLayoutManager) addedUsersList.getLayoutManager())
                            .scrollToPositionWithOffset(lastPos, 0);

                    addedUsersAdapter.notifyItemInserted(lastPos);
                }
            }
        });

        addedUsersList.setAdapter(addedUsersAdapter);
        addedUsersList.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public boolean canSwitch() {
        return addedUsersAdapter.getItemCount() == 0;
    }

    @Override
    public boolean onSave() {
        ArrayList<Long> selectedUsers = new ArrayList<>();
        int pos = User.getInstance().getGroupPosById().get(groupId);
        Group group = User.getInstance().getGroups().get(pos);

        for (ContactItem e : allUsersAdapter.getSelectedItems()) {
            selectedUsers.add(e.getUserId());
            if (e.getApprole() == 1) {
                int found = 0;
                for (ContactItem i : group.getAdmins())
                    if (i.getUserId() == e.getUserId()) {
                        found = 1;
                        break;
                    }
                if (found == 0)
                    group.getAdmins().add(e);
            } else {
                int found = 0;
                for (ContactItem i : group.getUsers())
                    if (i.getUserId() == e.getUserId()) {
                        found = 1;
                        break;
                    }
                if (found == 0)
                    group.getUsers().add(e);
            }
        }

        for (GroupItem i : User.getInstance().getAllGroups())
            if (i.getGroupId().equals(groupId)) {
                Integer cnt = group.getAdmins().size() + group.getUsers().size();
                i.setCountOfUsers(cnt.longValue());
                break;
            }

        User.getInstance().addUsersIntoGroupRequest(groupId, selectedUsers, new ResponseCallback() {
            @Override
            public void onResponse(String data) {
                // todo: какая нибудь проверка
            }

            @Override
            public void onFailure(String message) {
                // todo: какая нибудь проверка
            }
        });

        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onBackButtonPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (!canSwitch()) {
            builder.setTitle(Html.fromHtml("<b>Вы точно хотите выйти ?</b>"))
                    .setMessage("Вы не сохранили изменения")
                    .setCancelable(false)
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((MainActivity) getActivity()).hideKeyboard();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    })
                    .setNegativeButton("Нет", null);
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public boolean fragmentDataLoaded() {
        return dataLoaded;
    }

    @Override
    public void setLoadCallback(AsyncLoadCallback callback) {
        this.loadCallback = callback;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackButtonPressed();
                return true;
        }
        return false;
    }

    private void performSearch(final String text) {

        new AsyncTask<String, Void, Void>() {
            ArrayList foundData;

            @Override
            protected Void doInBackground(String... strings) {

                foundData = new ArrayList<>();

                String[] words = text.split(" ");

                for (int i = 0; i < words.length; i++) {
                    words[i] = words[i].toLowerCase();
                }

                for (ContactItem item : itemsForFind) {
                    String[] name = item.getName().split(" ");
                    boolean hasKeyWords = false;

                    for (String i : name) {
                        if (hasKeyWords) {
                            break;
                        }
                        for (String j : words) {
                            if (isSubsequence(i.toLowerCase(), j)) {
                                hasKeyWords = true;
                                break;
                            }
                        }
                    }

                    if (hasKeyWords) {
                        foundData.add(item);
                    }
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                allUsersAdapter.setData(foundData); // интерфейс начало
                allUsersAdapter.notifyDataSetChanged();

                if (foundData.size() == 0) {
                    noDataMessage.setVisibility(View.VISIBLE);
                } else {
                    noDataMessage.setVisibility(View.GONE);
                }
            }
        }.execute();
    }

    boolean isSubsequence(String s, String t) {
        if (t.length() > s.length()) {
            return false;
        }

        int longestPrefix = 0;
        for (int i = 0; i < t.length(); i++) {
            if (t.charAt(i) != s.charAt(i)) {
                break;
            }
            longestPrefix++;
        }
        return longestPrefix == t.length();
    }
}
