package ru.chebotar.newyorktimesapp.data.network;

import android.util.Log;

import org.reactivestreams.Publisher;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Хэндлинг сетевых ошибок для их преобразования в кастомные RetrofitException
 */
public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    private final RxJava2CallAdapterFactory original;


    private RxErrorHandlingCallAdapterFactory() {
        original = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit));
    }

    private static class RxCallAdapterWrapper<R> implements CallAdapter<R, Object> {
        private final Retrofit retrofit;
        private final CallAdapter<R, Object> wrapped;

        public RxCallAdapterWrapper(Retrofit retrofit, CallAdapter<R, Object> wrapped) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @Override
        public Object adapt(Call<R> call) {
            Object result = wrapped.adapt(call);
            if (result instanceof Single) {
                return ((Single) result)
                        .onErrorResumeNext(new Function<Throwable, SingleSource>() {
                            @Override
                            public SingleSource apply(Throwable throwable) throws Exception {
                                return Single.error(asRetrofitException(throwable));
                            }
                        });
            }
            if (result instanceof Observable) {
                return ((Observable) result)
                        .onErrorResumeNext(new io.reactivex.functions.Function<Throwable, ObservableSource>() {
                            @Override
                            public ObservableSource apply(Throwable throwable) throws Exception {
                                return Observable.error(asRetrofitException(throwable));
                            }
                        });

            }

            if (result instanceof Flowable) {
                return ((Flowable) result)
                        .onErrorResumeNext(new Function<Throwable, Publisher>() {
                            @Override
                            public Publisher apply(Throwable throwable) throws Exception {
                                return Flowable.error(asRetrofitException(throwable));
                            }
                        });

            }
            if (result instanceof Completable) {
                return ((Completable) result)
                        .onErrorResumeNext(new Function<Throwable, CompletableSource>() {
                            @Override
                            public CompletableSource apply(Throwable throwable) throws Exception {
                                return Completable.error(asRetrofitException(throwable));
                            }
                        });
            }

            return result;
        }

        private RetrofitException asRetrofitException(Throwable throwable) {

            // We had non-200 http error
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                Response response = httpException.response();
                Log.d("refreshToken", "there was an error - " + response.code());

                return RetrofitException.httpError(response.raw().request().url().toString(), response, retrofit);
            }
            // A network error happened
            if (throwable instanceof IOException) {
                return RetrofitException.networkError((IOException) throwable);
            }

            // We don't know what happened. We need to simply convert to an unknown error

            return RetrofitException.unexpectedError(throwable);
        }
    }
}