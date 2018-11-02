package ru.chebotar.newyorktimesapp.repositories;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.chebotar.newyorktimesapp.data.network.ServerAPI;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;
import ru.chebotar.newyorktimesapp.data.network.models.Result;
import ru.chebotar.newyorktimesapp.data.network.models.User;

@Singleton
public class NewsRepository extends BaseRepository {

    private final ServerAPI serverAPI;

    @Inject
    public NewsRepository(ServerAPI serverAPI) {
        this.serverAPI = serverAPI;
    }

    public Single<Result<List<NewsDTO>>> getNews(String section) {
        return serverAPI.getNews(section);
    }
}
