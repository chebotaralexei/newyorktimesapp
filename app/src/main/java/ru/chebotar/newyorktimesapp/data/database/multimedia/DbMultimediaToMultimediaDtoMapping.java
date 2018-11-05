package ru.chebotar.newyorktimesapp.data.database.multimedia;

import androidx.annotation.NonNull;
import ru.chebotar.newyorktimesapp.data.database.Mapping;
import ru.chebotar.newyorktimesapp.data.database.news.DbNews;
import ru.chebotar.newyorktimesapp.data.network.models.MultimediaDTO;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;

public class DbMultimediaToMultimediaDtoMapping extends Mapping<DbMultimedia, MultimediaDTO> {

    @NonNull
    @Override
    public MultimediaDTO map(@NonNull final DbMultimedia dbNews) {
        return new MultimediaDTO(
                dbNews.getUrl(),
                dbNews.getType(),
                dbNews.getHeight(),
                dbNews.getWidth());
    }
}
