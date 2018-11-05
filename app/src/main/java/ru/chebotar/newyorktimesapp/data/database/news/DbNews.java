package ru.chebotar.newyorktimesapp.data.database.news;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import ru.chebotar.newyorktimesapp.data.network.models.MultimediaDTO;

@Entity(tableName = "news")
public class DbNews {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;
    @NonNull
    @ColumnInfo(name = "section")
    private String section;
    @NonNull
    @ColumnInfo(name = "description")
    private String description;
    @NonNull
    @ColumnInfo(name = "publish_date")
    private String publishDate;
    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    @NonNull
    @ColumnInfo(name = "url")
    private String url;

    @Ignore
    public DbNews(String id, String section, String description, String publishDate,
                  String title, String url) {
        this.id = id;
        this.section = section;
        this.description = description;
        this.publishDate = publishDate;
        this.title = title;
        this.url = url;
    }

    public DbNews() {
    }

    public String getId() {
        return id;
    }

    public String getSection() {
        return section;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
