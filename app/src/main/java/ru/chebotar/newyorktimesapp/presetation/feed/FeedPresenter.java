package ru.chebotar.newyorktimesapp.presetation.feed;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.chebotar.newyorktimesapp.App;
import ru.chebotar.newyorktimesapp.data.database.AppDatabase;
import ru.chebotar.newyorktimesapp.data.database.ListMapping;
import ru.chebotar.newyorktimesapp.data.database.multimedia.DbMultimedia;
import ru.chebotar.newyorktimesapp.data.database.multimedia.DbMultimediaToMultimediaDtoMapping;
import ru.chebotar.newyorktimesapp.data.database.multimedia.MultimediaDtoToDbMultimediaMapping;
import ru.chebotar.newyorktimesapp.data.database.news.DbNewsToNewsDtoMapping;
import ru.chebotar.newyorktimesapp.data.database.news.NewsDtoToDbNewsMapping;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;
import ru.chebotar.newyorktimesapp.presetation.base.MvpBasePresenter;

@InjectViewState
public class FeedPresenter extends MvpBasePresenter<FeedView> {

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
    private String feedId;

    public FeedPresenter() {
        App.INSTANCE.getAppComponent().inject(this);
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
        compositeDisposable.add(appDatabase.newsDao().findById(feedId)
                .map(newsWithMultimedia -> {
                    NewsDTO news = dbToDtoNewsMapping.map(newsWithMultimedia.dbNews);
                    List<DbMultimedia> media = newsWithMultimedia.dbMultimedia;
                    news.setMultimedia((new ListMapping<>(dbToDtoMultimediaMapping)).map(media));
                    return news;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsDTO -> getViewState().showData(newsDTO),
                        throwable -> getViewState().showError()));
    }

    public void refresh(boolean progress) {
        // TODO from api
        compositeDisposable.add(appDatabase.newsDao().findById(feedId)
                .map(newsWithMultimedia -> {
                    NewsDTO news = dbToDtoNewsMapping.map(newsWithMultimedia.dbNews);
                    List<DbMultimedia> media = newsWithMultimedia.dbMultimedia;
                    news.setMultimedia((new ListMapping<>(dbToDtoMultimediaMapping)).map(media));
                    return news;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsDTO -> getViewState().showData(newsDTO),
                        throwable -> getViewState().showError()));
    }

    public void onEditClick() {
        //TODO
    }

    public void onDeleteClick() {
        //TODO
    }

    public void onSaveClick() {
        //TODO
    }
}
