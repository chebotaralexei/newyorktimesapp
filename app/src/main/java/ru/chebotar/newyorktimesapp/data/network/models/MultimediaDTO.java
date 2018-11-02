package ru.chebotar.newyorktimesapp.data.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MultimediaDTO implements Parcelable {
    @SerializedName("url")
    private String url;
    @SerializedName("type")
    private String type;
    @SerializedName("height")
    private int height;
    @SerializedName("width")
    private int width;

    protected MultimediaDTO(Parcel in) {
        url = in.readString();
        type = in.readString();
        height = in.readInt();
        width = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(type);
        dest.writeInt(height);
        dest.writeInt(width);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
