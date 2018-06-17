package com.example.kevin.parcial2.GameNewsAPI;

import com.example.kevin.parcial2.ModelsAndEntities.Login;
import com.example.kevin.parcial2.ModelsAndEntities.News;
import com.example.kevin.parcial2.ModelsAndEntities.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GamesServiceInterface {

    @FormUrlEncoded
    @POST("/login")
    Call<String> login(@Field("user") String user, @Field("password")String password);


    @FormUrlEncoded
    @POST("/login")
    Call<Login> token(@Field("user") String user, @Field("password")String password);

    @GET("/news")
    Call<ArrayList<News>> getNewsList();

    @GET("/users/detail")
    Call<User> getUserDetails();

    @POST("/users/{userid}/fav")
    @FormUrlEncoded
    Call<String> addFavorite(@Path("userid") String userId, @Field("new")String newid);

    @HTTP(method = "DELETE", path = "/users/{userid}/fav", hasBody = true)
    @FormUrlEncoded
    Call<String> deleteFavorite(@Path("userid") String userId, @Field("new") String newid);
}
