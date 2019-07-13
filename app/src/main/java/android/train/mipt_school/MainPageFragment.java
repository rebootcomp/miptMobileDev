package android.train.mipt_school;


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
import android.support.design.button.MaterialButton;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class MainPageFragment extends Fragment implements SceneFragment, AsyncLoadingFragment {

    private String title;

    private static final String NEWS_WEBSITE = "https://it-edu.com/ru/news";
    private static final String ITEDU_WEBSITE = "https://it-edu.com";
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


        RecyclerView newsList = view.findViewById(R.id.news_list);
        TextView greetingText = view.findViewById(R.id.greeting_text_main_page);
        MaterialButton vkButton = view.findViewById(R.id.vk_button);
        // setting up bottombar
        ((MainActivity) getActivity())
                .getBottomNavigationBar()
                .setSelectedItemId(R.id.navigation_main);
        ((MainActivity) getActivity())
                .getBottomNavigationBar()
                .getMenu().getItem(0).setChecked(true);

       

        greetingText.setText(String.format("Здравствуйте,\n%s %s!",
                User.getInstance().getFirstName(),
                User.getInstance().getThirdName()));

        vkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openWebLink(NEWS_WEBSITE);
            }
        });

        NewsAdapter adapter = new NewsAdapter();
        adapter.setData(newsItems);
        adapter.setMainActivity((MainActivity) getActivity());

        newsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsList.setAdapter(adapter);


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

    public void loadNews() {

        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... strings) {

                try {

                    newsItems = new ArrayList<>();
                    Document newsPage = Jsoup.connect(strings[0]).get();

                    Elements news = newsPage.getElementsByClass("node--content-container");

                    for (Element e : news) {

                        // Получаем заголовок
                        Element tileElement = e.getElementsByClass("node__title")
                                .first()
                                .select("a")
                                .first();

                        String title = tileElement.select("span").first().text();


                        // Получаем дату
                        Element dateElement = e.getElementsByClass("submitted-date").first();

                        String date = String.format("%s. %s, %s",
                                dateElement.getElementsByClass("month").first().text(),
                                dateElement.getElementsByClass("day").first().text(),
                                dateElement.getElementsByClass("year").first().text());


                        // Получаем ссылку на новость
                        String link = e.select("a").first().attr("href");


                        // Получаем описание новости
                        Element mainContent =
                                e.getElementsByClass("node--main-content").first();

                        String description = mainContent.select("p").text();

                        newsItems.add(
                                new NewsItem(title, description, date, ITEDU_WEBSITE + link));

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
}