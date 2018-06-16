package com.example.kevin.parcial2.ViewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.kevin.parcial2.Data.NewsRepository;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final NewsRepository dataRepository;
    private final String id;

    public DetailViewModelFactory(NewsRepository repository, String id){
        dataRepository = repository;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NewsDetailViewModel(dataRepository, id);
    }

}
