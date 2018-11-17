package ru.chebotar.newyorktimesapp.presetation.feeds;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;
import ru.chebotar.newyorktimesapp.presetation.base.MvpBaseFragment;
import ru.chebotar.newyorktimesapp.presetation.feed.FeedFragment;

public class FeedsFragment extends MvpBaseFragment implements FeedsView {

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private FeedsAdapter adapter;
    private SwipeRefreshLayout swl;
    private View progressBar;
    private View placeholder;
    private Button refresh;

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
        progressBar = rootView.findViewById(R.id.progressBar);
        refresh = rootView.findViewById(R.id.refresh);
        placeholder = rootView.findViewById(R.id.placeholder);
        recyclerView = rootView.findViewById(R.id.news_list);
        swl = rootView.findViewById(R.id.swl);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.span_count));
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FeedsAdapter(Glide.with(getContext()), this::navigateToFeed);
        recyclerView.setAdapter(adapter);
        presenter.observeFeeds();
        swl.setOnRefreshListener(() -> presenter.refresh(false));
        refresh.setOnClickListener(v -> presenter.refresh(true));
    }

    public void showData(List<NewsDTO> data) {
        adapter.setData(data);
        showLoading(false);
    }


    private void navigateToFeed(NewsDTO feed) {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, FeedFragment.getNewInstance(FeedFragment.getBundle(feed.getId())))
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void configureToolbar(@NonNull Toolbar toolbar) {
        super.configureToolbar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.news_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            presenter.onMenuItemClick(item.getTitle().toString().toLowerCase());
            return true;
        });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public void showLoading(boolean b) {
        progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
        swl.setRefreshing(b);
        placeholder.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        progressBar.setVisibility(View.GONE);
        placeholder.setVisibility(View.VISIBLE);
        swl.setRefreshing(false);
    }
}
