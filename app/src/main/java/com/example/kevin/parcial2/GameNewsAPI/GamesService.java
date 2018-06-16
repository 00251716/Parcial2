package com.example.kevin.parcial2.GameNewsAPI;

import android.util.Log;

import com.example.kevin.parcial2.Data.SharedData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.constraint.Constraints.TAG;

public class GamesService {

    private static GamesServiceInterface API_SERVICE;

    private static String BASE_URL = "http://gamenewsuca.herokuapp.com";

    public static GamesServiceInterface getApiService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
        Log.d(TAG, "getApiService: " + client.interceptors().size());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
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
