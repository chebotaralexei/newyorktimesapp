package ru.chebotar.newyorktimesapp.presetation.feed;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;
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
    private View content;

    private EditText titleEdit;
    private EditText fullTextEdit;
    private TextView timeNoEdit;
    private View contentEdit;

    private ImageView image;
    private SwipeRefreshLayout swl;
    private View progressBar;
    private View placeholder;
    private Toolbar toolbar;
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
        fullText = rootView.findViewById(R.id.full_text);
        content = rootView.findViewById(R.id.content);

        titleEdit = rootView.findViewById(R.id.title_edit);
        timeNoEdit = rootView.findViewById(R.id.time_no_edit);
        fullTextEdit = rootView.findViewById(R.id.full_text_edit);
        contentEdit = rootView.findViewById(R.id.content_edit);

        image = rootView.findViewById(R.id.image);
        toolbar = rootView.findViewById(R.id.toolbar);
        presenter.setFeedId(getArguments().getString(KEY_FEED_ID));
        refresh.setOnClickListener(v -> presenter.refresh(true));
        swl.setOnRefreshListener(() -> presenter.refresh(false));
        setEditMode(false);
    }

    @Override
    public void setEditMode(boolean editMode) {
        toolbar.getMenu().clear();
        if (editMode) {
            toolbar.inflateMenu(R.menu.news_detail_save_menu);
            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.delete) {
                    presenter.onDeleteClick();
                } else {
                    presenter.onSaveClick(titleEdit.getText().toString().trim(),
                            fullTextEdit.getText().toString().trim());
                }
                return true;
            });
        } else {
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
        contentEdit.setVisibility(editMode ? View.VISIBLE : View.GONE);
        content.setVisibility(!editMode ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMessage(String message) {
        showToast(message);
    }

    @Override
    public void goBack() {
        onBackPressed();
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
        toolbar.setTitle(TextUtils.isEmpty(feed.getSection()) ? "No Section" : feed.getSection());
        title.setText(feed.getTitle());
        fullText.setText(feed.getDescription());
        time.setText(Utils.formatDate(Utils.getDate(feed.getPublishDate())));

        titleEdit.setText(feed.getTitle());
        fullTextEdit.setText(feed.getDescription());
        timeNoEdit.setText(Utils.formatDate(Utils.getDate(feed.getPublishDate())));

        if (feed.getBestMultimediaImage() != null) {
            Glide.with(getContext()).load(feed.getBestMultimediaImage().getUrl())
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
