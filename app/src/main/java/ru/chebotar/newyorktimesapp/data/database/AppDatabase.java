package ru.chebotar.newyorktimesapp.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import ru.chebotar.newyorktimesapp.data.database.multimedia.DbMultimedia;
import ru.chebotar.newyorktimesapp.data.database.multimedia.MultimediaDao;
import ru.chebotar.newyorktimesapp.data.database.news.DbNews;
import ru.chebotar.newyorktimesapp.data.database.news.NewsDao;

@Database(entities = {DbNews.class, DbMultimedia.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NewsDao newsDao();

    public abstract MultimediaDao multimediaDao();
}
