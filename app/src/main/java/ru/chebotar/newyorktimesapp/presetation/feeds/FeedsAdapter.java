package ru.chebotar.newyorktimesapp.presetation.feeds;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;
import ru.chebotar.newyorktimesapp.utils.Utils;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedViewHolder> {

    private List<NewsDTO> feeds = new ArrayList<>();
    private RequestManager requestManager;
    private Consumer<NewsDTO> click;

    public FeedsAdapter(RequestManager requestManager, Consumer<NewsDTO> click) {
        this.requestManager = requestManager;
        this.click = click;
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

    public void setData(@NonNull final List<NewsDTO> newsItems) {
        feeds.clear();
        feeds.addAll(newsItems);
        notifyDataSetChanged();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView topic;
        public TextView description;
        public TextView time;
        public ImageView image;
        public CardView cardView;
        private int viewType;

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

        public void bind(NewsDTO newsItem) {
            title.setText(newsItem.getTitle());
            topic.setText(newsItem.getSection());
            topic.setVisibility(TextUtils.isEmpty(newsItem.getSection()) ? View.GONE : View.VISIBLE);
            description.setText(newsItem.getDescription());
            time.setText(Utils.formatDate(Utils.getDate(newsItem.getPublishDate())));
            if (!newsItem.getMultimedia().isEmpty()) {
                requestManager.load(newsItem.getMultimedia().get(0).getUrl())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e,
                                                        Object model,
                                                        Target<Drawable> target,
                                                        boolean isFirstResource) {
                                image.setImageResource(R.drawable.ph);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource,
                                                           Object model,
                                                           Target<Drawable> target,
                                                           DataSource dataSource,
                                                           boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(image);
            } else {
                image.setImageResource(R.drawable.ph);
            }
            cardView.setOnClickListener(v -> {
                try {
                    click.accept(newsItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}