package android.train.mipt_school;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.train.mipt_school.Tools.AsyncLoadCallback;
import android.train.mipt_school.Tools.AsyncLoadingFragment;
import android.train.mipt_school.Tools.DataSavingFragment;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class UsersDeleteFragment extends Fragment implements SceneFragment {

    private static final String BUNDLE_LOAD_TYPE = "LOAD_TYPE";

    public static final int DELETE_ADMINS = 1;
    public static final int DELETE_USERS = 2;

    private int loadType;

    private Group groupForEditing;

    private ContactAdapter allUsersAdapter;
    private RecyclerView allUsersList;

    private TextView noDataMessage;

    private View clearSearch;
    private EditText searchField;

    private ArrayList<ContactItem> itemsForFind = new ArrayList<>();

    public static UsersDeleteFragment newInstance(int loadType, Group groupForEditing) {

        UsersDeleteFragment instance = new UsersDeleteFragment();
        instance.setRetainInstance(true);

        Bundle args = new Bundle();
        args.putInt(BUNDLE_LOAD_TYPE, loadType);
        instance.setArguments(args);


        if (loadType == DELETE_ADMINS) {
            instance.setItemsForFind(groupForEditing.getAdmins());
        } else if (loadType == DELETE_USERS) {
            instance.setItemsForFind(groupForEditing.getUsers());
        }

        ContactAdapter adapter = new ContactAdapter(instance.getItemsForFind(), false);
        instance.setAllUsersAdapter(adapter);
        instance.setGroupForEditing(groupForEditing);

        return instance;
    }

    public void setAllUsersAdapter(ContactAdapter allUsersAdapter) {
        this.allUsersAdapter = allUsersAdapter;
    }

    public void setItemsForFind(ArrayList<ContactItem> itemsForFind) {
        this.itemsForFind = itemsForFind;
    }

    public ArrayList<ContactItem> getItemsForFind() {
        return itemsForFind;
    }


    public void setGroupForEditing(Group groupForEditing) {
        this.groupForEditing = groupForEditing;
    }

    public Group getGroupForEditing() {
        return groupForEditing;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_delete, container, false);
        allUsersList = view.findViewById(R.id.all_users_list);
        noDataMessage = view.findViewById(R.id.no_data_message);

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        allUsersList.setAdapter(allUsersAdapter);
        allUsersList.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadType = getArguments().getInt(BUNDLE_LOAD_TYPE);

        allUsersAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, final int position) {
                final ContactItem item = allUsersAdapter.getData().get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                String messageText =
                        String.format("Вы точно хотите удалить пользователя %s из %s группы?",
                                Html.fromHtml("<b>" + item.getName() + "</b>").toString(),
                                Html.fromHtml(loadType == DELETE_ADMINS ? "<b>администраторов</b>"
                                        : "<b>пользователей</b>").toString());

                builder.setTitle(Html.fromHtml("<b>Подтвердите действие</b>"))
                        .setMessage(messageText)
                        .setCancelable(false)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // todo delete делать сдесь
                                if (loadType == DELETE_ADMINS) {
                                    groupForEditing.getAdmins().remove(position);
                                    allUsersAdapter.notifyItemRemoved(position);
                                } else if (loadType == DELETE_USERS) {
                                    groupForEditing.getUsers().remove(position);
                                    allUsersAdapter.notifyItemRemoved(position);
                                }
                            }
                        })
                        .setNegativeButton("Нет", null);
                builder.show();
            }

            @Override
            public void onItemUnchecked(ContactItem item) {

            }

            @Override
            public void onItemChecked(ContactItem item) {

            }
        });
    }

    @Override
    public void onBackButtonPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public String getTitle() {
        return "";
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
