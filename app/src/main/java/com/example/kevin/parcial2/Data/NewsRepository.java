package com.example.kevin.parcial2.Data;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kevin.parcial2.Activities.AppExecutors;
import com.example.kevin.parcial2.DAOs.NewsDao;
import com.example.kevin.parcial2.ModelsAndEntities.News;
import com.example.kevin.parcial2.Network.NetworkDataSource;

import java.util.ArrayList;
import java.util.List;

public class NewsRepository {
    private static final String TAG = "GN:DataRepository";

    private static NewsRepository instance;
    private static final Object LOCK = new Object();

    private final NewsDao newsDao;
    private final NetworkDataSource networkDataSource;
    private final AppExecutors executors;

    private boolean initialized = false;

    public NewsRepository(NewsDao newsDao,
                          NetworkDataSource networkDataSource,
                          AppExecutors executors) {
        this.newsDao = newsDao;
        this.networkDataSource = networkDataSource;
        this.executors = executors;
        LiveData<ArrayList<News>> downloadedNews = networkDataSource.getCurrentNews();
        downloadedNews.observeForever(
                news -> executors.diskIO().execute(() -> {
                    Log.d(TAG, "DataRepository: inserting into database");
                    newsDao.insertNews(news);
                })
        );
    }


    public synchronized static NewsRepository getInstance(NewsDao newsDao,
                                                          NetworkDataSource networkDataSource,
                                                          AppExecutors executors){
        Log.d(TAG, "getInstance: Providing Repository");
        if(instance == null){
            synchronized (LOCK){
                instance = new NewsRepository(newsDao, networkDataSource, executors);
                Log.d(TAG, "getInstance: New repository made");
            }
        }
        return instance;
    }

    /**
     * Performs periodic sync tasks and check if a sync is required
     * */
    private synchronized void initializeData(){
        Log.d(TAG, "initializeData? "+(!initialized?"Yes":"No"));
        if (initialized) return;
        initialized = true;
        startFetchService();
    }

    // Database operations

    public LiveData<News> getNewById(String id){
        initializeData();
        return newsDao.getNewDetail(id);
    }

    public LiveData<List<News>> getNews(){
        initializeData();
        return newsDao.getAll();
    }



    private void deleteOldData(){

    }

    private boolean isFetchNeeded(){
        return true;
    }

    private void startFetchService(){
        networkDataSource.startFetchNewsService();
    }

    public void insert (News news) {
        new insertAsyncTask(newsDao).execute(news);
    }

    private static class insertAsyncTask extends AsyncTask<News, Void, Void> {

        private NewsDao mAsyncTaskDao;

        insertAsyncTask(NewsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final News... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
