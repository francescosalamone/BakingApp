package com.francescosalamone.backingapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.francescosalamone.backingapp.DetailActivity;
import com.francescosalamone.backingapp.FullScreenVideoActivity;
import com.francescosalamone.backingapp.Model.Steps;
import com.francescosalamone.backingapp.R;
import com.francescosalamone.backingapp.databinding.FragmentStepDetailBinding;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


public class StepDetailFragment extends Fragment {

    public static final String BUNDLE_VIDEO_URL = "video_url";
    public static final int LANDSCAPE_INTENT_REQUEST = 157;
    public static final String BUNDLE_VIDEO_POSITION = "video_position";
    private static final String BUNDLE_STEPS = "steps";

    private OnVideoUrlListener mCallback;
    private OnStartFullScreenListener mFullScreenCallback;
    private ExoPlayerFragment exoPlayerFragment;
    private List<Steps> steps;
    private int position=-1;
    private Steps step;
    private long currentVideoPosition = 0L;

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
            throw new ClassCastException(context.toString() + "must implement OnStartFulloScreenListener");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DetailActivity.ITEM_POSITION, position);
        outState.putParcelableArrayList(BUNDLE_STEPS, (ArrayList<? extends Parcelable>) steps);
        if(exoPlayerFragment != null) {
            long latestVideoPosition = mFullScreenCallback.onStartFullScreen(exoPlayerFragment);
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
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);
        View rootView = mBinding.getRoot();

        if(savedInstanceState == null) {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                steps = intent.getParcelableArrayListExtra(DetailActivity.ITEM_STEPS);
                if (position == -1) {
                    position = intent.getIntExtra(DetailActivity.ITEM_POSITION, 0);
                }
            }
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

        fragmentManager.beginTransaction()
                .add(containerId, exoPlayerFragment)
                .commit();

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
        startActivityForResult(intentFullScreen, LANDSCAPE_INTENT_REQUEST);
    }

    private void popolateUI(final FragmentManager fragmentManager, final int containerId){
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(step.getShortDescription());

        mCallback.onVideoUrlChanged(step.getVideoURL(), exoPlayerFragment);

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

            if (position < steps.size() - 1) {
                mBinding.nextStepBt.setVisibility(View.VISIBLE);
                mBinding.nextStepBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentVideoPosition = 0L;
                        exoPlayerFragment.setSeekTo(currentVideoPosition);
                        position++;
                        step = steps.get(position);
                        popolateUI(fragmentManager, containerId);
                    }
                });
            } else {
                mBinding.nextStepBt.setVisibility(GONE);
            }

            if (position > 0) {
                mBinding.prevStepBt.setVisibility(View.VISIBLE);
                mBinding.prevStepBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentVideoPosition = 0L;
                        exoPlayerFragment.setSeekTo(currentVideoPosition);
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
                currentVideoPosition = data.getLongExtra(StepDetailFragment.BUNDLE_VIDEO_POSITION, 0L);
                exoPlayerFragment.setSeekTo(currentVideoPosition);
            } else {
                getActivity().finish();
            }
        }
    }

}
