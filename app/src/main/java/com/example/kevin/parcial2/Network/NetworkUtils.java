package com.example.kevin.parcial2.Network;

import com.example.kevin.parcial2.Data.SharedData;
import com.example.kevin.parcial2.GameNewsAPI.GamesServiceInterface;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private static Retrofit retrofit;
    private static String BASE_URL = "http://gamenewsuca.herokuapp.com";
    private static GamesServiceInterface dataService;

    public static GamesServiceInterface getClientInstance(Gson gson){
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        dataService = retrofit.create(GamesServiceInterface.class);
        return dataService;
    }
    public static GamesServiceInterface getClientInstance(){
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dataService = retrofit.create(GamesServiceInterface.class);
        return dataService;
    }
    public static GamesServiceInterface getClientInstanceAuth(){
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getHeader())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dataService = retrofit.create(GamesServiceInterface.class);
        return dataService;
    }

    private static OkHttpClient getHeader() {
        final String token = SharedData.getToken();
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }).build();
    }
}
