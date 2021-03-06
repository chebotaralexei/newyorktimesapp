package ru.chebotar.newyorktimesapp;

import android.app.Application;

import com.facebook.stetho.Stetho;

import ru.chebotar.newyorktimesapp.dagger.AppComponent;
import ru.chebotar.newyorktimesapp.dagger.DaggerAppComponent;
import ru.chebotar.newyorktimesapp.dagger.module.ApplicationModule;


public class App extends Application {

    public static App INSTANCE;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        appComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        try {
            Stetho.initializeWithDefaults(this);
        } catch (final NoClassDefFoundError error) {
        }
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
