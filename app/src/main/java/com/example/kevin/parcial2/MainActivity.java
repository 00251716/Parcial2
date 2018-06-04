package com.example.kevin.parcial2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kevin.parcial2.GameNewsAPI.NewsService;
import com.example.kevin.parcial2.Models.News;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://gamenewsuca.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        populateNews();

    }

    private void populateNews() {
        NewsService service = retrofit.create(NewsService.class);
        final Call<ArrayList<News>> newsResponseCall = service.getNewsList("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1YjBmZmNjYWM4NDcxYTAwMjAxNGMzMGEiLCJpYXQiOjE1MjgwNzQ5ODMsImV4cCI6MTUyOTI4NDU4M30.jWgzv4UuYpFf4rG5vvAbZOHoJd6_zMqGMfhkOglDCgM");

        newsResponseCall.enqueue(new Callback<ArrayList<News>>() {
            @Override
            public void onResponse(Call<ArrayList<News>> call, Response<ArrayList<News>> response) {

                if(response.isSuccessful()){

                    ArrayList<News> newsResponse = response.body();
                    //ArrayList<News> newsList = newsResponse.getResults();

                    //listaPokemonAdapter.adicionarListaPokemon(listaPokemon);

                    for(int i = 0; i < newsResponse.size() ; i++){
                        News n = newsResponse.get(i);
                        Log.i("FUCKING NEWS", "Noticia: " + n.getGame());
                    }

                } else {

                    Log.e("No funcion� la mierda", "onResponse: "+ response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<News>> call, Throwable t) {
                //aptoParaCargar = true;
                Log.e("NO", "onFailure: "+ t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {
            // Handle the camera action
        } else if (id == R.id.nav_games) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_favorites) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
