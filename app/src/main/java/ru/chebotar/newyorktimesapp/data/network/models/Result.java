package ru.chebotar.newyorktimesapp.data.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Result<T> implements Parcelable {
    public T data;
    public int status;
    public ArrayList<String> errors;
    public Map<String, List<String>> headers;


    public Result(T data) {
        this.data = data;
    }

    protected Result(Parcel in) {
        status = in.readInt();
        errors = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeStringList(errors);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}