package ru.chebotar.newyorktimesapp.repositories;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import ru.chebotar.newyorktimesapp.data.network.ServerAPI;
import ru.chebotar.newyorktimesapp.data.network.models.Result;
import ru.chebotar.newyorktimesapp.data.network.models.User;

@Singleton
public class LoginRepository extends BaseRepository {

    private final ServerAPI serverAPI;

    @Inject
    public LoginRepository(ServerAPI serverAPI) {
        this.serverAPI = serverAPI;
    }

    public Flowable<Result<User>> auth(String email, String pass) {
        return serverAPI.auth(email, pass);
    }


    public Flowable<Result<User>> getUser() {
        return serverAPI.getUser();
    }
}
