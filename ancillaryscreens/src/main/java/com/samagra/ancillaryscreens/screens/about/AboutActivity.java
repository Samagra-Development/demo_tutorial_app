package com.samagra.ancillaryscreens.screens.about;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samagra.ancillaryscreens.InvalidConfigurationException;
import com.samagra.ancillaryscreens.R;
import com.samagra.ancillaryscreens.base.BaseActivity;
import com.samagra.ancillaryscreens.models.AboutBundle;


import javax.inject.Inject;

/**
 * The View Part for the About Screen, must implement {@link AboutContract.View} and {@link AboutAdapter.AboutItemClickListener}.
 * The activity is adapted from the ODK library and efforts have been made to keep it as similar as possible.
 *
 * @author Pranav Sharma
 */
public class AboutActivity extends BaseActivity implements AboutContract.View, AboutAdapter.AboutItemClickListener {

    private int websiteResIcon, websiteLinkTextResId, websiteSummaryDescResId;
    private String title;

    @Inject
    AboutPresenter<AboutContract.View, AboutContract.Interactor> aboutPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        initToolbar();

        if (getIntent().getBundleExtra("config") == null)
            throw new InvalidConfigurationException(AboutActivity.class);
        else
            configureActivityFromBundle(getIntent().getBundleExtra("config"));

        int [][] items1 = {
                {websiteResIcon, websiteLinkTextResId, websiteSummaryDescResId}
        };
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new AboutAdapter(items1, this, this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(getString(R.string.about_preferences));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

    @Override
    public void setupRecyclerView() {

    }


    @Override
    public void onStart() {
        super.onStart();
    }


    /**
     * Configures the AboutActivity through config values passed from the app module via {@link Bundle} object
     *
     * @param bundle - The bundle containing the config values.
     * @see AboutBundle
     */

    public void configureActivityFromBundle(Bundle bundle) {
        AboutBundle aboutBundle = AboutBundle.getAboutBundleFromBundle(bundle);
        websiteLinkTextResId = aboutBundle.getWebsiteLinkTextResId();
        websiteResIcon = aboutBundle.getWebsiteIconResId();
        websiteSummaryDescResId = aboutBundle.getWebsiteSummaryDescriptionResId();
        title = aboutBundle.getScreenTitle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(int position) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
