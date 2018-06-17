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

    private NewsRepository(NewsDao newsDao,
                           NetworkDataSource networkDataSource,
                           AppExecutors executors) {
        this.newsDao = newsDao;
        this.networkDataSource = networkDataSource;
        this.executors = executors;

        LiveData<ArrayList<News>> downloadedNews = networkDataSource.getCurrentNews();
        downloadedNews.observeForever(
                news -> this.executors.diskIO().execute(() -> {
                    Log.d(TAG, "DataRepository: truncating News table");
                    newsDao.deleteAll();
                    Log.d(TAG, "DataRepository: Inserting into database");
                    newsDao.insertNews(news);

                })
        );

        LiveData<String[]> favorites = networkDataSource.getCurrentFavs();
        favorites.observeForever(
                favs -> this.executors.diskIO().execute(() ->{
                    Log.d(TAG, "DataRepository: Updating favorite news");
                    for (String fav:favs){
                        newsDao.updateFavorite(fav,true);
                    }
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
        //if (initialized) return;
        initialized = true;
        networkDataSource.getUserDetails();
    }

    // Database operations

    public LiveData<News> getNewById(String id){
        //initializeData();
        return newsDao.getNewDetail(id);
    }

    public LiveData<List<News>>getNews(){
        initializeData();
        return newsDao.getAll();
    }

    public void updateFavorite(String newid, boolean fav){
        executors.diskIO().execute(()-> newsDao.updateFavorite(newid,fav));
    }

}
