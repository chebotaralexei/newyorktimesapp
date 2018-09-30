package ru.chebotar.newyorktimesapp.data.network;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.chebotar.newyorktimesapp.data.network.models.Result;


public class RetrofitException extends RuntimeException {
    public String errorMessageToUser;

    /**
     * Если в специальном поле есть ошибка для юзера отдаем ее.
     */
    public static RetrofitException httpError(String url, Response response, Retrofit retrofit) {
        String message = response.code() + " " + response.message();
        Result<Object> error;
        String errorMessageToUser = null;
        try {
            error = new Gson().fromJson(response.errorBody().string(), Result.class);
            errorMessageToUser = error.errors.get(0);
        } catch (Throwable e) {

        }

        if (errorMessageToUser != null)
            return new RetrofitException(message, url, response, Kind.HTTP, null, retrofit, errorMessageToUser);
        else
            return new RetrofitException(message, url, response, Kind.HTTP, null, retrofit);

    }

    public static RetrofitException networkError(IOException exception) {
        return new RetrofitException(exception.getMessage(), null, null, Kind.NETWORK, exception, null);
    }

    public static RetrofitException unexpectedError(Throwable exception) {
        return new RetrofitException(exception.getMessage(), null, null, Kind.UNEXPECTED, exception, null);
    }

    /**
     * Identifies the event kind which triggered a {@link RetrofitException}.
     */
    public enum Kind {
        /**
         * An {@link IOException} occurred while communicating to the server.
         */
        NETWORK,
        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    private final String url;
    private final Response response;
    private final Kind kind;
    public final int code;
    private final Retrofit retrofit;

    RetrofitException(String message, String url, Response response, Kind kind, Throwable exception, Retrofit retrofit) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.kind = kind;
        this.retrofit = retrofit;
        this.code = response.code();
    }

    public RetrofitException(String message, String url, Response response, Kind http, Throwable exception, Retrofit retrofit, String error) {
        this(message, url, response, http, exception, retrofit);
        this.errorMessageToUser = error;
    }

    /**
     * The request URL which produced the error.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Response object containing status code, headers, body, etc.
     */
    public Response getResponse() {
        return response;
    }

    /**
     * The event kind which triggered this error.
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * The Retrofit this request was executed on
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * HTTP response body converted to specified {@code type}. {@code null} if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified {@code type}.
     */
    public <T> T getErrorBodyAs(Class<T> type) throws IOException {
        if (response == null || response.errorBody() == null) {
            return null;
        }
        Converter<ResponseBody, T> converter = retrofit.responseBodyConverter(type, new Annotation[0]);
        return converter.convert(response.errorBody());
    }
}