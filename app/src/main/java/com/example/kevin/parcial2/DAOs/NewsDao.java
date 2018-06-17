package com.example.kevin.parcial2.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.kevin.parcial2.ModelsAndEntities.News;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NewsDao {

    @Insert
    void insert(News news);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNews(ArrayList<News> news);

    @Query("SELECT * FROM news ORDER BY created_date DESC")
    LiveData<List<News>> getAll();

    @Query("SELECT * FROM news WHERE id = :newid")
    LiveData<News> getNewDetail(String newid);

    @Query("DELETE FROM news")
    void deleteAll();

    @Query("UPDATE news SET favorite = :favorite WHERE id LIKE :id")
    void updateFavorite(String id, boolean favorite);


}
