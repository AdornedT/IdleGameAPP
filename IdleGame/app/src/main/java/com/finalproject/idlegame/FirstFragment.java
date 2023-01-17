package com.finalproject.idlegame;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.finalproject.idlegame.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Intent mIntent;
    private static GameContentOps mGameContentOps;

    private static final String TAG = "FirstFragment";
    private static final String INTENT_MUSIC_PAUSE = "com.finalproject.idlegame.BackgroundMusicService.PAUSE";


    private static Double mMoneyValue = 0.0;
    private static Double mFactoriesValue = 0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mIntent = new Intent(getActivity(), BackgroundMusicService.class);

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Should be removed at some point
        binding.buttonUpgrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

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
                ((MainActivity)getActivity()).saveGame(mMoneyValue, mFactoriesValue);
            }
        });

        //Bottle Water button
        binding.buttonBottleWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyValue++;
                changeMoneyText();
            }
        });

        //Buy Factory button
        binding.buttonBuyFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFactoriesValue++;
                changeFactoriesText();
            }
        });

        //Start button
        binding.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoneyValue = ((MainActivity)getActivity()).getMoneyValue();
                mFactoriesValue = ((MainActivity)getActivity()).getFactoriesValue();
                Log.d(TAG, "money found: " +mMoneyValue+ " factories found: " +mFactoriesValue);
                changeMoneyText();
                changeFactoriesText();

                view.setVisibility(View.GONE);

                final TextView bottle_water_button = (TextView) getView().findViewById(R.id.button_bottleWater);
                final TextView music_play_button = (TextView) getView().findViewById(R.id.button_musicPlay);
                final TextView music_pause_button = (TextView) getView().findViewById(R.id.button_musicPause);
                final TextView buy_factories_button = (TextView) getView().findViewById(R.id.button_buyFactory);
                final TextView upgrades_button = (TextView) getView().findViewById(R.id.button_upgrades);
                final TextView save_button = (TextView) getView().findViewById(R.id.button_save);

                bottle_water_button.setVisibility(View.VISIBLE);
                music_play_button.setVisibility(View.VISIBLE);
                music_pause_button.setVisibility(View.VISIBLE);
                buy_factories_button.setVisibility(View.VISIBLE);
                upgrades_button.setVisibility(View.VISIBLE);
                save_button.setVisibility(View.VISIBLE);

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
    }

    public void changeMoneyText(){
        final TextView money_num_txt = (TextView) getView().findViewById(R.id.textView_moneyValue);
        money_num_txt.setText(mMoneyValue.toString());
    }

    public void changeFactoriesText(){
        final TextView factories_num_txt = (TextView) getView().findViewById(R.id.textView_factoriesNumber);
        factories_num_txt.setText(mFactoriesValue.toString());
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