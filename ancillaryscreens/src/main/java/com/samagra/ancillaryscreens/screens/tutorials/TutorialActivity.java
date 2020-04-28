package com.samagra.ancillaryscreens.screens.tutorials;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.samagra.ancillaryscreens.R;
import com.samagra.ancillaryscreens.base.BaseActivity;

import java.util.Objects;

public class TutorialActivity extends BaseActivity {

    Bundle videoDetails = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_tutorial);

        String title = "Tutorials";
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(view -> finish());

        if(getIntent() != null) {
            videoDetails = getIntent().getBundleExtra("video_details_bundle");
        }
        YoutubeFragment fragment = new YoutubeFragment();
        fragment.setArguments(videoDetails);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.frame_fragment, fragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

}