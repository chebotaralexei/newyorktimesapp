package ru.chebotar.newyorktimesapp.data.database.news;


import androidx.annotation.NonNull;
import ru.chebotar.newyorktimesapp.data.database.Mapping;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;

public class NewsDtoToDbNewsMapping extends Mapping<NewsDTO, DbNews> {

    @NonNull
    @Override
    public DbNews map(@NonNull final NewsDTO newsDTO) {
        return new DbNews(
                newsDTO.getId(),
                newsDTO.getSection(),
                newsDTO.getDescription(),
                newsDTO.getPublishDate(),
                newsDTO.getTitle(),
                newsDTO.getUrl());
    }
}
