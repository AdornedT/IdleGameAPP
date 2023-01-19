package com.finalproject.idlegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.finalproject.idlegame.databinding.FragmentFirstBinding;;import java.util.Timer;
import java.util.TimerTask;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Intent mIntent;
    private static GameContentOps mGameContentOps;

    private static final String TAG = "FirstFragment";
    private static final String INTENT_MUSIC_PAUSE = "com.finalproject.idlegame.BackgroundMusicService.PAUSE";

    private static Double mWaterBottleSellPrice = 1.0;
    private static Double mMoneyValue = 0.0;
    private static Double mFactoriesValue = 0.0;
    private static Double mUpgradesBought = 0.0;
    private static Double mFactoryCurrentCost = 10.0;
    private static Double mFactoryIncreaseCost = 10.0;

    private static final Double[] mUpgradePrice = {20.0, 40.0, 80.0, 160.0};

    Timer mGameTimer = new Timer();

    private static boolean[] mUpgradeBoughtStatus = {false, false, false};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mIntent = new Intent(getActivity(), BackgroundMusicService.class);

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Play music button
        binding.buttonMusicPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Play music");
                getActivity().startService(mIntent);
            }
        });

        //Pause music button
        binding.buttonMusicPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send intent to service to pause music
                Log.d(TAG, "Pause music");
                Intent broadcastIntent = new Intent(INTENT_MUSIC_PAUSE);

                try{
                    getActivity().sendBroadcast(broadcastIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Save button
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).saveGameThread(mMoneyValue, mFactoriesValue, mUpgradesBought);
            }
        });

        //Bottle Water button
        binding.buttonBottleWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyValue += mWaterBottleSellPrice;
                ChangeMoneyText();
            }
        });

        //Buy Factory button
        binding.buttonBuyFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMoneyValue >= mFactoryCurrentCost){
                    mMoneyValue -= mFactoryCurrentCost;
                    mFactoryCurrentCost = mFactoryCurrentCost + mFactoryIncreaseCost;
                    mFactoriesValue++;
                    ChangeMoneyText();
                }
                else{
                    ((MainActivity)getActivity()).toastMessages("Not enough money");
                }
                ChangeFactoriesText();
            }
        });

        //Buy Factory in upgrades button
        binding.buttonBuyFactoryUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMoneyValue >= mFactoryCurrentCost){
                    mMoneyValue -= mFactoryCurrentCost;
                    mFactoryCurrentCost = mFactoryCurrentCost + mFactoryIncreaseCost;
                    mFactoriesValue++;
                    ChangeMoneyText();
                }
                else{
                    ((MainActivity)getActivity()).toastMessages("Not enough money");
                }
                ChangeFactoriesText();
            }
        });

        //Reset button
        binding.buttonResetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).resetGame();
                mMoneyValue = 0.0;
                mFactoriesValue = 0.0;
                mUpgradesBought = 0.0;
                int i = 0;
                for (boolean aux: mUpgradeBoughtStatus) {
                    mUpgradeBoughtStatus[i] = false;
                    i++;
                }
                ChangeMoneyText();
                ChangeFactoriesText();
            }
        });

        //Buy Mountain Water Upgrade button
        binding.buttonMountainWaterUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mUpgradeBoughtStatus[0]) {
                    if(mMoneyValue >= mUpgradePrice[0]){
                        mMoneyValue -= mUpgradePrice[0];
                        mWaterBottleSellPrice *= 2;
                        mUpgradeBoughtStatus[0] = true;
                        mUpgradesBought = 1.0;
                        ChangeMoneyText();
                    }
                    else{
                        ((MainActivity)getActivity()).toastMessages("Cost: $"+mUpgradePrice[0]);
                    }
                } else{
                    ((MainActivity)getActivity()).toastMessages("You already have this upgrade.");
                }
                ChangeFactoriesText();
            }
        });

        //Buy Spicy Water Upgrade button
        binding.buttonSpicyWaterUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mUpgradeBoughtStatus[1]) {
                    if(mUpgradeBoughtStatus[0]) {
                        if (mMoneyValue >= mUpgradePrice[1]) {
                            mMoneyValue -= mUpgradePrice[1];
                            mWaterBottleSellPrice *= 2;
                            mUpgradeBoughtStatus[1] = true;
                            mUpgradesBought = 2.0;
                            ChangeMoneyText();
                        } else {
                            ((MainActivity) getActivity()).toastMessages("Cost: $" + mUpgradePrice[1]);
                        }
                    }else{
                        ((MainActivity)getActivity()).toastMessages("You must purchase the upgrade before this one.");
                    }
                } else{
                    ((MainActivity)getActivity()).toastMessages("You already have this upgrade.");
                }
                ChangeFactoriesText();
            }
        });

        //Buy Divine Water Upgrade button
        binding.buttonDivineWaterUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mUpgradeBoughtStatus[2]) {
                    if(mUpgradeBoughtStatus[1]) {
                        if (mMoneyValue >= mUpgradePrice[2]) {
                            mMoneyValue -= mUpgradePrice[2];
                            mWaterBottleSellPrice *= 2;
                            mUpgradeBoughtStatus[2] = true;
                            mUpgradesBought = 3.0;
                            ChangeMoneyText();
                        } else {
                            ((MainActivity) getActivity()).toastMessages("Cost: $" + mUpgradePrice[2]);
                        }
                    }else{
                        ((MainActivity)getActivity()).toastMessages("You must purchase the upgrade before this one.");
                    }
                }else{
                    ((MainActivity)getActivity()).toastMessages("You already have this upgrade.");
                }
                ChangeFactoriesText();
            }
        });

        //Start button
        binding.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWaterBottleSellPrice = ((MainActivity)getActivity()).getWaterValueHttp();
                mMoneyValue = ((MainActivity)getActivity()).getMoneyValue();
                mFactoriesValue = ((MainActivity)getActivity()).getFactoriesValue();
                mFactoryCurrentCost = ((MainActivity)getActivity()).getFactoriesCurrentCost();
                mUpgradesBought = ((MainActivity)getActivity()).getUpgradesPrice();
                Log.d(TAG, "money found: " +mMoneyValue+ " factories found: " +mFactoriesValue+
                        " current factory cost: " +mFactoryCurrentCost+ " Upgrades bought status " +mUpgradesBought+
                        " water bottle price: " +mWaterBottleSellPrice);

                //Starts game timer to loop every 1 second, used for the factories.
                startGameTimer();

                //Mountain water upgrade status
                if(mUpgradesBought >= 1){
                    mUpgradeBoughtStatus[0] = true;
                    mWaterBottleSellPrice *= 2;
                    //Spicy water upgrade status
                    if(mUpgradesBought >= 2){
                        mUpgradeBoughtStatus[1] = true;
                        mWaterBottleSellPrice *= 2;
                        //Divine water upgrade status
                        if(mUpgradesBought >= 3){
                            mUpgradeBoughtStatus[2] = true;
                            mWaterBottleSellPrice *= 2;
                        }
                    }
                }

                ChangeMoneyText();
                ChangeFactoriesText();

                view.setVisibility(View.GONE);

                final TextView bottle_water_button = (TextView) getView().findViewById(R.id.button_bottleWater);
                final TextView music_play_button = (TextView) getView().findViewById(R.id.button_musicPlay);
                final TextView music_pause_button = (TextView) getView().findViewById(R.id.button_musicPause);
                final TextView buy_factories_button = (TextView) getView().findViewById(R.id.button_buyFactory);
                final TextView upgrades_button = (TextView) getView().findViewById(R.id.button_upgrades);
                final TextView save_button = (TextView) getView().findViewById(R.id.button_save);
                final TextView reset_button = (TextView) getView().findViewById(R.id.button_reset_game);

                bottle_water_button.setVisibility(View.VISIBLE);
                music_play_button.setVisibility(View.VISIBLE);
                music_pause_button.setVisibility(View.VISIBLE);
                buy_factories_button.setVisibility(View.VISIBLE);
                upgrades_button.setVisibility(View.VISIBLE);
                save_button.setVisibility(View.VISIBLE);
                reset_button.setVisibility(View.VISIBLE);

                final TextView music_text = (TextView) getView().findViewById(R.id.text_music);
                final TextView money_text = (TextView) getView().findViewById(R.id.textView_money);
                final TextView money_value_text = (TextView) getView().findViewById(R.id.textView_moneyValue);
                final TextView factories_text = (TextView) getView().findViewById(R.id.textView_factories);
                final TextView factories_value_text = (TextView) getView().findViewById(R.id.textView_factoriesNumber);

                music_text.setVisibility(View.VISIBLE);
                money_text.setVisibility(View.VISIBLE);
                money_value_text.setVisibility(View.VISIBLE);
                factories_text.setVisibility(View.VISIBLE);
                factories_value_text.setVisibility(View.VISIBLE);
            }
        });

        //Upgrades button
        binding.buttonUpgrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeMoneyText();
                ChangeFactoriesText();

                //Main game screen
                final TextView bottle_water_button = (TextView) getView().findViewById(R.id.button_bottleWater);
                final TextView music_play_button = (TextView) getView().findViewById(R.id.button_musicPlay);
                final TextView music_pause_button = (TextView) getView().findViewById(R.id.button_musicPause);
                final TextView buy_factories_button = (TextView) getView().findViewById(R.id.button_buyFactory);
                final TextView upgrades_button = (TextView) getView().findViewById(R.id.button_upgrades);
                final TextView save_button = (TextView) getView().findViewById(R.id.button_save);
                final TextView reset_button = (TextView) getView().findViewById(R.id.button_reset_game);

                bottle_water_button.setVisibility(View.GONE);
                music_play_button.setVisibility(View.GONE);
                music_pause_button.setVisibility(View.GONE);
                buy_factories_button.setVisibility(View.GONE);
                upgrades_button.setVisibility(View.GONE);
                save_button.setVisibility(View.GONE);
                reset_button.setVisibility(View.GONE);

                final TextView music_text = (TextView) getView().findViewById(R.id.text_music);
                final TextView money_text = (TextView) getView().findViewById(R.id.textView_money);
                final TextView money_value_text = (TextView) getView().findViewById(R.id.textView_moneyValue);
                final TextView factories_text = (TextView) getView().findViewById(R.id.textView_factories);
                final TextView factories_value_text = (TextView) getView().findViewById(R.id.textView_factoriesNumber);

                music_text.setVisibility(View.GONE);
                money_text.setVisibility(View.GONE);
                money_value_text.setVisibility(View.GONE);
                factories_text.setVisibility(View.GONE);
                factories_value_text.setVisibility(View.GONE);

                //Upgrade screen
                final TextView buy_factory_upgrades_button = (TextView) getView().findViewById(R.id.button_buyFactory_upgrade);
                final TextView mountain_water_button = (TextView) getView().findViewById(R.id.button_mountainWater_upgrade);
                final TextView spicy_water_button = (TextView) getView().findViewById(R.id.button_spicyWater_upgrade);
                final TextView divine_water_button = (TextView) getView().findViewById(R.id.button_divineWater_upgrade);
                final TextView back_button = (TextView) getView().findViewById(R.id.button_back);

                buy_factory_upgrades_button.setVisibility(View.VISIBLE);
                mountain_water_button.setVisibility(View.VISIBLE);
                spicy_water_button.setVisibility(View.VISIBLE);
                divine_water_button.setVisibility(View.VISIBLE);
                back_button.setVisibility(View.VISIBLE);

                final TextView money_text_upgrade = (TextView) getView().findViewById(R.id.textView_money_upgrade);
                final TextView money_value_upgrade = (TextView) getView().findViewById(R.id.textView_moneyValue_upgrade);
                final TextView factories_text_upgrade = (TextView) getView().findViewById(R.id.textView_factories_upgrade);
                final TextView factories_value_upgrade = (TextView) getView().findViewById(R.id.textView_factoriesNumber_upgrade);

                money_text_upgrade.setVisibility(View.VISIBLE);
                money_value_upgrade.setVisibility(View.VISIBLE);
                factories_text_upgrade.setVisibility(View.VISIBLE);
                factories_value_upgrade.setVisibility(View.VISIBLE);
            }
        });

        //Back button
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeMoneyText();
                ChangeFactoriesText();

                //Main game screen
                final TextView bottle_water_button = (TextView) getView().findViewById(R.id.button_bottleWater);
                final TextView music_play_button = (TextView) getView().findViewById(R.id.button_musicPlay);
                final TextView music_pause_button = (TextView) getView().findViewById(R.id.button_musicPause);
                final TextView buy_factories_button = (TextView) getView().findViewById(R.id.button_buyFactory);
                final TextView upgrades_button = (TextView) getView().findViewById(R.id.button_upgrades);
                final TextView save_button = (TextView) getView().findViewById(R.id.button_save);
                final TextView reset_button = (TextView) getView().findViewById(R.id.button_reset_game);

                bottle_water_button.setVisibility(View.VISIBLE);
                music_play_button.setVisibility(View.VISIBLE);
                music_pause_button.setVisibility(View.VISIBLE);
                buy_factories_button.setVisibility(View.VISIBLE);
                upgrades_button.setVisibility(View.VISIBLE);
                save_button.setVisibility(View.VISIBLE);
                reset_button.setVisibility(View.VISIBLE);

                final TextView music_text = (TextView) getView().findViewById(R.id.text_music);
                final TextView money_text = (TextView) getView().findViewById(R.id.textView_money);
                final TextView money_value_text = (TextView) getView().findViewById(R.id.textView_moneyValue);
                final TextView factories_text = (TextView) getView().findViewById(R.id.textView_factories);
                final TextView factories_value_text = (TextView) getView().findViewById(R.id.textView_factoriesNumber);

                music_text.setVisibility(View.VISIBLE);
                money_text.setVisibility(View.VISIBLE);
                money_value_text.setVisibility(View.VISIBLE);
                factories_text.setVisibility(View.VISIBLE);
                factories_value_text.setVisibility(View.VISIBLE);

                //Upgrade screen
                final TextView buy_factory_upgrades_button = (TextView) getView().findViewById(R.id.button_buyFactory_upgrade);
                final TextView mountain_water_button = (TextView) getView().findViewById(R.id.button_mountainWater_upgrade);
                final TextView spicy_water_button = (TextView) getView().findViewById(R.id.button_spicyWater_upgrade);
                final TextView divine_water_button = (TextView) getView().findViewById(R.id.button_divineWater_upgrade);
                final TextView back_button = (TextView) getView().findViewById(R.id.button_back);

                buy_factory_upgrades_button.setVisibility(View.GONE);
                mountain_water_button.setVisibility(View.GONE);
                spicy_water_button.setVisibility(View.GONE);
                divine_water_button.setVisibility(View.GONE);
                back_button.setVisibility(View.GONE);

                final TextView money_text_upgrade = (TextView) getView().findViewById(R.id.textView_money_upgrade);
                final TextView money_value_upgrade = (TextView) getView().findViewById(R.id.textView_moneyValue_upgrade);
                final TextView factories_text_upgrade = (TextView) getView().findViewById(R.id.textView_factories_upgrade);
                final TextView factories_value_upgrade = (TextView) getView().findViewById(R.id.textView_factoriesNumber_upgrade);

                money_text_upgrade.setVisibility(View.GONE);
                money_value_upgrade.setVisibility(View.GONE);
                factories_text_upgrade.setVisibility(View.GONE);
                factories_value_upgrade.setVisibility(View.GONE);
            }
        });
    }

    public void ChangeMoneyText(){
        final TextView money_num_txt = (TextView) getView().findViewById(R.id.textView_moneyValue);
        final TextView money_num_upgrade_txt = (TextView) getView().findViewById(R.id.textView_moneyValue_upgrade);
        money_num_txt.setText(mMoneyValue.toString());
        money_num_upgrade_txt.setText(mMoneyValue.toString());
    }

    public void ChangeFactoriesText(){
        final TextView factories_num_txt = (TextView) getView().findViewById(R.id.textView_factoriesNumber);
        final TextView factories_num_upgrade_txt = (TextView) getView().findViewById(R.id.textView_factoriesNumber_upgrade);
        factories_num_txt.setText(mFactoriesValue.toString());
        factories_num_upgrade_txt.setText(mFactoriesValue.toString());
    }

    class GameTimerThread extends TimerTask {

        @Override
        public void run() {
            ((MainActivity)getActivity()).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    FactoriesProfit();
                }
            });
        }
    };

    public void startGameTimer(){
        mGameTimer.schedule(new GameTimerThread(), 0,1000);
    }

    public void FactoriesProfit(){
        mMoneyValue += mWaterBottleSellPrice*mFactoriesValue;
        ChangeMoneyText();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "I am being destroyed");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "Stopping");
    }

}