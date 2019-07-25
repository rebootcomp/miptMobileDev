package android.train.mipt_school;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class MainPageFragment extends Fragment implements SceneFragment, AsyncLoadingFragment {

    private String title;

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

        MaterialButton vkButton = view.findViewById(R.id.vk_button);
        RecyclerView newsList = view.findViewById(R.id.news_list);
        TextView greetingText = view.findViewById(R.id.greeting_text_main_page);

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
        Log.d("fragload", "loaded");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("smth", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        Log.d("result_token", token);
                    }
                });

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

                        //получает ещё и комментарии, поэтому проверка
                        if (!link.contains("reply")) {
                            Element mainContent = e.getElementsByClass("wall_post_text").first();
                            System.out.println(mainContent.toString());
                            String text = translateFromHtml(mainContent.toString());
                            newsItems.add(new NewsItem(text, date, link));
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

    private String translateFromHtml(String s) {
        // <img class="emoji" src="/emoji/e/f09f8f86.png" alt="&#x1f3c6;">
        s = s.replaceAll("<img class=\"emoji\"[^<>]*alt=\"([^<>]*)\">", "$1");
        s = s.replaceAll("<a class=\"wall_post_more\".*</a>", "");

        return Html.fromHtml(s).toString();
    }


}