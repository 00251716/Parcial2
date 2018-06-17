package com.example.kevin.parcial2.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kevin.parcial2.Activities.AppExecutors;
import com.example.kevin.parcial2.Activities.NewsInfoActivity;
import com.example.kevin.parcial2.Adapters.NewsListAdapter;
import com.example.kevin.parcial2.Data.DependencyContainer;
import com.example.kevin.parcial2.ModelsAndEntities.News;
import com.example.kevin.parcial2.Network.NetworkDataSource;
import com.example.kevin.parcial2.R;
import com.example.kevin.parcial2.ViewModels.NewsDetailViewModel;
import com.example.kevin.parcial2.ViewModels.NewsViewModel;
import com.example.kevin.parcial2.ViewModels.NewsViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ShowNewsFragment extends Fragment implements NewsListAdapter.onNewsClickHandler, SwipeRefreshLayout.OnRefreshListener{

    RecyclerView newsRecycler;
    NewsListAdapter newsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    List<News> newsArray = new ArrayList<>();
    NewsViewModel newsViewModel;

    private static final String TAG = "ShowNewsFragment";

    public ShowNewsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_fragment_layout, container, false);

        newsRecycler = v.findViewById(R.id.main_recycler);
        newsRecycler.setLayoutManager(new LinearLayoutManager(container.getContext()));
        newsRecycler.setHasFixedSize(true);
        newsAdapter = new NewsListAdapter(getContext(), newsArray,this);
        newsRecycler.setAdapter(newsAdapter);

        swipeRefreshLayout = v.findViewById(R.id.main_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefreshLayout.post(()->{ swipeRefreshLayout.setRefreshing(true); });
        Log.d(TAG, "onCreateView: Activating swipe animation");
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onCreate: Setting NewsModelFactory");
        NewsViewModelFactory factory = DependencyContainer.getNewsViewModelFactory(getContext());

        Log.d(TAG, "onCreate: Setting newsViewModel");
        newsViewModel = ViewModelProviders.of(this, factory).get(NewsViewModel.class);


        newsViewModel.getLatestNews().observe(this, news -> {
            Log.d(TAG, "onActivityCreated: Ejecutando observer");
            Log.d(TAG, "observer: Hiding swipe animation");
            swipeRefreshLayout.setRefreshing(false);
            loadList(news);
        });

        Log.d(TAG, "onActivityCreated: newsViewModel prepared!");
    }

    void loadList(List<News> news){
        newsArray.clear();
        newsArray.addAll(news);
        newsAdapter.notifyDataSetChanged();
        Log.d("MIR� AQUI", newsArray.size() + " ");
        swipeRefreshLayout.post(()->{ swipeRefreshLayout.setRefreshing(false); });
    }

    @Override
    public void onNewsClick(News mNew) {
        Intent intent = new Intent(getContext(), NewsInfoActivity.class);
        intent.putExtra("id",mNew.getId());
        startActivity(intent);
    }

    @Override
    public void onNewsChecked(ImageView v, String newid) {
        NetworkDataSource.getInstance(this.getContext(), AppExecutors.getInstance()).setFavorite(v,newid,newsRecycler);
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        Log.d(TAG, "onRefresh: Loading data...");
        newsViewModel.refreshNews();
        Log.d(TAG, "onRefresh: Finished");
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(newsAdapter != null){
            Log.d(TAG, "onResume: notifying...");
            newsAdapter.notifyDataSetChanged();
        }
    }

}
