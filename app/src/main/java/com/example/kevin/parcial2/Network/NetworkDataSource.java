package com.example.kevin.parcial2.Network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kevin.parcial2.Activities.AppExecutors;
import com.example.kevin.parcial2.Data.DependencyContainer;
import com.example.kevin.parcial2.Data.SharedData;
import com.example.kevin.parcial2.GameNewsAPI.GamesServiceInterface;
import com.example.kevin.parcial2.ModelsAndEntities.News;
import com.example.kevin.parcial2.ModelsAndEntities.User;
import com.example.kevin.parcial2.R;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Manages operations to perform with the API
 * Provides the most recent downloaded data
 * */
public class NetworkDataSource {

    private static final String TAG = "NetworkDataSorce";

    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static final String GAMENEWS_SYNC_TAG = "GameNews-sync";

    private static final Object LOCK = new Object();
    private static NetworkDataSource mInstance;

    private final Context context;
    private final AppExecutors executors;

    private final MutableLiveData<ArrayList<News>> newsArray;
    private final MutableLiveData<String[]> favorites;

    private NetworkDataSource(Context context, AppExecutors executors) {
        this.context = context;
        this.executors = executors;
        newsArray = new MutableLiveData<>();
        favorites = new MutableLiveData<>();
    }

    /**
     * Obtener la instancia
     */
    public static NetworkDataSource getInstance(Context context, AppExecutors executors){
        Log.d(TAG, "Providing NetworkDataSource");
        if(mInstance == null){
            synchronized (LOCK){
                mInstance = new NetworkDataSource(context.getApplicationContext(), executors);
            }
        }
        return mInstance;
    }


    public LiveData<ArrayList<News>> getCurrentNews(){
        return newsArray;
    }

    public LiveData<String[]> getCurrentFavs(){
        return favorites;
    }

    /**
     * Comienza un intent service para jalar las noticias
     */
    public void startFetchNewsService(String[] favs) {
        Intent intentToFetch = new Intent(context, SyncIntentService.class);
        intentToFetch.putExtra("favorites",favs);
        context.startService(intentToFetch);
        Log.d(TAG, "startFetchNewsService: IntentService executed");
    }

