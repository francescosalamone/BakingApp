package com.francescosalamone.backingapp;


import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.francescosalamone.backingapp.Fragment.StepDetailFragment;

public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        int containerId = R.id.step_detail_container;

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(containerId, stepDetailFragment)
                .commit();
    }
}
