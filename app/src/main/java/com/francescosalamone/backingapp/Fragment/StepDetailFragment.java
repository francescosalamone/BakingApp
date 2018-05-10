package com.francescosalamone.backingapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.francescosalamone.backingapp.Adapter.RecipesAdapter;
import com.francescosalamone.backingapp.DetailActivity;
import com.francescosalamone.backingapp.FullScreenVideoActivity;
import com.francescosalamone.backingapp.Model.Recipes;
import com.francescosalamone.backingapp.Model.Steps;
import com.francescosalamone.backingapp.R;
import com.francescosalamone.backingapp.databinding.FragmentStepDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


public class StepDetailFragment extends Fragment {

    public static final String BUNDLE_VIDEO_URL = "video_url";
    public static final int LANDSCAPE_INTENT_REQUEST = 157;
    public static final String BUNDLE_VIDEO_POSITION = "video_position";
    private static final String BUNDLE_STEPS = "steps";
    public static final String BUNDLE_VIDEO_IS_PLAYING = "video_is_playing";

    private OnVideoUrlListener mCallback;
    private OnStartFullScreenListener mFullScreenCallback;
    private OnVideoStatusPlayListener mVideoStatusCallback;
    private ExoPlayerFragment exoPlayerFragment;
    private List<Steps> steps;
    private int position=-1;
    private Steps step;
    private long currentVideoPosition = 0L;
    private boolean videoIsPlaying = false;

    FragmentStepDetailBinding mBinding;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public interface OnVideoUrlListener{
        void onVideoUrlChanged(String url, ExoPlayerFragment exoPlayerFragment);
    }

    public interface OnStartFullScreenListener{
        long onStartFullScreen(ExoPlayerFragment exoPlayerFragment);
    }

    public interface OnVideoStatusPlayListener{
        boolean onVideoStatusPlayChanged(ExoPlayerFragment exoPlayerFragment);
    }

