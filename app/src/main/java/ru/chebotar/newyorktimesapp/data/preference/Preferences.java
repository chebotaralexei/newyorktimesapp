package ru.chebotar.newyorktimesapp.data.preference;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import ru.chebotar.newyorktimesapp.data.network.models.User;

/**
 * Враппер над SharedPreferences.
 */
public class Preferences {

    private static final String KEY_FIRST_RUN = "FIRST_RUN";
    private static final String KEY_MOCK = "MOCK";
    private static final Gson GSON = new Gson();
    private static final String KEY_USER = "USER";
    private static final String KEY_F_USER_ID = "F_USER_ID";

    private final SharedPreferences sharedPreferences;
    private final PublishSubject<User> userSubject = PublishSubject.create();

    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = (sharedPreferences1, key) -> {
        User user = getUser();
        if (KEY_USER.equals(key) && user != null) {
            userSubject.onNext(user);
            return;
        }
    };

    public Preferences(SharedPreferences preferences) {
        sharedPreferences = preferences;
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    // Observables

    public Observable<User> userObservable() {
        return userSubject;
    }


    public void setUser(User user) {
        sharedPreferences.edit().putString(KEY_USER, GSON.toJson(user)).apply();
    }

    public User getUser() {
        return GSON.fromJson(sharedPreferences.getString(KEY_USER, null), User.class);
    }

    public boolean isFirstRun() {
        return sharedPreferences.getBoolean(KEY_FIRST_RUN, true);
    }

    public void setFirstRun(final boolean firstRun) {
        sharedPreferences.edit().putBoolean(KEY_FIRST_RUN, firstRun).apply();
    }

    public void setMock(boolean mock) {
        sharedPreferences.edit().putBoolean(KEY_MOCK, mock).apply();
    }

    public boolean getMock() {
        return sharedPreferences.getBoolean(KEY_MOCK, true);
    }
}
