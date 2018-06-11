package com.example.kevin.parcial2.GameNewsAPI;

import com.example.kevin.parcial2.Persistence.SharedData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GamesService {

    private static GamesServiceInterface API_SERVICE;

    private static String BASE_URL = "http://gamenewsuca.herokuapp.com";

    public static GamesServiceInterface getApiService(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        API_SERVICE = retrofit.create(GamesServiceInterface.class);
        return API_SERVICE;
    }

    public static GamesServiceInterface getApiServiceWithAuthorization() {
        Gson gson = new GsonBuilder().create();
        API_SERVICE = getApiServiceWithAuthorization(gson);
        return API_SERVICE;
    }

    public static GamesServiceInterface getApiServiceWithAuthorization(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getHeaderAuthorization())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API_SERVICE = retrofit.create(GamesServiceInterface.class);
        return API_SERVICE;
    }

    private static OkHttpClient getHeaderAuthorization() {
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + SharedData.getToken())
                    .build();
            return chain.proceed(newRequest);
        }).build();
    }

}
