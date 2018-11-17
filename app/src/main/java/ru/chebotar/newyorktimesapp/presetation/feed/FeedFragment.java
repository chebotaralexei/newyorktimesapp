package ru.chebotar.newyorktimesapp.presetation.feed;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;
import ru.chebotar.newyorktimesapp.presetation.base.MvpBaseFragment;
import ru.chebotar.newyorktimesapp.utils.Utils;

public class FeedFragment extends MvpBaseFragment implements FeedView {
    private TextView title;
    private TextView fullText;
    private TextView time;
    private ImageView image;
    private SwipeRefreshLayout swl;
    private View progressBar;
    private View placeholder;
    private Button refresh;
    private static final String KEY_FEED_ID = "FEED_ID";


    @InjectPresenter
    public FeedPresenter presenter;

    @ProvidePresenter
    FeedPresenter provideTutorialPresenter() {
        return new FeedPresenter();
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_feed;
    }

    public static Fragment getNewInstance(Bundle data) {
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public static Bundle getBundle(String id) {
        Bundle arguments = new Bundle();
        arguments.putString(KEY_FEED_ID, id);
        return arguments;
    }

    @Override
    protected void onPostCreateView() {
        progressBar = rootView.findViewById(R.id.progressBar);
        refresh = rootView.findViewById(R.id.refresh);
        placeholder = rootView.findViewById(R.id.placeholder);
        swl = rootView.findViewById(R.id.swl);
        title = rootView.findViewById(R.id.title);
        time = rootView.findViewById(R.id.time);
        image = rootView.findViewById(R.id.image);
        fullText = rootView.findViewById(R.id.full_text);
        presenter.setFeedId(getArguments().getString(KEY_FEED_ID));
        refresh.setOnClickListener(v -> presenter.refresh(true));
    }

    @Override
    protected void configureToolbar(@NonNull Toolbar toolbar) {
        super.configureToolbar(toolbar);
        toolbar.inflateMenu(R.menu.news_detail_edit_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.delete) {
                presenter.onDeleteClick();
            } else {
                presenter.onEditClick();
            }
            return true;
        });
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

    @Override
    public void showData(NewsDTO feed) {
        if (getToolbar() != null) {
            getToolbar().setTitle(TextUtils.isEmpty(feed.getSection()) ? "No Section" : feed.getSection());
        }
        title.setText(feed.getTitle());
        fullText.setText("" + feed.getDescription()
                + "\n\nOne more time\n\n" + feed.getDescription()
                + "\n\nAnd Again\n\n" + feed.getDescription());
        time.setText(Utils.formatDate(Utils.getDate(feed.getPublishDate())));
        if (feed.getBestMultimediaImage() != null)
            Glide.with(getContext()).load(feed.getBestMultimediaImage().getUrl()).into(image);
        showLoading(false);
    }

    @Override
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
