package ru.chebotar.newyorktimesapp.dagger.module;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.chebotar.newyorktimesapp.BuildConfig;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.data.network.RxErrorHandlingCallAdapterFactory;
import ru.chebotar.newyorktimesapp.data.network.ServerAPI;
import ru.chebotar.newyorktimesapp.data.network.interceptors.ApiKeyInterceptor;
import ru.chebotar.newyorktimesapp.utils.ResourceProvider;

/**
 * Предоставляет зависимости для запросов в сеть.
 */
@Module
@Singleton
public class NetworkModule {

    @NonNull
    @Provides
    @Singleton
    OkHttpClient okHttpClient(@NonNull final ResourceProvider resourceProvider) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS);
        builder.addNetworkInterceptor(ApiKeyInterceptor.create(resourceProvider.getString(R.string.api_key_nyt)));
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        builder.addNetworkInterceptor(logging);

        return builder.build();
    }

    @NonNull
    @Provides
    @Singleton
    ServerAPI serverAPI(@NonNull final OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ServerAPI.SERVER_DOMEN)
                .build().create(ServerAPI.class);
    }

}
