package ru.chebotar.newyorktimesapp.presetation.login;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.chebotar.newyorktimesapp.App;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.domain.interactors.LoginScreenInteractor;
import ru.chebotar.newyorktimesapp.domain.interactors.UserInteractor;
import ru.chebotar.newyorktimesapp.presetation.base.BaseFragment;

public class LoginFragment extends BaseFragment {

    TextView enter;
    TextView email;
    EditText password;
    TextView forgotPass;

    @Inject
    LoginScreenInteractor loginScreenInteractor;
    @Inject
    UserInteractor userInteractor;

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_login;
    }

    public static Fragment getNewInstance(Bundle data) {
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public static Bundle getBundle() {
        Bundle arguments = new Bundle();
        return arguments;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.INSTANCE.getAppComponent().inject(this);
    }

    @Override
    protected void onPostCreateView() {
        enter = rootView.findViewById(R.id.enter);
        email = rootView.findViewById(R.id.email);
        password = rootView.findViewById(R.id.password);
        forgotPass = rootView.findViewById(R.id.forgotPass);
        enter.setOnClickListener(view -> onAuthPressed("email", password.getText().toString().trim()));
        forgotPass.setOnClickListener(view -> onForgotPassPressed(email.getText().toString().trim()));
    }

    @Override
    protected void configureToolbar(@NonNull Toolbar toolbar) {
        super.configureToolbar(toolbar);
        toolbar.setTitle(R.string.enter);
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    private void onAuthPressed(String email, String pass) {
        addSub(loginScreenInteractor
                .auth(email, pass)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> showLoading(true))
                .doOnTerminate(() -> showLoading(false))
                .subscribe(
                        result -> {
                            userInteractor.saveUser(result.data);
                            showToast("Успех");
                        },
                        throwable -> showToast("Ошибка")));
    }

    private void onForgotPassPressed(String email) {
        showToast("Восстановление пароля");
    }
}
