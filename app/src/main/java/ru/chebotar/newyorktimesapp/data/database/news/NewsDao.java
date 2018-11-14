package ru.chebotar.newyorktimesapp.data.database.news;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Observable;
import io.reactivex.Single;

//Some methods can not return Rx operator. See [https://issuetracker.google.com/issues/63317956]
@Dao
public interface NewsDao {

   @Query("SELECT * FROM news")
   List<DbNews> getAll();

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   void insertAll(List<DbNews> dbNews);

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   void insert(DbNews dbNews);

   @Delete
   void delete(DbNews newsItem);

   @Query("DELETE FROM news")
   void deleteAll();

   @Query("SELECT * FROM news WHERE title LIKE :title LIMIT 1")
   Single<DbNews> findByName(String title);

   @Query("SELECT * FROM news WHERE title IN (:titles)")
   Observable<List<DbNews>> loadAllByTitles(String[] titles);

   @Query("SELECT * FROM news")
   Observable<List<NewsWithMultimedia>> getNewsWithMultimedia();
}

