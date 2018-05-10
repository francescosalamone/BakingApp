package com.francescosalamone.backingapp.Fragment;


import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.francescosalamone.backingapp.R;
import com.francescosalamone.backingapp.Utils.PlayerUtils;
import com.francescosalamone.backingapp.databinding.FragmentExoPlayerBinding;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

public class ExoPlayerFragment extends Fragment {

    private long currentVideoPosition = 0L;
    private FragmentExoPlayerBinding mBinding;
    private SimpleExoPlayer exoPlayer;
    private Uri videoUri;
    private boolean activityIsStarted = false;
    private boolean videoIsPlaying = false;

    public ExoPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_exo_player,container, false);
        View rootView = mBinding.getRoot();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        activityIsStarted = true;
        startVideo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(exoPlayer != null) {
            PlayerUtils.releasePlayer(exoPlayer);
        }
    }

    public void setMediaUrl(String mediaUrl){

        if(!mediaUrl.equals("")) {
            videoUri = Uri.parse(mediaUrl).buildUpon().build();
        } else {
            videoUri = null;
        }
        startVideo();
    }

    public long getCurrentPosition(){
        if(exoPlayer != null) {
            return exoPlayer.getCurrentPosition();
        } else {
            return 0L;
        }
    }

    public boolean getCurrentStatus() {
        return exoPlayer != null && exoPlayer.getPlayWhenReady();
    }

    private void startVideo(){
        if(activityIsStarted) {
            if (videoUri != null) {
                mBinding.videoPlayer.setVisibility(View.VISIBLE);
                if (exoPlayer != null) {
                    PlayerUtils.releasePlayer(exoPlayer);
                }
                exoPlayer = PlayerUtils.initPlayer(videoUri, getContext(), mBinding.videoPlayer, currentVideoPosition, videoIsPlaying);
            } else {
                mBinding.videoPlayer.setVisibility(View.GONE);
                PlayerUtils.releasePlayer(exoPlayer);
            }
        }
    }

    public void setSeekTo(long currentVideoPosition) {
        if(exoPlayer != null){
            exoPlayer.seekTo(currentVideoPosition);
        }
        this.currentVideoPosition = currentVideoPosition;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(exoPlayer != null) {
            PlayerUtils.releasePlayer(exoPlayer);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(exoPlayer != null) {
            PlayerUtils.releasePlayer(exoPlayer);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startVideo();
    }

    public void setStatus(boolean status) {
        videoIsPlaying = status;
    }
}
