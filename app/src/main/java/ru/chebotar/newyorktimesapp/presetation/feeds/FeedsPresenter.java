package ru.chebotar.newyorktimesapp.presetation.feeds;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.chebotar.newyorktimesapp.App;
import ru.chebotar.newyorktimesapp.domain.interactors.NewsScreenInteractor;
import ru.chebotar.newyorktimesapp.presetation.base.MvpBasePresenter;

@InjectViewState
public class FeedsPresenter extends MvpBasePresenter<FeedsView> {

    @Inject
    NewsScreenInteractor newsScreenInteractor;

    public FeedsPresenter() {
        App.INSTANCE.getAppComponent().inject(this);
    }


    public void getFeeds() {
        compositeDisposable.add(
                newsScreenInteractor.getNews("home")
                        .map(newsDTOResult -> newsDTOResult.data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> getViewState().showLoading(true))
                        .subscribe(newsItems -> {
                            getViewState().showData(newsItems);
                            getViewState().showLoading(false);
                        }, t-> getViewState().showLoading(false))
        );
    }
}
