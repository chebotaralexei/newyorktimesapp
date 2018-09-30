package ru.chebotar.newyorktimesapp.domain.interactors;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import ru.chebotar.newyorktimesapp.data.network.models.Result;
import ru.chebotar.newyorktimesapp.data.network.models.User;
import ru.chebotar.newyorktimesapp.repositories.LoginRepository;

@Singleton
public class LoginScreenInteractor extends BaseInteractor {

    private final LoginRepository loginRepository;

    @Inject
    public LoginScreenInteractor(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public Flowable<Result<User>> auth(String email, String pass) {
        return loginRepository.auth(email, pass);
    }


}
