package com.francescosalamone.backingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.francescosalamone.backingapp.Fragment.ExoPlayerFragment;
import com.francescosalamone.backingapp.Fragment.RecipeDetailFragment;
import com.francescosalamone.backingapp.Fragment.StepDetailFragment;
import com.francescosalamone.backingapp.Model.Steps;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepClickListener,
        StepDetailFragment.OnVideoUrlListener, StepDetailFragment.OnStartFullScreenListener,
        StepDetailFragment.OnVideoStatusPlayListener{

    public static final String ITEM_STEPS = "steps";
    public static final String ITEM_POSITION = "position";
    private StepDetailFragment stepDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        int containerId = R.id.recipe_detail_container;

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment existOldFragment = getSupportFragmentManager().findFragmentById(containerId);
        if (existOldFragment == null || !existOldFragment.getClass().equals(recipeDetailFragment.getClass())) {
            fragmentManager.beginTransaction()
                    .add(containerId, recipeDetailFragment)
                    .commit();
        }

        if(getResources().getBoolean(R.bool.isTablet)){
            stepDetailFragment = new StepDetailFragment();
            int stepContainerId = R.id.step_detail_container;

            Fragment existOldStepFragment = getSupportFragmentManager().findFragmentById(stepContainerId);
            if(existOldStepFragment == null || !existOldStepFragment.getClass().equals(stepDetailFragment.getClass())){
                fragmentManager.beginTransaction()
                        .add(stepContainerId, stepDetailFragment)
                        .commit();
            } else {
                stepDetailFragment = (StepDetailFragment) existOldStepFragment;
            }
        }

    }

    @Override
    public void onStepClicked(List<Steps> steps, int position) {
        if(!getResources().getBoolean(R.bool.isTablet)) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(ITEM_STEPS, (ArrayList<? extends Parcelable>) steps);
            bundle.putInt(ITEM_POSITION, position);

            Intent intent;
            intent = new Intent(this, StepActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            stepDetailFragment.setPosition(position);
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

    @Override
    public boolean onVideoStatusPlayChanged(ExoPlayerFragment exoPlayerFragment) {
        return exoPlayerFragment != null && exoPlayerFragment.getCurrentStatus();
    }
}
