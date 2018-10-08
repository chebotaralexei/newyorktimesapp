package ru.chebotar.newyorktimesapp.presetation.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.chebotar.newyorktimesapp.R;

public abstract class BaseFragment extends Fragment implements BackButtonListener {

    protected View rootView;
    View progressBar;
    Toolbar toolbar;

    private Toast toast;
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean shouldNotAnimate = false;

    protected void addSub(Disposable subscription) {
        compositeDisposable.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.shouldNotAnimate = false;
    }

    public BaseFragment() {
    }

    protected abstract int setLayoutRes();

    /**
     * Вызываеться после установки лэйаута
     */
    protected void onPostCreateView() {
        // nothing
    }

    /**
     * Для настройки тулбара
     */
    @CallSuper
    protected void configureToolbar(@NonNull final Toolbar toolbar) {
        if (showBackButton()) {
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        } else {
            toolbar.setNavigationIcon(null);
            toolbar.setOnMenuItemClickListener(null);
        }
    }

    /**
     * Переопределить чтобы проказать кнопку назад в тулбаре
     */
    protected boolean showBackButton() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(setLayoutRes(), container, false);
        progressBar = rootView.findViewById(R.id.progressBar);
        toolbar = rootView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            configureToolbar(toolbar);
        }
        onPostCreateView();
        return rootView;
    }

    protected void showToast(String message) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    protected void showToast(int message) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void showLoading(boolean b) {
        if (progressBar != null)
            progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
    }
}
