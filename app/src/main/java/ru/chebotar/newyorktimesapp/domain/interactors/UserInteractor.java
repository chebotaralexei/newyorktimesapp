package ru.chebotar.newyorktimesapp.domain.interactors;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import ru.chebotar.newyorktimesapp.data.network.models.Result;
import ru.chebotar.newyorktimesapp.data.network.models.User;
import ru.chebotar.newyorktimesapp.data.preference.Preferences;
import ru.chebotar.newyorktimesapp.repositories.LoginRepository;


@Singleton
public class UserInteractor extends BaseInteractor {
    private final Preferences preferences;
    private final LoginRepository loginRepository;

    @Inject
    public UserInteractor(Preferences preferences, LoginRepository loginRepository) {
        this.preferences = preferences;
        this.loginRepository = loginRepository;
    }

    public boolean isUserLogined() {
        return preferences.getUser() != null;
    }

    public void saveUser(User user) {
        preferences.setUser(user);
    }

    public User getUserFromPrefs() {
        return preferences.getUser();
    }

    public Observable<User> getUserChanges() {
        return preferences.userObservable();
    }

    public Flowable<Result<User>> getUser() {
        return loginRepository.getUser();
    }

    public boolean isFirstRun() {
        return preferences.isFirstRun();
    }

    public void setFirstRun() {
        preferences.setFirstRun();
    }

    public void logout() {
        preferences.setUser(null);
    }
}
