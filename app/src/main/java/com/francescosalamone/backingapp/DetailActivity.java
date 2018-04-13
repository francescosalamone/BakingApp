package com.francescosalamone.backingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.francescosalamone.backingapp.Fragment.RecipeDetailFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        int containerId = R.id.recipe_detail_container;

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(containerId, recipeDetailFragment)
                .commit();
    }
}
