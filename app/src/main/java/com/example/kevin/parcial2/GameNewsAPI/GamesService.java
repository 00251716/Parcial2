package com.example.kevin.parcial2.GameNewsAPI;

import com.example.kevin.parcial2.Entities.News;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GamesService {

    @GET("news")
    Call<ArrayList<News>> getNewsList(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("login")
    Call<String> login(@Header("Content-type") String content, @Field("user") String user, @Field("password") String password);

}
