package android.train.mipt_school.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.train.mipt_school.Items.NewsItem;
import android.train.mipt_school.MainActivity;
import android.train.mipt_school.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {


    private ArrayList<NewsItem> data;
    private MainActivity mainActivity;

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.news_item, viewGroup, false);

        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder newsHolder, int i) {
        newsHolder.bind(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<NewsItem> data) {
        this.data = data;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    class NewsHolder extends RecyclerView.ViewHolder {

        private TextView newsTitle;
        private TextView newsDescription;
        private TextView newsDate;
        private View newsLink;

        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDescription = itemView.findViewById(R.id.news_description);
            newsDate = itemView.findViewById(R.id.news_date);
            newsLink = itemView.findViewById(R.id.news_item_link);
        }

        public void bind(final NewsItem item) {
            newsTitle.setText(item.getTitle());
            newsDescription.setText(item.getDescription());
            newsDate.setText(item.getDate());
            newsLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.openWebLink(item.getLink());
                }
            });
        }
    }

}
