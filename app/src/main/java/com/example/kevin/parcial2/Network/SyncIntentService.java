package com.example.kevin.parcial2.Network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.kevin.parcial2.Data.DependencyContainer;

public class SyncIntentService extends IntentService {

    private static final String TAG = "SyncIntentService";

    /**
     * Crea un intent service para jalar los datos
     */
    public SyncIntentService() {
        super("SyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: Intent service started");
        if (intent != null) {
            String[] favs = intent.getStringArrayExtra("favorites");
            NetworkDataSource networkDataSource =
                    DependencyContainer.getNetworkDataSource(this.getApplicationContext());
            networkDataSource.fetchNews(favs);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Intent service finished");
    }
}