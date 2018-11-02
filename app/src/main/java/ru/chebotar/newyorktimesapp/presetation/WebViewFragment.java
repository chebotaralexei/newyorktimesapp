package ru.chebotar.newyorktimesapp.presetation;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
import ru.chebotar.newyorktimesapp.presetation.feeds.FeedsAdapter;
import ru.chebotar.newyorktimesapp.presetation.feeds.FeedsPresenter;
import ru.chebotar.newyorktimesapp.presetation.feeds.FeedsView;

public class WebViewFragment extends MvpBaseFragment {

    private WebView webView;
    private static final String KEY_URL = "URL";

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_web_view;
    }

    public static Fragment getNewInstance(Bundle data) {
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public static Bundle getBundle(String url) {
        Bundle arguments = new Bundle();
        arguments.putString(KEY_URL, url);
        return arguments;
    }


    @Override
    protected void onPostCreateView() {
        webView = rootView.findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(getArguments().getString(KEY_URL));
    }

    @Override
    public boolean onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            return false;
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
