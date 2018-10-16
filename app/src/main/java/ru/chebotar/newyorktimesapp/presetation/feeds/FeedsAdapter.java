package ru.chebotar.newyorktimesapp.presetation.feeds;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.data.test.model.NewsItem;
import ru.chebotar.newyorktimesapp.utils.Utils;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedViewHolder> {

    private List<NewsItem> feeds;
    private RequestManager requestManager;
    private Consumer<NewsItem> click;

    public FeedsAdapter(List<NewsItem> dataset, RequestManager requestManager, Consumer<NewsItem> click) {
        this.feeds = dataset;
        this.requestManager = requestManager;
        this.click = click;
    }

    @Override
    public int getItemViewType(int position) {
        return feeds.get(position).getCategory().getId();
    }

    @Override
    public FeedsAdapter.FeedViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        return new FeedViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_item, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        holder.bind(feeds.get(position));
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView topic;
        public TextView description;
        public TextView time;
        public ImageView image;
        public CardView cardView;
        private int viewType;
        int[] colors = new int[]{R.color.yellow, R.color.red, R.color.green, R.color.blue};

        public FeedViewHolder(View v, int viewType) {
            super(v);
            title = v.findViewById(R.id.title);
            topic = v.findViewById(R.id.topic);
            description = v.findViewById(R.id.description);
            time = v.findViewById(R.id.time);
            image = v.findViewById(R.id.image);
            cardView = v.findViewById(R.id.card);
            this.viewType = viewType;
        }

        public void bind(NewsItem newsItem) {
            title.setText(newsItem.getTitle());
            topic.setText(newsItem.getCategory().getName());
            description.setText(newsItem.getPreviewText());
            time.setText(Utils.formatDate(newsItem.getPublishDate()));
            requestManager.load(newsItem.getImageUrl()).into(image);
            cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(),colors[viewType-1]));
            cardView.setOnClickListener(v-> {
                try {
                    click.accept(newsItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}