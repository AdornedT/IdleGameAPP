package com.finalproject.idlegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.finalproject.idlegame.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Intent mIntent;

    private static final String TAG = "FirstFragment";
    private static final String INTENT_MUSIC_PAUSE = "com.finalproject.idlegame.BackgroundMusicService.PAUSE";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        mIntent = new Intent(getActivity(), BackgroundMusicService.class);

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Should be removed at some point
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}