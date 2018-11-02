package ru.chebotar.newyorktimesapp.data.network.models;

import com.google.gson.annotations.SerializedName;

public class Result<T> {

    @SerializedName("results")
    public T data;

    public Result(T data) {
        this.data = data;
    }
}