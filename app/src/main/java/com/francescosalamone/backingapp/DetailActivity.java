package com.francescosalamone.backingapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.francescosalamone.backingapp.Fragment.RecipeDetailFragment;

public class DetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepClickListener{

    public static final String ITEM_POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        int containerId = R.id.recipe_detail_container;

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment existOldFragment = getSupportFragmentManager().findFragmentById(containerId);
        if(existOldFragment == null || !existOldFragment.getClass().equals(recipeDetailFragment.getClass())) {
            fragmentManager.beginTransaction()
                    .add(containerId, recipeDetailFragment)
                    .commit();
        }

    }

    @Override
    public void onStepClicked(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(ITEM_POSITION, position);

        Intent intent = new Intent(this, StepActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
