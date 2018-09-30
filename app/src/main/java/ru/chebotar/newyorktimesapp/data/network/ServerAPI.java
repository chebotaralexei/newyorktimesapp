package ru.chebotar.newyorktimesapp.data.network;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.chebotar.newyorktimesapp.BuildConfig;
import ru.chebotar.newyorktimesapp.data.network.models.Result;
import ru.chebotar.newyorktimesapp.data.network.models.User;

/**
 * Интерфейс запросов в сеть.
 */
public interface ServerAPI {
    String SERVER_DOMEN = "http://" + BuildConfig.HOST + "/api/";

    @GET("auth/login")
    Flowable<Result<User>>
    auth(@Query("email") String email,
         @Query("password") String pass);

    @GET("user/view")
    Flowable<Result<User>>
    getUser();

    @GET("auth/refresh-token")
    Flowable<Result<User>>
    refreshToken(@Query("token") String refreshToken);
}
