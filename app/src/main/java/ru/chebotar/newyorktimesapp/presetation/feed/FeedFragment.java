package ru.chebotar.newyorktimesapp.presetation.feed;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;
import ru.chebotar.newyorktimesapp.presetation.base.MvpBaseFragment;
import ru.chebotar.newyorktimesapp.utils.Utils;

public class FeedFragment extends MvpBaseFragment {
    public TextView title;
    public TextView fullText;
    public TextView time;
    public ImageView image;
    private static final String KEY_FEED = "FEED";
    private NewsDTO feed;

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_feed;
    }

    public static Fragment getNewInstance(Bundle data) {
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public static Bundle getBundle(NewsDTO feed) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(KEY_FEED, feed);
        return arguments;
    }

    @Override
    protected void onPostCreateView() {
        feed = getArguments().getParcelable(KEY_FEED);
        title = rootView.findViewById(R.id.title);
        time = rootView.findViewById(R.id.time);
        image = rootView.findViewById(R.id.image);
        fullText = rootView.findViewById(R.id.full_text);
        title.setText(feed.getTitle());
        fullText.setText(feed.getDescription());
        time.setText(Utils.formatDate(Utils.getDate(feed.getPublishDate())));
        if (!feed.getMultimedia().isEmpty())
            Glide.with(getContext()).load(feed.getMultimedia().get(0).getUrl()).into(image);
    }

    @Override
    protected void configureToolbar(@NonNull Toolbar toolbar) {
        super.configureToolbar(toolbar);
        toolbar.setTitle(feed.getSection());
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
