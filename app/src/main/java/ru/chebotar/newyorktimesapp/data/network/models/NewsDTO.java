package ru.chebotar.newyorktimesapp.data.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NewsDTO implements Parcelable {
    @NonNull
    @SerializedName("url")
    private String url;
    @NonNull
    @SerializedName("subsection")
    private String section;
    @NonNull
    @SerializedName("title")
    private String title;
    @NonNull
    @SerializedName("abstract")
    private String description;
    @NonNull
    @SerializedName("published_date")
    private String publishDate;
    @NonNull
    @SerializedName("multimedia")
    private List<MultimediaDTO> multimedia;

    public NewsDTO(String url, String section, String title, String description,
                   String publishDate, List<MultimediaDTO> multimedia) {
        this.url = url;
        this.section = section;
        this.title = title;
        this.description = description;
        this.publishDate = publishDate;
        this.multimedia = multimedia;
    }

    protected NewsDTO(@NonNull final Parcel in) {
        url = in.readString();
        section = in.readString();
        title = in.readString();
        description = in.readString();
        publishDate = in.readString();
        multimedia = in.createTypedArrayList(MultimediaDTO.CREATOR);
    }

    @Override
    public void writeToParcel(@NonNull final Parcel dest, final int flags) {
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

    @NonNull
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

    @NonNull
    public String getUrl() {
        return url;
    }

    @NonNull
    public String getSection() {
        return section;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getId() {
        return title + url;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getPublishDate() {
        return publishDate;
    }

    @NonNull
    public List<MultimediaDTO> getMultimedia() {
        return multimedia;
    }

    @Nullable
    public MultimediaDTO getMultimediaImage() {
        if (multimedia.isEmpty())
            return null;
        else {
            return multimedia.get(0);
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setMultimedia(List<MultimediaDTO> multimedia) {
        this.multimedia = multimedia;
    }

    @Nullable
    public MultimediaDTO getBestMultimediaImage() {
        if (multimedia.isEmpty())
            return null;
        else {
            return multimedia.get(multimedia.size() - 1);
        }
    }
}