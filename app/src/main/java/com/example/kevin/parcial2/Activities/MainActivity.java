package com.example.kevin.parcial2.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.example.kevin.parcial2.Fragments.ShowNewsFragment;
import com.example.kevin.parcial2.GameNewsAPI.GamesService;
import com.example.kevin.parcial2.Entities.News;
import com.example.kevin.parcial2.R;
import com.example.kevin.parcial2.ViewModels.NewsViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final int NEW_NEWS_ACTIVITY_REQUEST_CODE = 1;

    private NewsViewModel mNewsViewModel;
    static Retrofit retrofit;
    static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ---------------- Cosas del ViewModel --------------
        mNewsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        // ---------------- Cosas del ViewModel --------------


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewNewsActivity.class);
                startActivityForResult(intent, NEW_NEWS_ACTIVITY_REQUEST_CODE);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://gamenewsuca.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            populateNews(mNewsViewModel);
            setFragment(0);
            token = getIntent().getStringExtra("TOKEN_OBTAINED");
        }
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NEWS_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            News news = new News(data.getStringExtra(NewNewsActivity.EXTRA_REPLY));
            mNewsViewModel.insert(news);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    //Método para cambiar entre fragmentos de acuerdo a la opción seleccionada en el navigation drawer
    public void setFragment(int position) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;

        switch (position) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                ShowNewsFragment showFragment = new ShowNewsFragment();
                fragmentTransaction.replace(R.id.fragment, showFragment);
                fragmentTransaction.commit();
                break;


        }
    }



    static private void populateNews(final NewsViewModel newsViewModel) {
        GamesService service = retrofit.create(GamesService.class);
        final Call<ArrayList<News>> newsResponseCall = service.getNewsList("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1YjBmZmNjYWM4NDcxYTAwMjAxNGMzMGEiLCJpYXQiOjE1MjgxMzM1NzgsImV4cCI6MTUyOTM0MzE3OH0.QJQmdMoxUh8PBKK6rWtd-SRsXTO6AC1UQ5FweDv0J8Y");

        newsResponseCall.enqueue(new retrofit2.Callback<ArrayList<News>>() {
            @Override
            public void onResponse(Call<ArrayList<News>> call, Response<ArrayList<News>> response) {

                if(response.isSuccessful()){

                    ArrayList<News> newsResponse = response.body();
                    //ArrayList<News> newsList = newsResponse.getResults();

                    //adapter.setNews(newsResponse); est� bueno, pero aqu� solo lo añadis al adapter
                    newsViewModel.insertAll(newsResponse);

                    for(int i = 0; i < newsResponse.size() ; i++){
                        News n = newsResponse.get(i);
                        Log.i("FUCKING NEWS", "Noticia: " + token);
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

}
