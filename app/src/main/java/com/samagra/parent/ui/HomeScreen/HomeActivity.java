package com.samagra.parent.ui.HomeScreen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androidnetworking.AndroidNetworking;
import com.google.android.material.snackbar.Snackbar;
import com.samagra.ancillaryscreens.AncillaryScreensDriver;
import com.samagra.ancillaryscreens.models.AboutBundle;
import com.samagra.cascading_module.CascadingModuleDriver;
import com.samagra.cascading_module.models.InstitutionInfo;
import com.samagra.commons.Constants;
import com.samagra.commons.CustomEvents;
import com.samagra.commons.ExchangeObject;
import com.samagra.commons.InternetMonitor;
import com.samagra.commons.MainApplication;
import com.samagra.commons.Modules;
import com.samagra.notification_module.AppNotificationUtils;
import com.samagra.parent.AppConstants;
import com.samagra.parent.R;
import com.samagra.parent.UtilityFunctions;
import com.samagra.parent.base.BaseActivity;
import com.samagra.parent.ui.Settings.ILanguageChangedListener;
import com.samagra.parent.ui.Settings.UpdateAppLanguageFragment;
import com.samagra.user_profile.contracts.ComponentManager;
import com.samagra.user_profile.contracts.IProfileContract;
import com.samagra.user_profile.contracts.ProfileSectionInteractor;
import com.samagra.user_profile.profile.UserProfileElement;

