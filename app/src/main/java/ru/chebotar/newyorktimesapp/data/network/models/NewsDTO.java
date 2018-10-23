package ru.chebotar.newyorktimesapp.data.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewsDTO implements Parcelable {
    @SerializedName("url")
    private String url;
    @SerializedName("subsection")
    private String section;
    @SerializedName("title")
    private String title;
    @SerializedName("abstract")
    private String description;
    @SerializedName("published_date")
    private String publishDate;
    @SerializedName("multimedia")
    private List<MultimediaDTO> multimedia;

    protected NewsDTO(Parcel in) {
        url = in.readString();
        section = in.readString();
        title = in.readString();
        description = in.readString();
        publishDate = in.readString();
        multimedia = in.createTypedArrayList(MultimediaDTO.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(section);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(publishDate);
        dest.writeTypedList(multimedia);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NewsDTO> CREATOR = new Creator<NewsDTO>() {
        @Override
        public NewsDTO createFromParcel(Parcel in) {
            return new NewsDTO(in);
        }

        @Override
        public NewsDTO[] newArray(int size) {
            return new NewsDTO[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public String getSection() {
        return section;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public List<MultimediaDTO> getMultimedia() {
        return multimedia;
    }
}