package com.samagra.ancillaryscreens.screens.tutorials;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;
import com.samagra.ancillaryscreens.R;

import java.util.Objects;


public class YoutubeFragment extends Fragment {

    private String YoutubeAPIKey = "";
    private String VIDEO_ID = "";
    private YouTubePlayer YPlayer;

    @Override
    public void onAttach(Activity activity) {

        if (activity instanceof FragmentActivity) {
            FragmentActivity myContext = (FragmentActivity) activity;
        }
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.you_tube_api, container, false);
        YouTubePlayerSupportFragmentX youTubePlayerFragment = YouTubePlayerSupportFragmentX.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.youtube_layout, youTubePlayerFragment).commit();
        if(getArguments() != null) {
            YoutubeAPIKey = Objects.requireNonNull(getArguments()).getString("youtube_api_key", "");
            VIDEO_ID = Objects.requireNonNull(getArguments()).getString("youtube_tutorial_video_id", "");
        }

        youTubePlayerFragment.initialize(YoutubeAPIKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                if (!wasRestored) {
                    YPlayer = youTubePlayer;
                    YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    YPlayer.loadVideo(VIDEO_ID);
                    YPlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        return rootView;
    }
}