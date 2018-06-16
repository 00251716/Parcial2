package com.example.kevin.parcial2.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.kevin.parcial2.Data.NewsRepository;

public class NewsViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final NewsRepository newsRepository;

    public NewsViewModelFactory(NewsRepository repository){
        newsRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NewsViewModel(newsRepository);
    }

}
