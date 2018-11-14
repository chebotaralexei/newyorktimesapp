package ru.chebotar.newyorktimesapp.data.database.multimedia;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.chebotar.newyorktimesapp.data.database.multimedia.DbMultimedia;

//Some methods can not return Rx operator. See [https://issuetracker.google.com/issues/63317956]
@Dao
public interface MultimediaDao {

   @Query("SELECT * FROM multimedia")
   Observable<List<DbMultimedia>> getAll();

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   void insertAll(List<DbMultimedia> dbMultimedias);

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   void insert(DbMultimedia dbMultimedia);

   @Delete
   void delete(DbMultimedia dbMultimedia);

   @Query("DELETE FROM multimedia")
   void deleteAll();

   @Query("SELECT * FROM multimedia WHERE news_id LIKE :newsId LIMIT 1")
   DbMultimedia findByNewsId(String newsId);

   @Query("SELECT * FROM multimedia WHERE url IN (:urls)")
   Observable<List<DbMultimedia>> loadAllByUrls(String[] urls);
}

