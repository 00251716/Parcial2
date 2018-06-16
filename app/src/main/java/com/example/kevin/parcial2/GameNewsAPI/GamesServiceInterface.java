package com.example.kevin.parcial2.GameNewsAPI;

import com.example.kevin.parcial2.ModelsAndEntities.Login;
import com.example.kevin.parcial2.ModelsAndEntities.News;
import com.example.kevin.parcial2.ModelsAndEntities.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GamesServiceInterface {

    @GET("/news")
    Call<ArrayList<News>> getNewsList();


    @GET("/users/detail")
    Call<User> userDetail();

    @FormUrlEncoded
    @POST("/login")
    Call<Login> token(@Field("user") String username, @Field("password") String password);

}
