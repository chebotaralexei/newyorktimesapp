package ru.chebotar.newyorktimesapp.presetation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.presetation.feeds.FeedsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FeedsFragment.getNewInstance(FeedsFragment.getBundle()))
                    .commit();
    }
}
