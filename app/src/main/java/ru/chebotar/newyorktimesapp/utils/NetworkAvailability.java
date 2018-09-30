package ru.chebotar.newyorktimesapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Класс для проверки доступа к сети.
 */
public final class NetworkAvailability {

    private static NetworkAvailability instance;

    public static NetworkAvailability getInstance(@NonNull Context context) {
        if (instance == null) instance = new NetworkAvailability(context.getApplicationContext());
        return instance;
    }

    private final ConnectivityManager manager;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean value = manager.getActiveNetworkInfo() != null &&
                    manager.getActiveNetworkInfo().isConnectedOrConnecting();
            if (internetAvailable.getValue() != value)
                internetAvailable.onNext(value);
        }
    };
    private BehaviorSubject<Boolean> internetAvailable = BehaviorSubject.create();

    private NetworkAvailability(Context context) {
        manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        internetAvailable.onNext(manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting());

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    public boolean isInternetAvailable() {
        return internetAvailable.getValue();
    }

    public Observable<Boolean> isInternetAvailableObservable() {
        return internetAvailable.skip(1);
    }

    public BehaviorSubject<Boolean> isInternetAvailableBehaviour() {
        return internetAvailable;
    }
}
