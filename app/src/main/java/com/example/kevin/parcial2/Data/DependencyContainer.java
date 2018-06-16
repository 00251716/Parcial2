package com.example.kevin.parcial2.Data;

import android.content.Context;

import com.example.kevin.parcial2.Activities.AppExecutors;
import com.example.kevin.parcial2.Network.NetworkDataSource;
import com.example.kevin.parcial2.ViewModels.DetailViewModelFactory;
import com.example.kevin.parcial2.ViewModels.NewsViewModelFactory;

public class DependencyContainer {

    public static NewsRepository getRepository(Context context){
        AppRoomDatabase database = AppRoomDatabase.getDatabase(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();

        NetworkDataSource networkDataSource =
                NetworkDataSource.getInstance(context.getApplicationContext(),
                        executors);
        return NewsRepository.getInstance(database.newsDao(),networkDataSource,executors);
    }

    public static NetworkDataSource getNetworkDataSource(Context context){
        AppExecutors executors = AppExecutors.getInstance();
        return NetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static DetailViewModelFactory getDetailViewModelFactory(Context context, String id){
        NewsRepository dataRepository = getRepository(context.getApplicationContext());
        return new DetailViewModelFactory(dataRepository, id);
    }

    public static NewsViewModelFactory getNewsViewModelFactory(Context context){
        NewsRepository dataRepository = getRepository(context.getApplicationContext());
        return new NewsViewModelFactory(dataRepository);
    }

}
