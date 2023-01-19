package com.finalproject.idlegame;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private static HttpURLConnectionActivity mHttpRequester;

    private static double mMoneyValue;
    private static double mFactoriesValue;
    private static double mFactoriesCurrentCost;
    private static double mUpgradesBought;

    private static String[] sAllValuesName = new String[]{
            "money",
            "factories",
            "bought_upgrades"
    };

    private static String mWaterValueHttp;

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

        mHttpRequester = new HttpURLConnectionActivity();

        mWaterValueHttp = HttpURLConnectionActivity.startSendHttpRequestThread("https://www.globalproductprices.com/USA/mineral_water_prices/#").split("<")[0];

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        mGameContentOps = new GameContentOps(this);

        //This checks if there is data already, if there is this part is ignored.
        try {
            if(!mGameContentOps.areThereValuesInTable(sAllValuesName)){
                Log.d(TAG, "New game detected.");
                mGameContentOps.insertHelper("money", 0);
                mGameContentOps.insertHelper("factories", 0);
                mGameContentOps.insertHelper("bought_upgrades", 0);
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

        mMoneyValue = mGameDataDouble[0]/100;
        mFactoriesValue = mGameDataDouble[1];
        mFactoriesCurrentCost = mFactoriesValue*10 + 10;
        mUpgradesBought = mGameDataDouble[2];
        for (double aux: mGameDataDouble) {
            Log.d(TAG, "double: " + aux);
        }
    }

    //Resets game
    public void resetGame(){
        saveGameThread(0.0, 0.0, 0.0);
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

    public double getFactoriesCurrentCost(){
        return mFactoriesCurrentCost;
    }

    public double getUpgradesPrice(){
        return mUpgradesBought;
    }

    public double getWaterValueHttp(){
        return Double.parseDouble(mWaterValueHttp);
    }

    class SaveThread extends Thread{
        private Double mMoneyValue;
        private Double mFactoriesValue;
        private Double mUpgradesBought;

        SaveThread(Double moneyValue, Double factoriesValue, Double upgradesBought){
            this.mMoneyValue = moneyValue;
            this.mFactoriesValue = factoriesValue;
            this.mUpgradesBought = upgradesBought;
        }

        @Override
        public void run(){
            saveGame(mMoneyValue, mFactoriesValue, mUpgradesBought);
        }
    }

    public void saveGameThread(Double moneyValue, Double factoriesValue, Double upgradesBought){
        Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
        SaveThread save = new SaveThread(moneyValue, factoriesValue, upgradesBought);
        new Thread(save).start();
        //This is waiting for the thread to finish
        while(save.isAlive()){
            //waiting...
        }
        Toast.makeText(this, "Game saved", Toast.LENGTH_SHORT).show();
    }

    private void saveGame(Double moneyValue, Double factoriesValue, Double upgradesBought){
        moneyValue *= 100;
        try {
            mGameContentOps.updateValueByName("money", moneyValue.intValue());
            mGameContentOps.updateValueByName("factories", factoriesValue.intValue());
            mGameContentOps.updateValueByName("bought_upgrades", upgradesBought.intValue());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void toastMessages(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}