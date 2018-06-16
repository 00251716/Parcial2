package com.example.kevin.parcial2.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.kevin.parcial2.Data.NewsRepository;
import com.example.kevin.parcial2.ModelsAndEntities.News;

import java.util.List;

public class NewsViewModel extends ViewModel {

    private final LiveData<List<News>> newsArrayList;

    private final NewsRepository newsRepository;

    public NewsViewModel(NewsRepository repository){
        newsRepository = repository;
        newsArrayList = newsRepository.getNews();
    }


    public void insert(News news) { newsRepository.insert(news); }

    public LiveData<List<News>> getLatestNews(){
        return newsArrayList;
    }
}
