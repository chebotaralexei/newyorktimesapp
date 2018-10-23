package ru.chebotar.newyorktimesapp.presetation.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpDelegate;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.chebotar.newyorktimesapp.R;

public abstract class MvpBaseFragment extends Fragment implements BackButtonListener {

    private boolean mIsStateSaved;
    private MvpDelegate<? extends MvpBaseFragment> mMvpDelegate;

    protected View rootView;
    private View progressBar;
    private Toolbar toolbar;
    private Toast toast;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMvpDelegate().onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(setLayoutRes(), container, false);
        progressBar = rootView.findViewById(R.id.progressBar);
        toolbar = rootView.findViewById(R.id.toolbar);
        onPostCreateView();
        if (toolbar != null) {
            configureToolbar(toolbar);
        }
        return rootView;
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

    @Override
    public void onStart() {
        super.onStart();

        mIsStateSaved = false;

        getMvpDelegate().onAttach();
    }

    public void onResume() {
        super.onResume();

        mIsStateSaved = false;

        getMvpDelegate().onAttach();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mIsStateSaved = true;

        getMvpDelegate().onSaveInstanceState(outState);
        getMvpDelegate().onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();

        getMvpDelegate().onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getMvpDelegate().onDetach();
        getMvpDelegate().onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.dispose();

        //We leave the screen and respectively all fragments will be destroyed
        if (getActivity().isFinishing()) {
            getMvpDelegate().onDestroy();
            return;
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (mIsStateSaved) {
            mIsStateSaved = false;
            return;
        }

        // See https://github.com/Arello-Mobile/Moxy/issues/24
        boolean anyParentIsRemoving = false;
        Fragment parent = getParentFragment();
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving();
            parent = parent.getParentFragment();
        }

        if (isRemoving() || anyParentIsRemoving) {
            getMvpDelegate().onDestroy();
        }
    }

    /**
     * @return The {@link MvpDelegate} being used by this Fragment.
     */
    public MvpDelegate getMvpDelegate() {
        if (mMvpDelegate == null) {
            mMvpDelegate = new MvpDelegate<>(this);
        }

        return mMvpDelegate;
    }

    protected void addSub(Disposable subscription) {
        compositeDisposable.add(subscription);
    }

}