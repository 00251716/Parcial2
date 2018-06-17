package com.example.kevin.parcial2.Activities;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.kevin.parcial2.ModelsAndEntities.User;
import com.example.kevin.parcial2.Fragments.ShowNewsFragment;
import com.example.kevin.parcial2.GameNewsAPI.GamesService;
import com.example.kevin.parcial2.ModelsAndEntities.News;
import com.example.kevin.parcial2.Data.SharedData;
import com.example.kevin.parcial2.R;
import com.example.kevin.parcial2.ViewModels.NewsViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final int NEW_NEWS_ACTIVITY_REQUEST_CODE = 1;

    private NewsViewModel mNewsViewModel;
    static String token;
    private NavigationView navigationView;
    private TextView navHeaderUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedData.init(this);
        token = SharedData.getToken();

        navigationView =  findViewById(R.id.nav_view);
        navHeaderUsername = navigationView.getHeaderView(0).findViewById(R.id.usernameTextView);

        if (!SharedData.hasUserDetail()) {
            requestUserDetail();
        } else {
            navHeaderUsername.setText(SharedData.getUsername());
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewsInfoActivity.class);
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
            setFragment(0);
            //getIntent().getStringExtra("TOKEN_OBTAINED");
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

        } else if (id == R.id.nav_favorites) {
            SharedData.logOutUser();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void requestUserDetail() {
        Call<User> userCall = GamesService.getApiServiceWithAuthorization().userDetail();
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && !response.body().getId().isEmpty()) {
                    SharedData.setUserDetail(response.body());
                    navHeaderUsername.setText(response.body().getUser());
                } else {
                    Log.e("USER: ", "onResponse: empty user id");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("USER: ", "onFailure: Cannot load user info. Error message: " + t.getMessage() );
            }
        });
    }

}
