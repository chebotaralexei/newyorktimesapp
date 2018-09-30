package ru.chebotar.newyorktimesapp.data.network.interceptors;

import com.google.gson.annotations.SerializedName;


public class MockedResponse {
    @SerializedName("status")
    int mStatusCode;

    public MockedResponse(int mStatusCode) {
        this.mStatusCode = mStatusCode;
    }

    public int getStatusCode() {
        return mStatusCode;
    }
}
