package com.finalproject.idlegame;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.finalproject.idlegame.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private static double mMoneyValue;
    private static double mFactoriesValue;

    private static String[] sAllValuesName = new String[]{
            "money",
            "factories"
    };

    private static final String TAG = "MainActivity";
    private static final String INTENT_MUSIC_PAUSE = "com.finalproject.idlegame.BackgroundMusicService.PAUSE";

    private static double[] mGameDataDouble;

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

        //try {
        //    mGameContentOps.deleteByName(sAllValuesName);
        //} catch (RemoteException e) {
        //    e.printStackTrace();
        //}

        //This checks if there is data already, if there is this part is ignored.
        try {
            if(!mGameContentOps.areThereValuesInTable(sAllValuesName)){
                Log.d(TAG, "New game detected.");
                mGameContentOps.insertHelper("money", 9);
                mGameContentOps.insertHelper("factories", 10);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //Collects data from the table so that it remembers your game even when you close the app.
        try {
            mGameDataDouble = mGameContentOps.getValueFromTable(sAllValuesName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        mMoneyValue = mGameDataDouble[0];
        mFactoriesValue = mGameDataDouble[1];
        Log.d(TAG, "money found: " +mMoneyValue+ " factories found: " +mFactoriesValue);

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

    // This checks if a service is running, one method is deprecated but still works for local services.
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
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    public double getMoneyValue(){
        return mMoneyValue;
    }

    public double getFactoriesValue(){
        return mFactoriesValue;
    }

    public void saveGame(Double moneyValue, Double factoriesValue){
        try {
            Toast.makeText(this, "Game saving...", Toast.LENGTH_SHORT).show();
            mGameContentOps.updateByUri(GameDatabaseHelper.GameEntry.CONTENT_URI, "money", moneyValue.intValue());
            mGameContentOps.updateByUri(GameDatabaseHelper.GameEntry.CONTENT_URI, "factories", factoriesValue.intValue());
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Game saved", Toast.LENGTH_SHORT).show();
        }
    }
}