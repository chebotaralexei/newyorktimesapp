package ru.chebotar.newyorktimesapp.data.database.news;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;
import ru.chebotar.newyorktimesapp.data.database.multimedia.DbMultimedia;

public class NewsWithMultimedia {
    @Embedded
    public DbNews dbNews;

    @Relation(parentColumn = "id",
            entityColumn = "news_id")
    public List<DbMultimedia> dbMultimedia;
}
