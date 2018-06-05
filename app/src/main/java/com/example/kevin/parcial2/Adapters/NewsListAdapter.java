package com.example.kevin.parcial2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kevin.parcial2.Models.News;
import com.example.kevin.parcial2.R;

import java.util.ArrayList;

public class NewsListAdapter   {

    /*extends RecyclerView.Adapter<NewsListAdapter.MyViewHolder>
    Context mContext;
    ArrayList<News> mData;
    ArrayList<News> itemsCopy = new ArrayList<>();

    public NewsListAdapter(Context mContext, ArrayList<News> mData){
        this.mContext = mContext;
        this.mData = mData;
        //itemsCopy.addAll(DBHelper.ourInstance.getCurrentList());
    }

    public NewsListAdapter(){

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.news_item, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textViewCarnet.setText(mData.get(position).getCarnet());
        holder.textViewNota.setText(mData.get(position).getNota());
        holder.textViewMateria.setText(mData.get(position).getMateria());
        holder.textViewDocente.setText(mData.get(position).getDocente());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout registroItem;
        private TextView textViewCarnet;
        private TextView textViewNota;
        private TextView textViewMateria;
        private TextView textViewDocente;

        public MyViewHolder(View itemView) {
            super(itemView);

            registroItem = itemView.findViewById(R.id.registroItemLayout);
            textViewCarnet = itemView.findViewById(R.id.textViewCarnet);
            textViewNota = itemView.findViewById(R.id.textViewNota);
            textViewMateria = itemView.findViewById(R.id.textViewMateria);
            textViewDocente = itemView.findViewById(R.id.textViewDocente);

        }
    }

    //Este método sirve para filtrar resultados del buscador
    public void filter(String text) {
        mData.clear();
        if(text.isEmpty()){
            mData.addAll(itemsCopy);
        } else {
            text = text.toLowerCase();
            for(Registro r : itemsCopy) {
                if(r.getCarnet().contains(text)) mData.add(r);
            }
        }
        notifyDataSetChanged();
    }

    public ArrayList<Registro> getOriginal(){
        return itemsCopy;
    }

*/
}