    /**
     * Obtener las �ltimas noticias
     */
    public void fetchNews(String[] favs) {
        Log.d(TAG, "fetchNews: Starting a News fetch");
        executors.networkIO().execute(() -> {

            Call<ArrayList<News>> call = NetworkUtils.getClientInstanceAuth().getNewsList();
            call.enqueue(new Callback<ArrayList<News>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<News>> call,
                                       @NonNull Response<ArrayList<News>> response) {
                    if(response.isSuccessful()){
                        newsArray.postValue(response.body());
                        favorites.postValue(favs);
                        Log.d(TAG, "onResponse: News fetching successful!");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<News>> call,
                                      @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: News fetching failed alv!");
                    t.printStackTrace();
                }
            });

        });
    }

    public void getUserDetails(){
        Log.d(TAG, "getUserDetails: Getting user info");
        if (!NetworkUtils.checkConectivity(context)){
            Toast.makeText(context,
                    context.getResources().getText(R.string.message_no_internet),
                    Toast.LENGTH_LONG).show();
        }
        executors.networkIO().execute(()-> {
            Call<User> call = NetworkUtils.getClientInstanceAuth().getUserDetails();

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call,
                                       @NonNull Response<User> response) {
                    if (response.isSuccessful()){
                        Log.d(TAG, "getUserDetail: onResponse: The response was successful");
                        Log.d(TAG, "onResponse: old favs deleted");
                        SharedData.write(SharedData.KEY_USER_ID, response.body().getId());
                        startFetchNewsService(response.body().getFavoriteNews());
                    } else {
                        switch (response.code()){
                            case 401:
                                Toast.makeText(context,
                                        context.getResources().getText(R.string.message_session_expired),
                                        Toast.LENGTH_LONG).show();
                                SharedData.logOutUser();
                        }
                        Log.d(TAG, "getUserDetail: onResponse: The response failed. code "+response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call,@NonNull  Throwable t) {
                    Toast.makeText(context,
                            context.getResources().getText(R.string.message_net_failure),
                            Toast.LENGTH_LONG).show();
                    Log.d(TAG, "getUserDet: onFailure: the response failed : +"+t.getMessage());
                    t.printStackTrace();
                }
            });
        });
    }

    public void setFavorite(ImageView v, String newid, View rootView){

        Log.d(TAG, "onNewsChecked: FAV CLICKED");
        ImageView icon = v.findViewById(R.id.btn_favorite);

        Gson gson = new GsonBuilder().registerTypeAdapter(
                String.class,
                new MessageDeserializer()
        ).create();

        executors.networkIO().execute(()->{

            GamesServiceInterface gamesService = NetworkUtils.getClientInstanceAuth(gson);

            if(icon.getTag().toString().equalsIgnoreCase("n")){
                Log.d(TAG, "setFavorite: SHAREDPREF - userid:"+
                        SharedData.read(SharedData.KEY_USER_ID,"null"));
                Call<String> response = gamesService.addFavorite(
                        SharedData.read(SharedData.KEY_USER_ID,"null"), newid);
                response.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call,
                                           @NonNull Response<String> response) {
                        if (response.isSuccessful()){
                            Log.d(TAG, "onResponse: Response successful");

                            String[] data = response.body().split(":");
                            if (data[0].equalsIgnoreCase("success")){
                                if(data[1].equalsIgnoreCase("true")){
                                    DependencyContainer.getRepository(context).updateFavorite(newid,true);
                                    icon.setTag("y");
                                    icon.setImageResource(R.drawable.ic_star);
                                    Log.d(TAG, "onResponse: Favorite saved successfully");
                                    Snackbar.make(rootView,
                                            context.getResources().getString(R.string.message_fav_saved),
                                            Snackbar.LENGTH_SHORT).show();
                                } else if(data[1].equalsIgnoreCase("false")){
                                    Log.d(TAG, "onResponse: Favorite was not saved");
                                    Log.d(TAG, "onResponse:"+response.message());
                                    Log.d(TAG, "onResponse:"+response.code());
                                }
                            }
                        } else {
                            Snackbar.make(rootView,
                                    context.getResources().getString(R.string.message_net_failure),
                                    Snackbar.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: Response failed alv - code :"+response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call,@NonNull  Throwable t) {
                        Snackbar.make(rootView,
                                context.getResources().getString(R.string.message_net_failure),
                                Snackbar.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            } else {
                Call<String> response = gamesService.deleteFavorite(
                        SharedData.read(SharedData.KEY_USER_ID,"null"), newid);
                response.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call,
                                           @NonNull Response<String> response) {
                        if (response.isSuccessful()){
                            Log.d(TAG, "onResponse: Response successful");
                            String[] data = response.body().split(":");

                            if (data[0].equalsIgnoreCase("message")){

                                DependencyContainer.getRepository(context).updateFavorite(newid,false);
                                icon.setTag("n");
                                icon.setImageResource(R.drawable.ic_star_border);
                                Snackbar.make(rootView,
                                        context.getResources().getString(R.string.message_fav_deleted),
                                        Snackbar.LENGTH_SHORT).show();

                                Log.d(TAG, "onResponse: Favorite deleted successfully");

                            } else if(data[0].equals("success")) {
                                if (data[1].equals("false")){
                                    Snackbar.make(rootView,
                                            context.getResources().getString(R.string.message_fav_deleted),
                                            Snackbar.LENGTH_SHORT).show();

                                    Log.d(TAG, "onResponse: Favorite was not deleted");
                                    Log.d(TAG, "onResponse:"+response.message());

                                    Log.d(TAG, "onResponse:"+response.code());
                                }
                            }
                        } else {
                            Log.d(TAG, "onResponse: Response failed alv - code :"+response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call,@NonNull  Throwable t) {
                        Toast.makeText(context,
                                context.getResources().getText(R.string.message_net_failure),
                                Toast.LENGTH_LONG).show();
                        Log.d(TAG, "getUserDet: onFailure: the response failed : +"+t.getMessage());
                        t.printStackTrace();
                    }
                });
            }
        });

    }

    public class MessageDeserializer implements JsonDeserializer<String> {
        private static final String TAG = "MessageDeserializer";
        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject logininfo = json.getAsJsonObject();
            if(logininfo != null){
                Log.d(TAG, "deserialize: login info correct");
                if(logininfo.has("token"))
                    return "token:"+logininfo.get("token").getAsString();
                else if(logininfo.has("success")){
                    Log.d(TAG, "deserialize: has member success");
                    return "success:"+logininfo.get("success").getAsString();
                }
                else
                    return "message:"+logininfo.get("message").getAsString();

            } else
                return "message: Operation failed. Please try again later";

        }
    }
}