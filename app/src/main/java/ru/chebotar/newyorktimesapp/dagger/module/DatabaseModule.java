package ru.chebotar.newyorktimesapp.dagger.module;

import android.app.Application;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import dagger.Module;
import dagger.Provides;
import ru.chebotar.newyorktimesapp.data.database.AppDatabase;

/**
 * Предоставляет зависимости для запросов в бд.
 */
@Module
@Singleton
public class DatabaseModule {

    private static final String DATABASE_NAME = "NyTimesDb.db";

    @NonNull
    @Provides
    @Singleton
    AppDatabase provideRoomDatabase(@NonNull final Application application) {
        return Room.databaseBuilder(application,
                AppDatabase.class,
                DATABASE_NAME)
                .build();
    }
}