    //With this we are sure that in the host activity we implement the OnStepClickListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnVideoUrlListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement OnVideoUrlListener");
        }

        try {
            mFullScreenCallback = (OnStartFullScreenListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement OnStartFullScreenListener");
        }

        try {
            mVideoStatusCallback = (OnVideoStatusPlayListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement OnVideoStatusPlayListener");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DetailActivity.ITEM_POSITION, position);
        outState.putParcelableArrayList(BUNDLE_STEPS, (ArrayList<? extends Parcelable>) steps);
        if(exoPlayerFragment != null) {
            long latestVideoPosition = mFullScreenCallback.onStartFullScreen(exoPlayerFragment);
            videoIsPlaying = mVideoStatusCallback.onVideoStatusPlayChanged(exoPlayerFragment);
            outState.putBoolean(BUNDLE_VIDEO_IS_PLAYING, videoIsPlaying);
            outState.putLong(BUNDLE_VIDEO_POSITION, latestVideoPosition);
        } else {
            outState.putLong(BUNDLE_VIDEO_POSITION, currentVideoPosition);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            position = savedInstanceState.getInt(DetailActivity.ITEM_POSITION, 0);
            currentVideoPosition = savedInstanceState.getLong(BUNDLE_VIDEO_POSITION, 0L);
            steps = savedInstanceState.getParcelableArrayList(BUNDLE_STEPS);
            videoIsPlaying = savedInstanceState.getBoolean(BUNDLE_VIDEO_IS_PLAYING);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);
        View rootView = mBinding.getRoot();

        if(savedInstanceState == null) {
            if(getResources().getBoolean(R.bool.isTablet)){
                Intent intent = getActivity().getIntent();
                Recipes recipe = intent.getParcelableExtra(RecipesAdapter.RECIPE);
                steps = recipe.getSteps();
                position = 0;
            } else {
                Intent intent = getActivity().getIntent();
                if (intent != null) {
                    steps = intent.getParcelableArrayListExtra(DetailActivity.ITEM_STEPS);
                    if (position == -1) {
                        position = intent.getIntExtra(DetailActivity.ITEM_POSITION, 0);
                    }
                }
            }
            videoIsPlaying = true;
        }

        int containerId = R.id.portrait_exoplayer_container;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        step = steps.get(position);
        if(getResources().getBoolean(R.bool.isLandscape) && !step.getVideoURL().equals("")){
            startFullScreenMode(fragmentManager, containerId);
        } else {

            setHasOptionsMenu(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            createFragment();

        }

        return rootView;
    }

    private void createFragment(){
        int containerId = R.id.portrait_exoplayer_container;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        exoPlayerFragment = new ExoPlayerFragment();

        Fragment existOldFragment = fragmentManager.findFragmentById(containerId);
        if(existOldFragment == null || !existOldFragment.getClass().equals(existOldFragment.getClass())) {
            fragmentManager.beginTransaction()
                    .add(containerId, exoPlayerFragment)
                    .commit();
        } else {
            exoPlayerFragment = (ExoPlayerFragment) existOldFragment;
        }

        popolateUI(fragmentManager, containerId);
    }

    private void startFullScreenMode(FragmentManager fragmentManager, int containerId){
        if(fragmentManager.findFragmentById(containerId) != null){
            fragmentManager.beginTransaction()
                    .remove(fragmentManager.findFragmentById(containerId))
                    .commit();
        }
        Intent intentFullScreen = new Intent(getActivity(), FullScreenVideoActivity.class);
        intentFullScreen.putExtra(BUNDLE_VIDEO_URL, step.getVideoURL());
        intentFullScreen.putExtra(BUNDLE_VIDEO_POSITION, currentVideoPosition);
        intentFullScreen.putExtra(BUNDLE_VIDEO_IS_PLAYING, videoIsPlaying);
        startActivityForResult(intentFullScreen, LANDSCAPE_INTENT_REQUEST);
    }

    private void popolateUI(final FragmentManager fragmentManager, final int containerId){
        if(!getResources().getBoolean(R.bool.isTablet)) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(step.getShortDescription());
        }

        mCallback.onVideoUrlChanged(step.getVideoURL(), exoPlayerFragment);
        exoPlayerFragment.setSeekTo(currentVideoPosition);
        exoPlayerFragment.setStatus(videoIsPlaying);

        if(step.getVideoURL().equals("")){
            mBinding.portraitExoplayerContainer.setVisibility(View.GONE);
        } else {
            mBinding.portraitExoplayerContainer.setVisibility(View.VISIBLE);
        }

        if(getResources().getBoolean(R.bool.isLandscape) && !step.getVideoURL().equals("")){
            startFullScreenMode(fragmentManager, containerId);
        } else {

            mBinding.stepDescriptionFragmentTv.setText(step.getDescription());

            if (!step.getThumbnailURL().equals("")) {
                mBinding.stepThumbnailIv.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(step.getThumbnailURL())
                        .into(mBinding.stepThumbnailIv);
            } else {
                mBinding.stepThumbnailIv.setVisibility(GONE);
            }

            if (position < steps.size() - 1 && !getResources().getBoolean(R.bool.isTablet)) {
                mBinding.nextStepBt.setVisibility(View.VISIBLE);
                mBinding.nextStepBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentVideoPosition = 0L;
                        videoIsPlaying = true;
                        exoPlayerFragment.setSeekTo(currentVideoPosition);
                        exoPlayerFragment.setStatus(videoIsPlaying);
                        position++;
                        step = steps.get(position);
                        popolateUI(fragmentManager, containerId);
                    }
                });
            } else {
                mBinding.nextStepBt.setVisibility(GONE);
            }

            if (position > 0 && !getResources().getBoolean(R.bool.isTablet)) {
                mBinding.prevStepBt.setVisibility(View.VISIBLE);
                mBinding.prevStepBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentVideoPosition = 0L;
                        videoIsPlaying = true;
                        exoPlayerFragment.setSeekTo(currentVideoPosition);
                        exoPlayerFragment.setStatus(videoIsPlaying);
                        position--;
                        step = steps.get(position);
                        popolateUI(fragmentManager, containerId);
                    }
                });
            } else {
                mBinding.prevStepBt.setVisibility(GONE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == StepDetailFragment.LANDSCAPE_INTENT_REQUEST){
            if(resultCode == FullScreenVideoActivity.RESULT_POSITION){
                currentVideoPosition = data.getLongExtra(BUNDLE_VIDEO_POSITION, 0L);
                videoIsPlaying = data.getBooleanExtra(BUNDLE_VIDEO_IS_PLAYING, true);
                exoPlayerFragment.setSeekTo(currentVideoPosition);
                exoPlayerFragment.setStatus(videoIsPlaying);
            } else {
                getActivity().finish();
            }
        }
    }

    public void setPosition(int position){
        this.position = position;
        step = steps.get(position);
        currentVideoPosition = 0L;
        exoPlayerFragment.setSeekTo(currentVideoPosition);
        popolateUI(null, 0);
    }

}
