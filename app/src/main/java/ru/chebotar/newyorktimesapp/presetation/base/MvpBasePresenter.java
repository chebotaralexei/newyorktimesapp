package ru.chebotar.newyorktimesapp.presetation.base;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class MvpBasePresenter<T extends MvpView> extends MvpPresenter<T> {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected Disposable disposable;

    public MvpBasePresenter() {

    }

    protected void addSub(Disposable subscription) {
        compositeDisposable.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }

}
