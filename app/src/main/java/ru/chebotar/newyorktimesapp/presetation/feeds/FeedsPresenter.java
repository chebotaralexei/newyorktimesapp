package ru.chebotar.newyorktimesapp.presetation.feeds;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.chebotar.newyorktimesapp.App;
import ru.chebotar.newyorktimesapp.data.database.AppDatabase;
import ru.chebotar.newyorktimesapp.data.database.multimedia.DbMultimedia;
import ru.chebotar.newyorktimesapp.data.database.multimedia.DbMultimediaToMultimediaDtoMapping;
import ru.chebotar.newyorktimesapp.data.database.multimedia.MultimediaDtoToDbMultimediaMapping;
import ru.chebotar.newyorktimesapp.data.database.news.DbNews;
import ru.chebotar.newyorktimesapp.data.database.news.DbNewsToNewsDtoMapping;
import ru.chebotar.newyorktimesapp.data.database.news.NewsDtoToDbNewsMapping;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;
import ru.chebotar.newyorktimesapp.domain.interactors.NewsScreenInteractor;
import ru.chebotar.newyorktimesapp.presetation.base.MvpBasePresenter;

@InjectViewState
public class FeedsPresenter extends MvpBasePresenter<FeedsView> {

    @Inject
    NewsScreenInteractor newsScreenInteractor;
    @Inject
    AppDatabase appDatabase;
    @NonNull
    private NewsDtoToDbNewsMapping dtoToDbNewsMapping = new NewsDtoToDbNewsMapping();
    @NonNull
    private MultimediaDtoToDbMultimediaMapping dtoToDbMultimediaMapping = new MultimediaDtoToDbMultimediaMapping();
    @NonNull
    private DbNewsToNewsDtoMapping dbToDtoNewsMapping = new DbNewsToNewsDtoMapping();
    @NonNull
    private DbMultimediaToMultimediaDtoMapping dbToDtoMultimediaMapping = new DbMultimediaToMultimediaDtoMapping();
    @NonNull
    private String section = "home";

    public FeedsPresenter() {
        App.INSTANCE.getAppComponent().inject(this);
    }


    public void getFeeds(boolean progress) {
        compositeDisposable.add(
                newsScreenInteractor.getNews(section)
                        .subscribeOn(Schedulers.io())
                        .map(newsDTOResult -> newsDTOResult.data)
                        .doOnSuccess(this::saveData)
                        .onErrorReturn(throwable -> getData())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> getViewState().showLoading(progress))
                        .subscribe(newsItems -> {
                            getViewState().showData(newsItems);
                            getViewState().showLoading(false);
                        }, t -> getViewState().showData(getData()))
        );
    }

    private void saveData(List<NewsDTO> news) {
        List<DbNews> dbNewsList = new ArrayList<>();
        List<DbMultimedia> dbMultimediaList = new ArrayList<>();
        for (NewsDTO newsDTO : news) {
            DbNews dbNews = dtoToDbNewsMapping.map(newsDTO);
            dbNewsList.add(dbNews);

            DbMultimedia dbMultimedia = dtoToDbMultimediaMapping.map(newsDTO.getMultimediaImage());
            dbMultimedia.setNewsId(dbNews.getId());
            dbMultimediaList.add(dbMultimedia);
        }
        appDatabase.newsDao().deleteAll();
        DbNews[] dbNewsArray = dbNewsList.toArray(new DbNews[dbNewsList.size()]);
        appDatabase.newsDao().insertAll(dbNewsArray);

        appDatabase.multimediaDao().deleteAll();
        DbMultimedia[] dbMultimediaArray = dbMultimediaList.toArray(new DbMultimedia[dbMultimediaList.size()]);
        appDatabase.multimediaDao().insertAll(dbMultimediaArray);
    }

    private List<NewsDTO> getData() {
        List<DbNews> dbNewsList = appDatabase.newsDao().getAll();
        List<NewsDTO> newsList = new ArrayList<>();
        for (DbNews dbNews : dbNewsList) {
            NewsDTO news = dbToDtoNewsMapping.map(dbNews);
            DbMultimedia media = appDatabase.multimediaDao().findByNewsId(dbNews.getId());
            news.setMultimedia(Collections.singletonList(dbToDtoMultimediaMapping.map(media)));
            newsList.add(news);
        }
        return newsList;
    }


    public void onMenuItemClick(String section) {
        if (!this.section.equals(section)) {
            this.section = section;
            getFeeds(false);
        }
    }
}
