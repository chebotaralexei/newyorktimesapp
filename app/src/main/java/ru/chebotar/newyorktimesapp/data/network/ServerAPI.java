package ru.chebotar.newyorktimesapp.data.network;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.chebotar.newyorktimesapp.BuildConfig;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;
import ru.chebotar.newyorktimesapp.data.network.models.Result;
import ru.chebotar.newyorktimesapp.data.network.models.User;

/**
 * Интерфейс запросов в сеть.
 */
public interface ServerAPI {
    String SERVER_DOMEN = "http://api.nytimes.com/svc/topstories/v2/";

    @GET("{section}.json?api-key=d8e1a9137fe749a2af6506c3bb625113")
    Single<Result<List<NewsDTO>>>
    getNews(@Path("section") String section);
}
