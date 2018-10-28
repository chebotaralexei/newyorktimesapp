package ru.chebotar.newyorktimesapp.domain.interactors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;
import ru.chebotar.newyorktimesapp.data.network.models.Result;
import ru.chebotar.newyorktimesapp.domain.base.BaseInteractor;
import ru.chebotar.newyorktimesapp.repositories.NewsRepository;

@Singleton
public class NewsScreenInteractor extends BaseInteractor {

    private final NewsRepository newsRepository;

    @Inject
    public NewsScreenInteractor(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public Single<Result<List<NewsDTO>>> getNews(String section) {
        return newsRepository.getNews(section);
    }
}
