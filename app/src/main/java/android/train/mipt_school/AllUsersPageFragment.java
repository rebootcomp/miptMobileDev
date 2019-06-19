package android.train.mipt_school;


import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AllUsersPageFragment extends Fragment implements SceneFragment {

    private String title;
    private RecyclerView contactList;
    private View clearSearch;
    private EditText searchField;
    private Thread searchThread;
    private ProgressBar progressBar;
    private ContactAdapter contactAdapter;
    private TextView noDataMessage;
    ArrayList<ContactItem> foundData;

    public interface ResponseCallback {
        void call(String s);
    }

    public ResponseCallback responseCallback;

    public static AllUsersPageFragment newInstance() {
        AllUsersPageFragment fragment = new AllUsersPageFragment();
        return fragment;
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                contactAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        responseCallback = new ResponseCallback() {
                            @Override
                            public void call(String s) {
                                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    if (jsonObject.has("bad_request"))
                                        Toast.makeText(getContext(), jsonObject.getString("bad_request"), Toast.LENGTH_LONG).show();
                                    else {
                                        JSONObject data = jsonObject.getJSONObject("data");
                                        String lastname = data.getString("lastname");
                                        String firstname = data.getString("firstname");
                                        String email = data.getString("email");
                                        long id = data.getLong("id");
                                        ProfilePageFragment pf = ProfilePageFragment.newInstance();
                                        pf.setFirtsname(firstname);
                                        pf.setLastname(lastname);
                                        pf.setId(id);
                                        pf.setEmail(email);
                                        ((MainActivity) getActivity()).loadFragment(pf);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        Integer itmid = position;
                        ContactItem ci = foundData.get(itmid);
                        Long userid = ci.getUserId();
                        Toast.makeText(getContext(), userid.toString() + " " + itmid.toString(), Toast.LENGTH_LONG).show();
                        User.getInstance().userInfoRequest(ci.getUserId(), responseCallback);
                    }
                });

                contactAdapter.setData(User.getInstance().getAllUsers());

                contactList.setAdapter(contactAdapter);

                contactList.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).run(); // кажется, так быстрее будет

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

    private void performSearch(final String text) {
        if (searchThread != null) {
            searchThread.interrupt();
        }

        searchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                contactList.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

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


                contactAdapter.setData(foundData);
                contactAdapter.notifyDataSetChanged();

                if (foundData.size() == 0) {
                    noDataMessage.setVisibility(View.VISIBLE);
                } else {
                    noDataMessage.setVisibility(View.INVISIBLE);
                }

                contactList.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);


            }
        });

        searchThread.run();
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