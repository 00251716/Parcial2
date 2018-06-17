package com.example.kevin.parcial2.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
    private static GamesServiceInterface gameService;

    public static GamesServiceInterface getClientInstance(Gson gson){
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        gameService = retrofit.create(GamesServiceInterface.class);
        return gameService;
    }
    public static GamesServiceInterface getClientInstance(){
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gameService = retrofit.create(GamesServiceInterface.class);
        return gameService;
    }
    public static GamesServiceInterface getClientInstanceAuth(){
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getHeader())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gameService = retrofit.create(GamesServiceInterface.class);
        return gameService;
    }
    public static GamesServiceInterface getClientInstanceAuth(Gson gson){
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getHeader())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        gameService = retrofit.create(GamesServiceInterface.class);
        return gameService;
    }

    private static OkHttpClient getHeader() {
        final String token = SharedData.read(SharedData.KEY_TOKEN,null);
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }).build();
    }

    public static boolean checkConectivity(Context context){
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
