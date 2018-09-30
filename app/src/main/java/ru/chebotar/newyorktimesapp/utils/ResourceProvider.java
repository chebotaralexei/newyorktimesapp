package ru.chebotar.newyorktimesapp.utils;

import android.app.Application;

import androidx.annotation.StringRes;

/**
 * Обертка для доступа к ресурсам при отсутсвии контекста.
 */
public class ResourceProvider {

    private final Application application;

    public ResourceProvider(Application application) {
        this.application = application;
    }

    public String getString(@StringRes int id){
        return application.getString(id);
    }
}
