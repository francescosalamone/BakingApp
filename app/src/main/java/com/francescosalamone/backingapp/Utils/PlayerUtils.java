package com.francescosalamone.backingapp.Utils;

import android.content.Context;
import android.net.Uri;

import com.francescosalamone.backingapp.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


/**
 * Created by Francesco on 16/04/2018.
 */

public class PlayerUtils {

    public PlayerUtils() {
    }

    public static SimpleExoPlayer initPlayer(Uri mediaUri, Context context, PlayerView playerView, long startVideoPosition, boolean videoIsPlaying){
        SimpleExoPlayer exoPlayer;
        //playerView = new PlayerView(context);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context ,trackSelector);

        playerView.setPlayer(exoPlayer);

        DataSource.Factory dataSource = new CacheDataSourceFactory(context, 100 * 1024 * 1024, 5 * 1024 * 1024);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSource)
                .createMediaSource(mediaUri);

        exoPlayer.prepare(mediaSource);
        exoPlayer.seekTo(startVideoPosition);
        exoPlayer.setPlayWhenReady(videoIsPlaying);

        return exoPlayer;
    }

    public static void releasePlayer(SimpleExoPlayer exoPlayer){
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }
}
