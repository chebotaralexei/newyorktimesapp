package ru.chebotar.newyorktimesapp.presetation.feeds;

import com.arellomobile.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.chebotar.newyorktimesapp.App;
import ru.chebotar.newyorktimesapp.data.database.AppDatabase;
import ru.chebotar.newyorktimesapp.data.database.ListMapping;
import ru.chebotar.newyorktimesapp.data.database.multimedia.DbMultimedia;
import ru.chebotar.newyorktimesapp.data.database.multimedia.DbMultimediaToMultimediaDtoMapping;
import ru.chebotar.newyorktimesapp.data.database.multimedia.MultimediaDtoToDbMultimediaMapping;
import ru.chebotar.newyorktimesapp.data.database.news.DbNews;
import ru.chebotar.newyorktimesapp.data.database.news.DbNewsToNewsDtoMapping;
import ru.chebotar.newyorktimesapp.data.database.news.NewsDtoToDbNewsMapping;
import ru.chebotar.newyorktimesapp.data.database.news.NewsWithMultimedia;
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
                        .subscribe(this::saveData, t -> getViewState().showError())
        );
    }
    public void observeFeeds() {
        compositeDisposable.add(observeData()
                .subscribe(newsDTOS -> {
                    getViewState().showData(newsDTOS);
                    getViewState().showLoading(false);
                }, t -> getViewState().showError()));
    }

    private void saveData(List<NewsDTO> news) {
        List<DbNews> dbNewsList = new ArrayList<>();
        List<DbMultimedia> dbMultimediaList = new ArrayList<>();
        for (NewsDTO newsDTO : news) {
            DbNews dbNews = dtoToDbNewsMapping.map(newsDTO);
            dbNewsList.add(dbNews);

            List<DbMultimedia> dbMultimedia = new ListMapping<>(dtoToDbMultimediaMapping).map(newsDTO.getMultimedia());
            for (DbMultimedia multimedia : dbMultimedia) {
                multimedia.setNewsId(dbNews.getId());
            }
            dbMultimediaList.addAll(dbMultimedia);
        }
        appDatabase.newsDao().deleteAll();
        appDatabase.newsDao().insertAll(dbNewsList);

        appDatabase.multimediaDao().deleteAll();
        appDatabase.multimediaDao().insertAll(dbMultimediaList);
    }

    private Observable<List<NewsDTO>> observeData() {
        return appDatabase.newsDao().getNewsWithMultimedia()
                .map(newsWithMultimedia -> {
                    List<NewsDTO> newsList = new ArrayList<>();
                    for (NewsWithMultimedia dbNews : newsWithMultimedia) {
                        NewsDTO news = dbToDtoNewsMapping.map(dbNews.dbNews);
                        List<DbMultimedia> media = dbNews.dbMultimedia;
                        news.setMultimedia((new ListMapping<>(dbToDtoMultimediaMapping)).map(media));
                        newsList.add(news);
                    }
                    return newsList;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    public void onMenuItemClick(String section) {
        if (!this.section.equals(section)) {
            this.section = section;
            getFeeds(false);
        }
    }
}
