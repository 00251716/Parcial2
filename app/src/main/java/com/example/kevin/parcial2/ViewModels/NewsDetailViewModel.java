package com.example.kevin.parcial2.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.kevin.parcial2.Data.NewsRepository;
import com.example.kevin.parcial2.ModelsAndEntities.News;

public class NewsDetailViewModel extends ViewModel {

    private final LiveData<News> mNew;

    private final NewsRepository newsRepository;

    public NewsDetailViewModel(NewsRepository repository, String id){
        newsRepository = repository;
        mNew = newsRepository.getNewById(id);
    }

    public LiveData<News> getNew() {
        return mNew;
    }

}
