package android.train.mipt_school;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.train.mipt_school.Adapters.ContactAdapter;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.Tools.AsyncLoadCallback;
import android.train.mipt_school.Tools.AsyncLoadingFragment;
import android.train.mipt_school.Tools.SceneFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class AllUsersPageFragment extends Fragment implements SceneFragment, AsyncLoadingFragment {

    private String title;
    private RecyclerView contactList;
    private View clearSearch;
    private EditText searchField;
    private ProgressBar progressBar;
    private ContactAdapter contactAdapter;
    private TextView noDataMessage;
    private ProfilePageFragment pf;
    ArrayList<ContactItem> foundData;

    private boolean dataLoaded = false;
    private AsyncLoadCallback loadCallback;
    public ResponseCallback responseCallback;

    public static AllUsersPageFragment newInstance() {
        AllUsersPageFragment fragment = new AllUsersPageFragment();
        fragment.loadUsers();

        return fragment;
    }

    private void loadUsers() {
        if (User.getInstance().getAllUsers() != null
                && !User.getInstance().getAllUsers().isEmpty()) {
            dataLoaded = true;
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

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts_page, container, false);
        setHasOptionsMenu(true);

        contactList = view.findViewById(R.id.contacts_view);
        progressBar = view.findViewById(R.id.progressbar);
        noDataMessage = view.findViewById(R.id.no_data_found_message);
        foundData = User.getInstance().getAllUsers();


        /*DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.gray_divider));
        contactList.addItemDecoration(itemDecorator);*/


        ActionBar toolbar = ((MainActivity) getActivity()).getSupportActionBar();
        // setting up actionbar


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
                    contactAdapter.setData(User.getInstance().getAllUsers());
                    foundData = User.getInstance().getAllUsers();
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
        contactAdapter = new ContactAdapter();

        contactList.setLayoutManager(new LinearLayoutManager(this.getContext()));



                contactAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        pf = ProfilePageFragment.newInstance();
                        responseCallback = new ResponseCallback() {
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
                        ContactItem ci = foundData.get(itmid);

                        Long selectedUser = ci.getUserId();
                        User.getInstance().userInfoRequest(selectedUser, responseCallback);
                    }
                });

                contactAdapter.setData(User.getInstance().getAllUsers());

                contactList.setAdapter(contactAdapter);

                contactList.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.contact_page_menu, menu);
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

    @Override
    public boolean fragmentDataLoaded() {
        return dataLoaded;
    }

    @Override
    public void setLoadCallback(AsyncLoadCallback callback) {
        loadCallback = callback;
    }
  
    @SuppressLint("StaticFieldLeak")
    private void performSearch(final String text) {

        contactList.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... strings) {

                foundData = new ArrayList<>();

                String[] words = text.split(" ");

                for (int i = 0; i < words.length; i++) {
                    words[i] = words[i].toLowerCase();
                }

                for (ContactItem item : User.getInstance().getAllUsers()) {
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
                contactAdapter.setData(foundData); // интерфейс начало
                contactAdapter.notifyDataSetChanged();

                if (foundData.size() == 0) {
                    noDataMessage.setVisibility(View.VISIBLE);
                } else {
                    noDataMessage.setVisibility(View.INVISIBLE);
                }

                contactList.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

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