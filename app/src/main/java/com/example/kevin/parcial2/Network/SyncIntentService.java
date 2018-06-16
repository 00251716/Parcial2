package com.example.kevin.parcial2.Network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.kevin.parcial2.Data.DependencyContainer;

public class SyncIntentService extends IntentService {

    private static final String TAG = "GN:SyncIntentService";

    /**
     * Creates an IntentService for fetch the data
     */
    public SyncIntentService() {
        super("SyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: Intent service started");
        NetworkDataSource networkDataSource =
                DependencyContainer.getNetworkDataSource(this.getApplicationContext());
        networkDataSource.fetchNews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Intent service finished");
    }

}