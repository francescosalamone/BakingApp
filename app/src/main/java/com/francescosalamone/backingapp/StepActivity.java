package com.francescosalamone.backingapp;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.francescosalamone.backingapp.Fragment.ExoPlayerFragment;
import com.francescosalamone.backingapp.Fragment.StepDetailFragment;

public class StepActivity extends AppCompatActivity implements StepDetailFragment.OnVideoUrlListener,
StepDetailFragment.OnStartFullScreenListener{

    //private ExoPlayerFragment exoPlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        int containerId = R.id.step_detail_container;

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment existOldFragment = getSupportFragmentManager().findFragmentById(containerId);
        if(existOldFragment == null || !existOldFragment.getClass().equals(stepDetailFragment.getClass())) {
            fragmentManager.beginTransaction()
                    .add(containerId, stepDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onVideoUrlChanged(String url, ExoPlayerFragment exoPlayerFragment) {
        if(exoPlayerFragment != null){
            exoPlayerFragment.setMediaUrl(url);
        }
    }

    @Override
    public long onStartFullScreen(ExoPlayerFragment exoPlayerFragment) {
        if(exoPlayerFragment != null){
            return exoPlayerFragment.getCurrentPosition();
        }
        return 0L;
    }
}
