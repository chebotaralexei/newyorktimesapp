package ru.chebotar.newyorktimesapp.presetation.feed;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.data.test.model.NewsItem;
import ru.chebotar.newyorktimesapp.presetation.base.BaseFragment;
import ru.chebotar.newyorktimesapp.utils.Utils;

public class FeedFragment extends BaseFragment {
    public TextView title;
    public TextView fullText;
    public TextView time;
    public ImageView image;
    private static final String KEY_FEED = "FEED";
    private NewsItem feed;

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_feed;
    }

    public static Fragment getNewInstance(Bundle data) {
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public static Bundle getBundle(NewsItem feed) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_FEED, feed);
        return arguments;
    }

    @Override
    protected void onPostCreateView() {
        feed = ((NewsItem) getArguments().getSerializable(KEY_FEED));
        title = rootView.findViewById(R.id.title);
        time = rootView.findViewById(R.id.time);
        image = rootView.findViewById(R.id.image);
        fullText = rootView.findViewById(R.id.full_text);
        title.setText(feed.getTitle());
        fullText.setText(feed.getFullText());
        time.setText(Utils.formatDate(feed.getPublishDate()));
        Glide.with(getContext()).load(feed.getImageUrl()).into(image);
    }

    @Override
    protected void configureToolbar(@NonNull Toolbar toolbar) {
        super.configureToolbar(toolbar);
        toolbar.setTitle(feed.getCategory().getName());
    }

    @Override
    protected boolean showBackButton() {
        return true;
    }

    @Override
    public boolean onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }
}
