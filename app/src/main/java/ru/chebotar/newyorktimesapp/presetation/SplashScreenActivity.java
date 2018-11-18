package ru.chebotar.newyorktimesapp.presetation;

import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import ru.chebotar.newyorktimesapp.App;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.data.preference.Preferences;

public class SplashScreenActivity extends AppCompatActivity {

    @Inject
    public Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.INSTANCE.getAppComponent().inject(this);
        setContentView(R.layout.activity_splash_screen);

        Intent intent = new Intent(getApplicationContext(),
                preferences.isFirstRun()
                        ? TutorialActivity.class
                        : MainActivity.class);

        preferences.setFirstRun(!preferences.isFirstRun());
        startActivity(intent);
        finish();
    }
}