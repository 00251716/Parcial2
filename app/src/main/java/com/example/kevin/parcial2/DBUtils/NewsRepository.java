package com.example.kevin.parcial2.DBUtils;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.kevin.parcial2.DAOs.NewsDao;
import com.example.kevin.parcial2.Entities.News;

import java.util.List;

public class NewsRepository {

    private NewsDao mNewsDao;
    private LiveData<List<News>> mAllNews;

    //Este constructor maneja la base de datos e inicializa las member variables
    public NewsRepository(Application application){
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mNewsDao = db.newsDao();
        mAllNews = mNewsDao.getAllNews();
    }

    public LiveData<List<News>> getAllNews() {
        return mAllNews;
    }

    //De Google developers: You must call this on a non-UI thread or your app will crash. Room ensures that you don't do any long-running operations on the main thread, blocking the UI.
    public void insert(News news){
        new insertAsyncTask(mNewsDao).execute(news);
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
