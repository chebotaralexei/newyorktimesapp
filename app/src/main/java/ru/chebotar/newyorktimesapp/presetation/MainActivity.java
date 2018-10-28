package ru.chebotar.newyorktimesapp.presetation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import ru.chebotar.newyorktimesapp.R;
import ru.chebotar.newyorktimesapp.presetation.base.BackButtonListener;
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

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        BackButtonListener backPressedListener = null;
        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof  BackButtonListener) {
                backPressedListener = (BackButtonListener) fragment;
                break;
            }
        }

        if (!(backPressedListener != null && backPressedListener.onBackPressed())) {
            super.onBackPressed();
        }
    }
}
