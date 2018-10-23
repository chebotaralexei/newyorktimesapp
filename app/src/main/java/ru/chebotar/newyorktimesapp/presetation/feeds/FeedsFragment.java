package ru.chebotar.newyorktimesapp.presetation.feeds;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;
import ru.chebotar.newyorktimesapp.presetation.base.MvpBaseFragment;
import ru.chebotar.newyorktimesapp.presetation.feed.FeedFragment;

public class FeedsFragment extends MvpBaseFragment implements FeedsView {

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

    @InjectPresenter
    public FeedsPresenter presenter;

    @ProvidePresenter
    FeedsPresenter provideTutorialPresenter() {
        return new FeedsPresenter();
    }

    @Override
    protected void onPostCreateView() {
        recyclerView = rootView.findViewById(R.id.news_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.span_count));
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FeedsAdapter(Glide.with(getContext()), this::navigateToFeed);
        recyclerView.setAdapter(adapter);
        presenter.getFeeds();
    }

    public void showData(List<NewsDTO> data) {
        adapter.setData(data);
    }


    private void navigateToFeed(NewsDTO feed) {
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