import org.odk.collect.android.utilities.LocaleHelper;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static org.odk.collect.android.preferences.GeneralKeys.KEY_APP_LANGUAGE;

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
    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    private PopupMenu popupMenu;
    private Snackbar progressSnackbar = null;
    private Unbinder unbinder;

    @BindView(R.id.circularProgressBar)
    public ProgressBar circularProgressBar;

    @BindView(R.id.parentHome)
    public LinearLayout parentHome;
    private ProgressBar formProgressBar;

    @Inject
    HomePresenter<HomeMvpView, HomeMvpInteractor> homePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getActivityComponent().inject(this);
        unbinder = ButterKnife.bind(this);
        homePresenter.onAttach(this);
        setupToolbar();
        homePresenter.applySettings();
        formProgressBar = findViewById(R.id.form_progressBar);
        InternetMonitor.startMonitoringInternet(((MainApplication) getApplicationContext()));
        setupListeners();
        setDisposable();
        homePresenter.updateLanguageSettings();
        AppNotificationUtils.updateFirebaseToken(getActivityContext(), AppConstants.BASE_API_URL, getActivityContext().getResources().getString(R.string.fusionauth_api_key));
    }


    private void tartActivityAndCloseAllOthers(Activity activityContext) {
        new LocaleHelper().updateLocale(getActivityContext());
        startActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(0, 0);
        finishAffinity();
    }


    @Override
    protected void onResume() {
        super.onResume();
        renderLayoutInvisible();
        homePresenter.checkForFormUpdates();
        customizeToolbar();

    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if(fm.getBackStackEntryCount()>0) {
            if(fm.getBackStackEntryAt(0).getName().equals("ChangeLanguageActivity")){
                fm.popBackStackImmediate();
                parentHome.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void renderLayoutVisible() {
        formProgressBar.setVisibility(View.GONE);
        parentHome.setVisibility(View.VISIBLE);
        circularProgressBar.setVisibility(View.GONE);
    }


    private void renderLayoutInvisible() {
        formProgressBar.setVisibility(View.VISIBLE);
        formProgressBar.setProgress(0);
        parentHome.setVisibility(View.GONE);
        circularProgressBar.setVisibility(View.VISIBLE);
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
//                launchSearchModule();
//                homePresenter.onFillFormsOptionClicked();
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

    @SuppressWarnings("SameParameterValue")
    private void addFragment(int containerViewId, FragmentManager manager, Fragment fragment, String fragmentTag) {
        try {
            final String fragmentname = fragment.getClass().getName();
            Timber.d("addFragment() :: Adding new fragment %s", fragmentname);
            // Create new fragment and transaction
            final FragmentTransaction transaction = manager.beginTransaction();

            transaction.add(containerViewId, fragment, fragmentTag);
            transaction.addToBackStack(fragmentTag);
            new Handler().post(() -> {
                try {
                    transaction.commit();
                } catch (IllegalStateException ex) {
                    Timber.e("Failed to commit Fragment Transaction with exception %s", ex.getMessage());
                }
            });
        } catch (IllegalStateException ex) {
            Timber.e("Failed to add Fragment with exception %s", ex.getMessage());

        }

    }

    @SuppressWarnings("unchecked")
    private void setDisposable() {
        compositeDisposable.add(((MainApplication)getApplicationContext()).getEventBus()
                .toObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer) exchangeObject -> {
                    if (exchangeObject instanceof ExchangeObject.DataExchangeObject) {
                        if(((ExchangeObject.DataExchangeObject) exchangeObject).to == Modules.MAIN_APP &&
                                ((ExchangeObject.DataExchangeObject) exchangeObject).from == Modules.CASCADING_SEARCH &&
                                ((ExchangeObject.DataExchangeObject) exchangeObject).data instanceof InstitutionInfo) {
                            homePresenter.onFillFormsOptionClicked();
                            if (!compositeDisposable.isDisposed())
                                compositeDisposable.dispose();
                        }
                    }
                }, Timber::e));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (logoutListener != null && !logoutListener.isDisposed()) {
            AndroidNetworking.cancel(Constants.LOGOUT_CALLS);
            logoutListener.dispose();
        }
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
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
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.change_lang:
                            if (HomeActivity.this.findViewById(R.id.fragment_container) != null) {
                                UpdateAppLanguageFragment firstFragment = UpdateAppLanguageFragment.newInstance(PreferenceManager.getDefaultSharedPreferences(HomeActivity.this.getActivityContext())
                                        .getString(Constants.APP_LANGUAGE_KEY, "en"), language -> {
                                            SharedPreferences.Editor edit = PreferenceManager
                                                    .getDefaultSharedPreferences(getActivityContext()).edit();
                                            edit.putString(KEY_APP_LANGUAGE, language);
                                            edit.putString(Constants.APP_LANGUAGE_KEY, language);
                                            edit.apply();
                                            tartActivityAndCloseAllOthers((Activity) getActivityContext());
                                        });
                                HomeActivity.this.addFragment(R.id.fragment_container, HomeActivity.this.getSupportFragmentManager(), firstFragment, "ChangeLanguageActivity");
                                parentHome.setVisibility(View.GONE);
                            }
                            break;
                        case R.id.about_us:
                            AncillaryScreensDriver.launchAboutActivity(HomeActivity.this, HomeActivity.this.provideAboutBundle());
                            break;
                        case R.id.tutorial_video:
                            if (homePresenter.getTutorialVideoID().isEmpty() || homePresenter.getYoutubeAPIKey().isEmpty()) {
                                HomeActivity.this.showOnClickMessage("Please configure Video ID and API Key to see Youtube Videos.");
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("youtube_api_key", homePresenter.getYoutubeAPIKey());
                                bundle.putString("youtube_tutorial_video_id", homePresenter.getTutorialVideoID());
                                AncillaryScreensDriver.launchTutorialActivity(HomeActivity.this, bundle);
                            }
                            break;
                        case R.id.profile:
                            ComponentManager.registerProfilePackage(new ProfileSectionInteractor(), ((MainApplication) (HomeActivity.this.getApplicationContext())),
                                    AppConstants.BASE_API_URL,
                                    "4b49c1c8-f90e-41e9-99ab-16d4af9eb269",
                                    AppConstants.SEND_OTP_URL,
                                    AppConstants.UPDATE_PASSWORD_URL,
                                    HomeActivity.this.getApplicationContext().getResources().getString(R.string.fusionauth_api_key), homePresenter.fetchUserID());
                            IProfileContract initializer = ComponentManager.iProfileContract;
                            ArrayList<UserProfileElement> profileElements = homePresenter.getProfileConfig();
                            if (initializer != null) {
                                initializer.launchProfileActivity(HomeActivity.this.getActivityContext(), profileElements
                                        , HomeActivity.this.getActivityContext().getResources().getString(R.string.fusionauth_api_key));
                            }
                            break;
                        case R.id.logout:
                            if (homePresenter.isNetworkConnected()) {
                                if (logoutListener == null)
                                    HomeActivity.this.initializeLogoutListener();
                                AncillaryScreensDriver.performLogout(HomeActivity.this, HomeActivity.this.getActivityContext().getResources().getString(R.string.fusionauth_api_key));
                            } else {
                                HomeActivity.this.showSnackbar("It seems you are offline. Logout cannot happen in offline conditions.", 3000);
                            }
                            break;
                    }
                    return true;
                }
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
                                homePresenter.resetODKData();
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

    @Override
    public void launchSearchModule() {
        CascadingModuleDriver.init( (MainApplication)getApplicationContext(), AppConstants.FILE_PATH, AppConstants.ROOT);
        CascadingModuleDriver.launchSearchView(getActivityContext(), AppConstants.ROOT + "/data2.json", 100);


    }

    @Override
    public void updateLocale(String language) {
        new LocaleHelper().updateLocale(getActivityContext(), language);
    }


}
