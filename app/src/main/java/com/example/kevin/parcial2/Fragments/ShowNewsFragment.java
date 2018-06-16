package com.example.kevin.parcial2.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.parcial2.Adapters.NewsListAdapter;
import com.example.kevin.parcial2.Data.DependencyContainer;
import com.example.kevin.parcial2.ModelsAndEntities.News;
import com.example.kevin.parcial2.R;
import com.example.kevin.parcial2.ViewModels.NewsDetailViewModel;
import com.example.kevin.parcial2.ViewModels.NewsViewModel;
import com.example.kevin.parcial2.ViewModels.NewsViewModelFactory;

import java.util.List;

public class ShowNewsFragment extends Fragment {
    RecyclerView newsRecycler;
    NewsListAdapter newsAdapter;
    List<News> newsArray;
    NewsDetailViewModel model;

    private static final String TAG = "GameNews - NewsFragment";

    public ShowNewsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_layout, container, false);

        newsRecycler = v.findViewById(R.id.recyclerview);
        newsRecycler.setHasFixedSize(true);
        newsRecycler.setLayoutManager(new LinearLayoutManager(container.getContext()));

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated: Getting the ModelFactory");
        NewsViewModelFactory factory = DependencyContainer.getNewsViewModelFactory(getContext());
        NewsViewModel model = ViewModelProviders.of(this, factory).get(NewsViewModel.class);
        model.getLatestNews().observe(this, news -> {
            loadList(news);
        });
    }

    void loadList(List<News> news){
        // TODO: Verificar si la lista está vacía
        newsArray = news;
        newsAdapter = new NewsListAdapter(getContext(), newsArray);
        newsRecycler.setAdapter(newsAdapter);
    }


}
