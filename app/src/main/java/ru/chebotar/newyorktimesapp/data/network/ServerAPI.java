package ru.chebotar.newyorktimesapp.data.network;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;
import ru.chebotar.newyorktimesapp.data.network.models.Result;

/**
 * Интерфейс запросов в сеть.
 */
public interface ServerAPI {
    String SERVER_DOMEN = "http://api.nytimes.com/svc/topstories/v2/";

    @GET("{section}.json")
    Single<Result<List<NewsDTO>>>
    getNews(@Path("section") String section);
}
