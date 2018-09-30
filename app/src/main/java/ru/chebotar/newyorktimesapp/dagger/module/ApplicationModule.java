package ru.chebotar.newyorktimesapp.dagger.module;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import dagger.Module;
import dagger.Provides;
import ru.chebotar.newyorktimesapp.data.preference.Preferences;
import ru.chebotar.newyorktimesapp.utils.NetworkAvailability;
import ru.chebotar.newyorktimesapp.utils.ResourceProvider;

/**
 * Предоставляет общие для приложения зависимости.
 */
@Module
public class ApplicationModule {
    private Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    SharedPreferences sharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    NotificationManager notificationManager(Application application) {
        return (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    @Singleton
    NetworkAvailability networkAvailability(Application application) {
        return NetworkAvailability.getInstance(application);
    }

    @Provides
    @Singleton
    LocalBroadcastManager localBroadcastManager(Application application) {
        return LocalBroadcastManager.getInstance(application);
    }

    /**
     * Для доступа к ресурсам при отсутсвии контекста.
     */
    @Provides
    @Singleton
    ResourceProvider resourceProvider(Application application) {
        return new ResourceProvider(application);
    }

    @Provides
    @Singleton
    Preferences preferences(SharedPreferences sharedPreferences) {
        return new Preferences(sharedPreferences);
    }
}
