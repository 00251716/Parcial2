package com.example.kevin.parcial2.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.kevin.parcial2.Entities.News;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news")
    LiveData<List<News>> getAllNews();

    @Insert
    void insert(News news);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNews(List<News> news);

    @Query("DELETE FROM news")
    void deleteAll();

}
