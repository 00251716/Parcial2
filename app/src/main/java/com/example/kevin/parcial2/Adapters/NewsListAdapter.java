package com.example.kevin.parcial2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kevin.parcial2.ModelsAndEntities.News;
import com.example.kevin.parcial2.R;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>  {

    class NewsViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;

        private NewsViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
        }
    }

    private List<News> mNews;
    private Context context;


    public NewsListAdapter (Context context, List<News> newsArray) {
        this.context = context;
        this.mNews = newsArray;
    }


    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        if (mNews != null) {
            News current = mNews.get(position);
            holder.wordItemView.setText(current.getTitle());  //Vamos a probar esta vaina poniendo solo el juego
        } else {
            // Covers the case of data not being ready yet.
            holder.wordItemView.setText("No Word");
        }
    }

    public void setNews(List<News> news){
        mNews = news;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mNews != null)
            return mNews.size();
        else return 0;
    }



}
