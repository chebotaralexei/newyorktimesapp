package ru.chebotar.newyorktimesapp.data.database.news;

import java.util.Collections;

import androidx.annotation.NonNull;
import ru.chebotar.newyorktimesapp.data.database.Mapping;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;

public class DbNewsToNewsDtoMapping extends Mapping<DbNews, NewsDTO> {

    @NonNull
    @Override
    public NewsDTO map(@NonNull final DbNews dbNews) {
        return new NewsDTO(
                dbNews.getUrl(),
                dbNews.getSection(),
                dbNews.getTitle(),
                dbNews.getDescription(),
                dbNews.getPublishDate(),
                null
        );
    }
}
