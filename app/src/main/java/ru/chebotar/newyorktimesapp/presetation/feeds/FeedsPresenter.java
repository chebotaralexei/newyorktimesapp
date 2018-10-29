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
    private String section = "home";

    public FeedsPresenter() {
        App.INSTANCE.getAppComponent().inject(this);
    }


    public void getFeeds(boolean progress) {
        compositeDisposable.add(
                newsScreenInteractor.getNews(section)
                        .map(newsDTOResult -> newsDTOResult.data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> getViewState().showLoading(progress))
                        .subscribe(newsItems -> {
                            getViewState().showData(newsItems);
                            getViewState().showLoading(false);
                        }, t -> getViewState().showError())
        );
    }

    public void onMenuItemClick(String section) {
        if (!this.section.equals(section)) {
            this.section = section;
            getFeeds(false);
        }

    }
}
