package com.francescosalamone.backingapp.Fragment;


import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.francescosalamone.backingapp.R;
import com.francescosalamone.backingapp.Utils.PlayerUtils;
import com.francescosalamone.backingapp.databinding.FragmentExoPlayerBinding;
import com.google.android.exoplayer2.SimpleExoPlayer;

public class ExoPlayerFragment extends Fragment {

    private long currentVideoPosition = 0L;
    private FragmentExoPlayerBinding mBinding;
    private SimpleExoPlayer exoPlayer;
    private Uri videoUri;
    private boolean activityIsStarted=false;

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
        if(exoPlayer != null) {
            PlayerUtils.releasePlayer(exoPlayer);
        }
        super.onDestroyView();
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

    private void startVideo(){
        if(activityIsStarted) {
            if (videoUri != null) {
                mBinding.videoPlayer.setVisibility(View.VISIBLE);
                if (exoPlayer != null) {
                    PlayerUtils.releasePlayer(exoPlayer);
                }
                exoPlayer = PlayerUtils.initPlayer(videoUri, getContext(), mBinding.videoPlayer, currentVideoPosition);
            } else {
                mBinding.videoPlayer.setVisibility(View.GONE);
                exoPlayer = null;
            }
        }
    }

    public void setSeekTo(long currentVideoPosition) {
        if(exoPlayer != null){
            exoPlayer.seekTo(currentVideoPosition);
        }
        this.currentVideoPosition = currentVideoPosition;
    }
}
