package ru.chebotar.newyorktimesapp.data.network.interceptors;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.Random;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ru.chebotar.newyorktimesapp.data.preference.Preferences;
import ru.chebotar.newyorktimesapp.utils.Utils;

/**
 * Мокает ответы от сети если mPreferences.getMock().
 */
public class OkHttpMockInterceptor implements Interceptor {
    public final static String DEFAULT_BASE_PATH = "";
    public final static int DELAY_DEFAULT_MIN = 500;
    public final static int DELAY_DEFAULT_MAX = 1500;
    private final Preferences mPreferences;
    private Context mContext;
    private int mFailurePercentage;
    private String mBasePath;
    private int mMinDelayMilliseconds;

    private int mMaxDelayMilliseconds;

    /**
     * @param failurePercentage - Процент запросов которые сэмулируют ошибку.
     * @param basePath - Путь к папке в ассетах в которой лежат mock-ответы.
     * @param minDelayMilliseconds - Минимальная задержка сэмулированого ответа.
     * @param maxDelayMilliseconds - Минимальная задержка сэмулированого ответа.
     */
    public OkHttpMockInterceptor(Context context, int failurePercentage, String basePath,
                                 int minDelayMilliseconds, int maxDelayMilliseconds, Preferences preferences) {
        mContext = context;
        mFailurePercentage = failurePercentage;
        mBasePath = basePath;
        mMinDelayMilliseconds = minDelayMilliseconds;
        mMaxDelayMilliseconds = maxDelayMilliseconds;
        mPreferences = preferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!mPreferences.getMock())
            return chain.proceed(chain.request());
        Gson gson = new GsonBuilder().setLenient().create();
        HttpUrl url = chain.request().url();
        String responseString = getResponseString(url);


        MockedResponse mockedResponse = new MockedResponse(404);
        if (responseString != null) {
            try {
                mockedResponse = gson.fromJson(responseString, MockedResponse.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        try {
            Thread.sleep(Math.abs(new Random()
                    .nextInt() % (mMaxDelayMilliseconds - mMinDelayMilliseconds))
                    + mMinDelayMilliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean failure = Math.abs(new Random().nextInt() % 100) < mFailurePercentage;
        int statusCode = failure ? 504 : mockedResponse.getStatusCode(); /*504 или 404 или из mock-ответа*/

        return new Response.Builder()
                .code(statusCode)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString))
                .addHeader("content-type", "application/json")
                .build();
    }

    /**
     * Тащит ответ из мока.
     * Принцип поиска:
     * по запросу
     * будет искать файл api/auth/check-email/email=1.json
     * если не найдет то будет искать api/auth/check-email/userId=0.json
     * если не найдет то будет искать api/auth/check-email.json
     */
    private String getResponseString(HttpUrl url) {
        String sym = "";
        String query = url.encodedQuery() == null ? "" : url.encodedQuery();
        if (!query.equals(""))
            sym = "/";
        String[] queries = query.split("&");
        String path = url.encodedPath() + sym + query;
        String realPath = mBasePath + path.substring(1) + ".json";
        String responseString = Utils.loadAssetTextAsString(mContext, realPath);
        if (responseString == null && queries.length != 0) {
            for (int i = 0; i < queries.length && responseString ==null; i++) {
                path = url.encodedPath() + sym + queries[i];
                realPath = mBasePath + path.substring(1) + ".json";
                responseString = Utils.loadAssetTextAsString(mContext, realPath);
            }
        }
        if (responseString == null) {
            realPath = mBasePath + url.encodedPath().substring(1) + ".json";
            responseString = Utils.loadAssetTextAsString(mContext,
                    realPath);
        }

        Log.d("jsonPath", realPath);
        return responseString;
    }

    public int getFailurePercentage() {
        return mFailurePercentage;
    }

    public OkHttpMockInterceptor setFailurePercentage(int failurePercentage) {
        mFailurePercentage = failurePercentage;
        return this;
    }

    public String getBasePath() {
        return mBasePath;
    }

    public OkHttpMockInterceptor setBasePath(String basePath) {
        mBasePath = basePath;
        return this;
    }

    public int getMinDelayMilliseconds() {
        return mMinDelayMilliseconds;
    }

    public OkHttpMockInterceptor setMinDelayMilliseconds(int minDelayMilliseconds) {
        mMinDelayMilliseconds = minDelayMilliseconds;
        return this;
    }

    public int getMaxDelayMilliseconds() {
        return mMaxDelayMilliseconds;
    }

    public OkHttpMockInterceptor setMaxDelayMilliseconds(int maxDelayMilliseconds) {
        mMaxDelayMilliseconds = maxDelayMilliseconds;
        return this;
    }
}