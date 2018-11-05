package ru.chebotar.newyorktimesapp.data.database.multimedia;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import ru.chebotar.newyorktimesapp.data.database.news.DbNews;

@Entity(tableName = "multimedia",
        foreignKeys = @ForeignKey(entity = DbNews.class,
                parentColumns="id",
                childColumns = "news_id",
                onDelete = ForeignKey.CASCADE))
public class DbMultimedia {

    @ColumnInfo(name = "news_id")
    public String newsId;
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "url")
    private String url;
    @NonNull
    @ColumnInfo(name = "type")
    private String type;
    @NonNull
    @ColumnInfo(name = "height")
    private int height;
    @NonNull
    @ColumnInfo(name = "width")
    private int width;

    @Ignore
    public DbMultimedia(String url, String type, int height, int width) {
        this.url = url;
        this.type = type;
        this.height = height;
        this.width = width;
    }

    public DbMultimedia() {
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
