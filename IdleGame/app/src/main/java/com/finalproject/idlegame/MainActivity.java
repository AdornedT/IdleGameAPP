package com.finalproject.idlegame;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.RemoteException;
import android.util.Log;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.finalproject.idlegame.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private static String[] sAllValuesName = new String[]{
            "money",
            "factories"
    };

    private static final String TAG = "MainActivity";
    private static final String INTENT_MUSIC_PAUSE = "com.finalproject.idlegame.BackgroundMusicService.PAUSE";

    private static GameContentOps mGameContentOps;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        mGameContentOps = new GameContentOps(this);
        try {
            mGameContentOps.insertHelper("money", "a");
            mGameContentOps.insertHelper("factories", "b");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            mGameContentOps.getValueFromTable(sAllValuesName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            mGameContentOps.deleteByName(sAllValuesName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        /**binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                if(isMyServiceRunning(BackgroundMusicService.class)){
                    Log.d(TAG, "Service is running");
                }else{
                    Log.d(TAG, "Service is not running");
                }
            }
        }); **/
    }

    public static void testingContentTableGet(){
        try {
            mGameContentOps.getValueFromTable(sAllValuesName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // This checks if a service is running, one method is deprecated but still works for local.
    @SuppressWarnings("deprecation")
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(this.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}