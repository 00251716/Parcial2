package com.example.kevin.parcial2.GameNewsAPI;

import com.example.kevin.parcial2.Entities.News;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface NewsService {

    @GET("news")
    Call<ArrayList<News>> getNewsList(@Header("Authorization") String authHeader);

}
