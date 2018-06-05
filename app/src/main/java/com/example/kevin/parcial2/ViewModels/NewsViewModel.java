package com.example.kevin.parcial2.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.kevin.parcial2.DBUtils.NewsRepository;
import com.example.kevin.parcial2.Entities.News;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {

    //Referencia al repositorio
    private NewsRepository mRepository;

    //To cache the list of words
    private LiveData<List<News>> mAllNews;

    public NewsViewModel(Application application){
        super(application);
        mRepository = new NewsRepository(application);
        mAllNews = mRepository.getAllNews();
    }

    //El objetivo aqui es esconder los detalles de implementacion del UI
    public LiveData<List<News>> getAllNews() {
        return mAllNews;
    }

    public void insert(News news){
        mRepository.insert(news);
    }



}
