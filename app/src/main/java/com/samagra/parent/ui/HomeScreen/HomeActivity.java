package com.samagra.parent.ui.HomeScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.androidnetworking.AndroidNetworking;
import com.google.android.material.snackbar.Snackbar;
import com.samagra.ancillaryscreens.AncillaryScreensDriver;
import com.samagra.ancillaryscreens.models.AboutBundle;
import com.samagra.commons.Constants;
import com.samagra.commons.CustomEvents;
import com.samagra.commons.ExchangeObject;
import com.samagra.commons.InternetMonitor;
import com.samagra.commons.LocaleManager;
import com.samagra.commons.MainApplication;
import com.samagra.commons.Modules;
import com.samagra.parent.AppConstants;
import com.samagra.parent.R;
import com.samagra.parent.UtilityFunctions;
import com.samagra.parent.base.BaseActivity;
import com.samagra.parent.ui.Settings.ChangeLanguageActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * View part of the Home Screen. This class only handles the UI operations, all the business logic is simply
 * abstracted from this Activity. It <b>must</b> implement the {@link HomeMvpView} and extend the {@link BaseActivity}
 *
 * @author Pranav Sharma
 */
public class HomeActivity extends BaseActivity implements HomeMvpView, View.OnClickListener {

    @BindView(R.id.fill_forms)
    public LinearLayout fillFormLayout;
    @BindView(R.id.view_submitted_forms)
    public LinearLayout viewSubmittedLayout;
    @BindView(R.id.submit_forms)
    public LinearLayout submitFormLayout;
    @BindView(R.id.need_help)
    public LinearLayout helplineLayout;
    @BindView(R.id.parent)
    public RelativeLayout parent;

    private Disposable logoutListener = null;
    private PopupMenu popupMenu;
    private Snackbar progressSnackbar = null;
    private Unbinder unbinder;


    @Inject
    HomePresenter<HomeMvpView, HomeMvpInteractor> homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        InternetMonitor.startMonitoringInternet();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this);
        homePresenter.onAttach(this);
        setupToolbar();
        setupListeners();
    }


    @Override
    protected void onResume() {
        super.onResume();
        boolean isLanguageChanged = PreferenceManager.getDefaultSharedPreferences(getActivityContext()).getBoolean("isLanguageChanged", false);
        if (isLanguageChanged) {
            LocaleManager.setNewLocale(this,
                    PreferenceManager.getDefaultSharedPreferences(getActivityContext()).getString("currentLanguage", LocaleManager.HINDI));
            Intent intent = this.getIntent();
            PreferenceManager.getDefaultSharedPreferences(getActivityContext()).edit().putBoolean("isLanguageChanged", false).apply();
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        ;
        customizeToolbar();

    }

    private void setupListeners() {
        fillFormLayout.setOnClickListener(this);
        viewSubmittedLayout.setOnClickListener(this);
        submitFormLayout.setOnClickListener(this);
        helplineLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fill_forms:
                homePresenter.onFillFormsOptionClicked();
                break;
            case R.id.view_submitted_forms:
                homePresenter.onViewSubmittedFormsOptionsClicked();
                break;
            case R.id.submit_forms:
                homePresenter.onSubmitFormsClicked();
                break;
            case R.id.need_help:
                homePresenter.onViewHelplineClicked();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (logoutListener != null && !logoutListener.isDisposed()) {
            AndroidNetworking.cancel(Constants.LOGOUT_CALLS);
            logoutListener.dispose();
        }
        homePresenter.onDetach();
        unbinder.unbind();
    }

    /**
     * Only set the title and action bar here; do not make further modifications.
     * Any further modifications done to the toolbar here will be overwritten. If you wish to prevent modifications
     * from being overwritten, do them after onCreate is complete.
     * This method should be called in onCreate of your activity.
     */
    @Override
    public void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }

    @Override
    public void customizeToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(this::initAndShowPopupMenu);
    }

    @Override
    public void showOnClickMessage(String message) {
        showSnackbar(message, Snackbar.LENGTH_SHORT);
    }


    /**
     * Provides with a {@link AboutBundle} object that is used to further configure
     * the UI for {@link com.samagra.ancillaryscreens.screens.about.AboutActivity}
     */
    private AboutBundle provideAboutBundle() {
        return new AboutBundle(
                "About Us",
                AppConstants.ABOUT_WEBSITE_LINK,
                AppConstants.ABOOUT_FORUM_LINK,
                R.drawable.samagra_logo,
                R.string.app_name,
                R.string.about_us_summary);
    }

    /**
     * Giving Control of the UI to XML file for better customization and easier changes
     */
    private void initAndShowPopupMenu(View v) {

        if (popupMenu == null) {
            popupMenu = new PopupMenu(HomeActivity.this, v);
            popupMenu.getMenuInflater().inflate(R.menu.home_screen_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.about_us:
                        AncillaryScreensDriver.launchAboutActivity(this, provideAboutBundle());
                        break;
                    case R.id.tutorial_video:
                        if (homePresenter.getTutorialVideoID().isEmpty() || homePresenter.getYoutubeAPIKey().isEmpty()) {
                            showOnClickMessage("Please configure Video ID and API Key to see Youtube Videos.");
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("youtube_api_key", homePresenter.getYoutubeAPIKey());
                            bundle.putString("youtube_tutorial_video_id", homePresenter.getTutorialVideoID());
                            AncillaryScreensDriver.launchTutorialActivity(this, bundle);
                        }
                        break;
                    case R.id.profile:
                        showOnClickMessage("You have clicked View user profile option");
                        break;
                    case R.id.logout:
                        if (homePresenter.isNetworkConnected()) {
                            if (logoutListener == null)
                                initializeLogoutListener();
                            AncillaryScreensDriver.performLogout(this);
                        } else {
                            showSnackbar("It seems you are offline. Logout cannot happen in offline conditions.", 3000);
                        }
                        break;
                    case R.id.change_lang:
                        Intent intent = new Intent(getActivityContext(), ChangeLanguageActivity.class);
                        intent.putExtra("title", getResources().getString(R.string.change_language));
                        getActivityContext().startActivity(intent);
                        break;
                }
                return true;
            });
        }
        popupMenu.show();
    }


    private void initializeLogoutListener() {
        logoutListener = ((MainApplication) (getApplicationContext()))
                .getEventBus()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    Timber.i("Received event Logout");
                    if (o instanceof ExchangeObject.EventExchangeObject) {
                        ExchangeObject.EventExchangeObject eventExchangeObject = (ExchangeObject.EventExchangeObject) o;
                        if (eventExchangeObject.to == Modules.MAIN_APP && eventExchangeObject.from == Modules.ANCILLARY_SCREENS) {
                            if (eventExchangeObject.customEvents == CustomEvents.LOGOUT_COMPLETED) {
                                hideLoading();
                                logoutListener.dispose();
                            } else if (eventExchangeObject.customEvents == CustomEvents.LOGOUT_INITIATED) {
                                showLoading("Logging you out...Please wait.");
                            }
                        }
                    }
                }, Timber::e);
    }

    @Override
    public void showLoading(String message) {
        hideLoading();
        if (progressSnackbar == null) {
            progressSnackbar = UtilityFunctions.getSnackbarWithProgressIndicator(findViewById(android.R.id.content), getApplicationContext(), message);
        }
        progressSnackbar.setText(message);
        progressSnackbar.show();
    }

    @Override
    public void hideLoading() {
        if (progressSnackbar != null && progressSnackbar.isShownOrQueued())
            progressSnackbar.dismiss();
    }


}
