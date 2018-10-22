package ru.chebotar.newyorktimesapp.presetation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import ru.chebotar.newyorktimesapp.R;

public class TestActivity extends AppCompatActivity {

    private final Object object = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    public void onStart() {
        super.onStart();
        new Thread(new LeftLeg()).start();
        new Thread(new RightLeg()).start();
    }

    private class LeftLeg implements Runnable {
        private boolean isRunning = true;

        @Override
        public void run() {
            while (isRunning) {
                synchronized (object) {
                    System.out.println("Left step");
                    object.notify();
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private class RightLeg implements Runnable {
        private boolean isRunning = true;

        @Override
        public void run() {
            while (isRunning) {
                synchronized (object) {
                    System.out.println("Right step");
                    object.notify();
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
