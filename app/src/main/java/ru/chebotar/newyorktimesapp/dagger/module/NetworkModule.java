package ru.chebotar.newyorktimesapp.dagger.module;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.chebotar.newyorktimesapp.BuildConfig;
import ru.chebotar.newyorktimesapp.data.network.RxErrorHandlingCallAdapterFactory;
import ru.chebotar.newyorktimesapp.data.network.ServerAPI;
import ru.chebotar.newyorktimesapp.data.network.interceptors.OkHttpMockInterceptor;
import ru.chebotar.newyorktimesapp.data.preference.Preferences;

/**
 * Предоставляет зависимости для запросов в сеть.
 */
@Module
@Singleton
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient okHttpClient(Application application, Preferences preferences) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG)
            builder.addInterceptor(new OkHttpMockInterceptor(application,
                    1,
                    "",
                    500,
                    1000,
                    preferences));

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        builder.addNetworkInterceptor(logging);

        return builder.build();
    }

    @Provides
    @Singleton
    ServerAPI serverAPI(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerAPI.SERVER_DOMEN)
                .build().create(ServerAPI.class);
    }

}
