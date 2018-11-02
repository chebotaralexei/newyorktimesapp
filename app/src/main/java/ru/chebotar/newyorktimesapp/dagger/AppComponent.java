package ru.chebotar.newyorktimesapp.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.chebotar.newyorktimesapp.dagger.module.ApplicationModule;
import ru.chebotar.newyorktimesapp.dagger.module.NetworkModule;
import ru.chebotar.newyorktimesapp.presetation.feeds.FeedsPresenter;

/**
 * Добавляет обьекты в граф зависимостей.
 */
@Singleton
@Component(modules = {
        NetworkModule.class,
        ApplicationModule.class,
})
public interface AppComponent {

    void inject(FeedsPresenter presenter);
}
