package android.train.mipt_school;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.Adapters.NewsAdapter;
import android.train.mipt_school.DataHolders.User;
import android.train.mipt_school.Items.NewsItem;
import android.train.mipt_school.Tools.AsyncLoadCallback;
import android.train.mipt_school.Tools.AsyncLoadingFragment;
import android.train.mipt_school.Tools.SceneFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiGroups;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class MainPageFragment extends Fragment implements SceneFragment, AsyncLoadingFragment {

    private String title;
    private RecyclerView newsList;
    private TextView greetingText;

    private static final String NEWS_WEBSITE = "https://vk.com/miptschool";
    private static final String VK_WEBSITE = "https://vk.com";
    private boolean dataLoaded = false;
    AsyncLoadCallback dataLoadCallback;
    ArrayList<NewsItem> newsItems;

    public static MainPageFragment newInstance() {


        MainPageFragment fragment = new MainPageFragment();
        fragment.setRetainInstance(true);
        fragment.loadNews();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        // setting up actionbar
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        newsList = view.findViewById(R.id.news_list);
        greetingText = view.findViewById(R.id.greeting_text_main_page);

        greetingText.setText(String.format("Здравствуйте,\n%s %s!",
                User.getInstance().getFirstName(),
                User.getInstance().getThirdName()));

        NewsAdapter adapter = new NewsAdapter();
        adapter.setData(newsItems);
        adapter.setMainActivity((MainActivity) getActivity());
        newsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsList.setAdapter(adapter);
        Log.d("fragload", "loaded");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        title = context.getString(R.string.main_page_title);
    }

    @Override
    public void onBackButtonPressed() {

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
        this.dataLoadCallback = callback;
    }


    @SuppressLint("StaticFieldLeak")
    public void loadNews() {

        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... strings) {

                try {

                    newsItems = new ArrayList<>();
                    Document newsPage = Jsoup.connect(strings[0]).get();

                    Elements news = newsPage.getElementsByClass("_post_content");

                    for (Element e : news) {

                        //получаем ссылку
                        Element post_link = e.getElementsByClass("post_link").first();
                        String link = VK_WEBSITE + post_link.attr("href");

                        //получаем дату
                        String date = post_link.getElementsByClass("rel_date").first().text();

                        if (!link.contains("reply")) {
                            Element mainContent = e.getElementsByClass("wall_post_text").first();
                            System.out.println(mainContent.toString());
                            String text = textEditor(mainContent.toString());
                            newsItems.add(
                                new NewsItem("Олимпиадные школы МФТИ",text , date, link  ));
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                dataLoaded = true;
                if (dataLoadCallback != null) {
                    dataLoadCallback.onDataLoaded();
                }
            }
        }.execute(NEWS_WEBSITE);
    }

    public String textEditor(String string) {
        String string1 = string.replace("\n", "");
        char[] res = string1.replace("<br>", "\n").toCharArray();
        string = "";
        boolean delete = false;
        for (int i = 0; i < res.length; i++) {
            if (res[i] == '<') {
                delete = true;
            }
            if (!delete) {
                string+=res[i];
            }
            if (res[i] == '>') {
                delete = false;
            }
        }
        String result = string.replace("Показать полностью…","\n");
        return result;
    }

    
}

