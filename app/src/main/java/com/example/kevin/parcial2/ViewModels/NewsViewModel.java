package com.example.kevin.parcial2.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.kevin.parcial2.Data.NewsRepository;
import com.example.kevin.parcial2.ModelsAndEntities.News;

import java.util.List;

public class NewsViewModel extends ViewModel {

    private LiveData<List<News>> newsArrayList;

    private final NewsRepository dataRepository;

    public NewsViewModel(NewsRepository repository){
        dataRepository = repository;
        newsArrayList = dataRepository.getNews();
    }

    public LiveData<List<News>> getLatestNews(){
        return newsArrayList;
    }

    public void refreshNews(){
        newsArrayList = dataRepository.getNews();
    }
}


