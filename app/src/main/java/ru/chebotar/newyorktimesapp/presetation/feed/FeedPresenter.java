package ru.chebotar.newyorktimesapp.presetation.feed;

import com.arellomobile.mvp.InjectViewState;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Completable;
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
import ru.chebotar.newyorktimesapp.data.database.news.NewsWithMultimedia;
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
    @Nullable
    private NewsWithMultimedia dbNews;

    public FeedPresenter() {
        App.INSTANCE.getAppComponent().inject(this);
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
        compositeDisposable.add(appDatabase.newsDao().findById(feedId)
                .map(newsWithMultimedia -> {
                    this.dbNews = newsWithMultimedia;
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
        compositeDisposable.add(appDatabase.newsDao().findById(feedId)
                .map(newsWithMultimedia -> {
                    this.dbNews = newsWithMultimedia;
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
        if (dbNews != null) {
            getViewState().setEditMode(true);
        }
    }

    public void onDeleteClick() {
        if (dbNews != null) {
            compositeDisposable.add(Completable.fromAction(() ->
                    appDatabase.newsDao().delete(dbNews.dbNews))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                getViewState().showMessage("Новость удалена");
                                getViewState().goBack();
                            },
                            throwable -> getViewState().showError()));

        } else {
            getViewState().showMessage("Нет новости для удаления");
        }
    }

    public void onSaveClick(String title, String text) {
        dbNews.dbNews.setTitle(title);
        dbNews.dbNews.setDescription(text);
        compositeDisposable.add(Completable.fromAction(() ->
                appDatabase.newsDao().update(dbNews.dbNews))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            refresh(true);
                            getViewState().setEditMode(false);
                        },
                        throwable -> getViewState().showError()));

    }
}
