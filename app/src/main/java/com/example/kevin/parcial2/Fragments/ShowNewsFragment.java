package com.example.kevin.parcial2.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.parcial2.Activities.MainActivity;
import com.example.kevin.parcial2.Adapters.NewsListAdapter;
import com.example.kevin.parcial2.Entities.News;
import com.example.kevin.parcial2.R;
import com.example.kevin.parcial2.ViewModels.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShowNewsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NewsViewModel mNewsViewModel;
    private NewsListAdapter mNewsListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Mostrar registros");

        // Aquí se infla el layout para el fragmento
        final View view = inflater.inflate(R.layout.news_layout, container, false);

        mRecyclerView =  (RecyclerView) view.findViewById(R.id.recyclerview);
        mNewsListAdapter = new NewsListAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mNewsListAdapter);

        // ---------------- Cosas del ViewModel --------------
        mNewsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        // ---------------- Cosas del ViewModel --------------


        // ------------------ Aqui se añade el observer --------------
        mNewsViewModel.getAllNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> news) {
                mNewsListAdapter.setNews(news);
            }
        });

        // ------------------ Aqui se añade el observer --------------

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search,menu);

        final MenuItem searchItem = menu.findItem(R.id.item_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint("Ingrese un carnet");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //recyclerAdapter.filter(query);
                //se oculta el EditText
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //recyclerAdapter.filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //mRegistros.clear();
        //mRegistros.addAll(recyclerAdapter.getOriginal());

    }

}
