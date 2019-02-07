package com.anie.dara.nontonfilm;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.anie.dara.nontonfilm.fragment.FavoriteFragment;
import com.anie.dara.nontonfilm.fragment.NowPlayingFragment;
import com.anie.dara.nontonfilm.fragment.SearchFragment;
import com.anie.dara.nontonfilm.fragment.UpComingFragment;
import com.anie.dara.nontonfilm.scheduler.SettingScheduleActivity;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static String apiKey = BuildConfig.TMDB_API_KEY;
    public static String imageUrl = BuildConfig.IMAGE_URL;
    public static String baseUrl = BuildConfig.BASE_URL;

    private Fragment pageContent = new NowPlayingFragment();
    private String title = "Home";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            loadFragment(pageContent);
            Log.d("fragment", String.valueOf(pageContent));
        } else {
            pageContent = getSupportFragmentManager().getFragment(savedInstanceState, "KEY_FRAGMENT");
            Log.d("fragment", String.valueOf(pageContent));
           loadFragment(pageContent);
        }
    }

    private boolean loadFragment(android.support.v4.app.Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_bahasa :
                Intent settingBahasa = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(settingBahasa);
            case R.id.menu_schedule :
                Intent settingSchedule = new Intent(MainActivity.this, SettingScheduleActivity.class);
                startActivity(settingSchedule);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case   R.id.menu_upcoming :
                pageContent = new UpComingFragment();
                title = "UpComing";
                break;
            case  R.id.menu_now :
                pageContent = new NowPlayingFragment();
                title = "NowPlaying";
                break;
            case  R.id.menu_search :
                pageContent = new SearchFragment();
                title = "Search";
                break;
            case  R.id.menu_favorit :
                pageContent = new FavoriteFragment();
                title = "Favorite";
                break;
        }
        loadFragment(pageContent);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        getSupportFragmentManager().putFragment(outState, "KEY_FRAGMENT" , pageContent);
        Log.d("fragment", pageContent.getTag());
        super.onSaveInstanceState(outState);
    }
}

