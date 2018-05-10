package com.francescosalamone.backingapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.francescosalamone.backingapp.Fragment.ExoPlayerFragment;
import com.francescosalamone.backingapp.Fragment.StepDetailFragment;

public class FullScreenVideoActivity extends AppCompatActivity {

    public static final int RESULT_POSITION = 112;
    private static final String STATE_VIDEO_POSITION = "video_position";
    public static final String STATE_VIDEO_IS_PLAYING = "video_is_playing";
    private ExoPlayerFragment exoPlayerFragment;
    private long currentVideoPosition = 0L;
    private boolean videoIsPlaying = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_VIDEO_POSITION, exoPlayerFragment.getCurrentPosition());
        outState.putBoolean(STATE_VIDEO_IS_PLAYING, exoPlayerFragment.getCurrentStatus());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        setContentView(R.layout.activity_full_screen_video);

        if(savedInstanceState != null){
            currentVideoPosition = savedInstanceState.getLong(STATE_VIDEO_POSITION, 0L);
            videoIsPlaying = savedInstanceState.getBoolean(STATE_VIDEO_IS_PLAYING);
        }

        exoPlayerFragment = new ExoPlayerFragment();
        int containerId = R.id.fullscreen_exoplayer_container;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(!getResources().getBoolean(R.bool.isLandscape)) {
            if(fragmentManager.findFragmentById(containerId) != null){
                fragmentManager.beginTransaction()
                        .remove(fragmentManager.findFragmentById(containerId))
                        .commit();
            }
            Intent intent = new Intent();
            intent.putExtra(StepDetailFragment.BUNDLE_VIDEO_POSITION, currentVideoPosition);
            intent.putExtra(StepDetailFragment.BUNDLE_VIDEO_IS_PLAYING, videoIsPlaying);
            setResult(RESULT_POSITION, intent);
            finish();
        } else {
            Intent intent = getIntent();
            String videoUrl = intent.getStringExtra(StepDetailFragment.BUNDLE_VIDEO_URL);
            currentVideoPosition = intent.getLongExtra(StepDetailFragment.BUNDLE_VIDEO_POSITION, 0L);
            videoIsPlaying = intent.getBooleanExtra(StepDetailFragment.BUNDLE_VIDEO_IS_PLAYING, true);

            Fragment existOldFragment = getSupportFragmentManager().findFragmentById(containerId);
            if (existOldFragment == null || !existOldFragment.getClass().equals(exoPlayerFragment.getClass())) {
                fragmentManager.beginTransaction()
                        .add(containerId, exoPlayerFragment)
                        .commit();
            } else {
                exoPlayerFragment = (ExoPlayerFragment) existOldFragment;
            }

            exoPlayerFragment.setMediaUrl(videoUrl);
            exoPlayerFragment.setSeekTo(currentVideoPosition);
            exoPlayerFragment.setStatus(videoIsPlaying);
        }

    }
}
