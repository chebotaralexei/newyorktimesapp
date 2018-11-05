package ru.chebotar.newyorktimesapp.data.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.stetho.Stetho;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class MultimediaDTO implements Parcelable {
    @NonNull
    @SerializedName("url")
    private String url;
    @NonNull
    @SerializedName("type")
    private String type;
    @NonNull
    @SerializedName("height")
    private int height;
    @NonNull
    @SerializedName("width")
    private int width;

    public MultimediaDTO(String url, String type, int height, int width) {
        this.url = url;
        this.type = type;
        this.height = height;
        this.width = width;
    }

    protected MultimediaDTO(@NonNull final Parcel in) {
        url = in.readString();
        type = in.readString();
        height = in.readInt();
        width = in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull final Parcel dest, @NonNull final int flags) {
        dest.writeString(url);
        dest.writeString(type);
        dest.writeInt(height);
        dest.writeInt(width);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    public static final Creator<MultimediaDTO> CREATOR = new Creator<MultimediaDTO>() {
        @Override
        public MultimediaDTO createFromParcel(Parcel in) {
            return new MultimediaDTO(in);
        }

        @Override
        public MultimediaDTO[] newArray(int size) {
            return new MultimediaDTO[size];
        }
    };

    @NonNull
    public String getUrl() {
        return url;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public int getHeight() {
        return height;
    }

    @NonNull
    public int getWidth() {
        return width;
    }
}
