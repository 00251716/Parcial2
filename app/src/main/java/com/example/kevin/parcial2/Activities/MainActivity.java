package com.example.kevin.parcial2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

    private static final String TAG = "MainActivity";
    private Fragment contentFragment;
    private FragmentManager fragmentManager;
    TextView lblUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedData.init(getApplicationContext());
        Log.d(TAG, "onCreate: Checking login");
        if(SharedData.checkLogin()){
            finishAffinity();
            //finish();
            Log.d(TAG, "onCreate: No login");
        }
        findViews();

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey("content")){
                String content = savedInstanceState.getString("content");
                if (content.equals("news") && fragmentManager.findFragmentByTag("news")!=null){
                    contentFragment = fragmentManager.findFragmentByTag("news");
                }
            }
        } else {
            ShowNewsFragment newsFragment = new ShowNewsFragment();
            setTitle(R.string.app_name);
            switchContent(newsFragment,"news");
        }


        /*SharedPreferencesUtils.init(this);
        if (SharedPreferencesUtils.hasUserDetail())
            navTextUsername.setText(SharedPreferencesUtils.getUsername());*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (contentFragment instanceof ShowNewsFragment)
            outState.putString("content", "news");
        super.onSaveInstanceState(outState);
    }



    void findViews(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lblUser = navigationView.getHeaderView(0).findViewById(R.id.usernameTextView);
        lblUser.setText(SharedData.read(SharedData.getUsername(),null));
    }

    public void navigate(){
        if (fragmentManager.getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else if (contentFragment instanceof ShowNewsFragment
                || fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        }
    }

    public void switchContent(Fragment fragment, String tag) {


        if (fragment != null){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //transaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up,
            //      R.anim.slide_in_down, R.anim.slide_out_up);
            transaction.replace(R.id.main_container, fragment, tag);

            if(!(fragment instanceof ShowNewsFragment)){
                transaction.addToBackStack(tag);
            }
            transaction.commit();
            contentFragment = fragment;
        }
    }

    public void setTitle(int resource){
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getResources().getString(resource));
    }

    /* Navigation Drawer Methods */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            navigate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            SharedData.logOutUser();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch(id){
            case R.id.nav_favorites:
                SharedData.logOutUser();
                finish();
            default:
                ;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
