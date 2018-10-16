package ru.chebotar.newyorktimesapp.presetation.feeds;

import android.os.Bundle;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.data.test.model.DataUtils;
import ru.chebotar.newyorktimesapp.data.test.model.NewsItem;
import ru.chebotar.newyorktimesapp.presetation.base.BaseFragment;
import ru.chebotar.newyorktimesapp.presetation.feed.FeedFragment;

public class FeedsFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private FeedsAdapter adapter;

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_feeds;
    }

    public static Fragment getNewInstance(Bundle data) {
        FeedsFragment fragment = new FeedsFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public static Bundle getBundle() {
        Bundle arguments = new Bundle();
        return arguments;
    }

    @Override
    protected void onPostCreateView() {
        recyclerView = rootView.findViewById(R.id.news_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.span_count));
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FeedsAdapter(DataUtils.generateNews(), Glide.with(getContext()), this::navigateToFeed);
        recyclerView.setAdapter(adapter);
    }

    private void navigateToFeed(NewsItem feed) {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, FeedFragment.getNewInstance(FeedFragment.getBundle(feed)))
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void configureToolbar(@NonNull Toolbar toolbar) {
        super.configureToolbar(toolbar);
        toolbar.setTitle(R.string.app_name);
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
