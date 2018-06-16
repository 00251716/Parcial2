package com.example.kevin.parcial2.Network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.kevin.parcial2.Activities.AppExecutors;
import com.example.kevin.parcial2.ModelsAndEntities.News;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkDataSource {

    //Number of days we want API to return
    public static final int NUM_DAYS = 14;
    private static final String TAG = "GN:NetworkDataSorce";

    //Setting intervals to do sync
    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static final String GAMENEWS_SYNC_TAG = "GameNews-sync";

    private static final Object LOCK = new Object();
    private static NetworkDataSource mInstance;

    private final Context context;
    private final AppExecutors executors;

    private final MutableLiveData<ArrayList<News>> newsArray;

    private NetworkDataSource(Context context, AppExecutors executors) {
        this.context = context;
        this.executors = executors;
        newsArray = new MutableLiveData<>();
    }

    /**
     * Get the class singleton
     */
    public static NetworkDataSource getInstance(Context context, AppExecutors executors){
        Log.d(TAG, "Providing NetworkDataSource");
        if(mInstance == null){
            synchronized (LOCK){
                mInstance = new NetworkDataSource(context.getApplicationContext(), executors);
            }
        }
        return mInstance;
    }

    public void startFetchNewsService() {
        Intent intentToFetch = new Intent(context, SyncIntentService.class);
        context.startService(intentToFetch);
        Log.d(TAG, "startFetchNewsService: IntentService executed");
    }

    public LiveData<ArrayList<News>> getCurrentNews(){
        return newsArray;
    }

    /**
     * Get the latests news
     */
    public void fetchNews() {
        Log.d(TAG, "fetchNews: Starting a News fetch");
        executors.networkIO().execute(() -> {

            Call<ArrayList<News>> call = NetworkUtils.getClientInstanceAuth().getNewsList();
            call.enqueue(new Callback<ArrayList<News>>() {
                @Override
                public void onResponse(Call<ArrayList<News>> call, Response<ArrayList<News>> response) {
                    if(response.isSuccessful()){
                        newsArray.postValue(response.body());
                        Log.d(TAG, "onResponse: News fetching successful!");
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<News>> call, Throwable t) {
                    Log.d(TAG, "onFailure: News fetching failed alv!");
                    t.printStackTrace();
                }
            });

        });
    }
}
