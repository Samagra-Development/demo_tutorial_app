package com.samagra.ancillaryscreens.screens.splash;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.samagra.ancillaryscreens.AncillaryScreensDriver;
import com.samagra.ancillaryscreens.BuildConfig;
import com.samagra.ancillaryscreens.base.BasePresenter;
import com.samagra.ancillaryscreens.data.network.BackendCallHelper;
import com.samagra.commons.Constants;
import com.samagra.commons.ExchangeObject;
import com.samagra.commons.Modules;


import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * The presenter for the Splash Screen. This class controls the interactions between the View and the data.
 * Must implement {@link com.samagra.ancillaryscreens.screens.splash.SplashContract.Presenter}
 *
 * @author Pranav Sharma
 */
public class SplashPresenter<V extends SplashContract.View, I extends SplashContract.Interactor> extends BasePresenter<V, I> implements SplashContract.Presenter<V, I> {

    private static final boolean EXIT = true;

    @Inject
    public SplashPresenter(I mvpInteractor, BackendCallHelper apiHelper, CompositeDisposable compositeDisposable) {
        super(mvpInteractor, apiHelper, compositeDisposable);
    }

    /**
     * Decides the next screen and moves to the decided screen.
     * This decision is based on the Login status which is managed by the {@link com.samagra.ancillaryscreens.screens.login.LoginActivity}
     * in this module.
     *
     * @see com.samagra.ancillaryscreens.screens.login.LoginActivity
     * @see com.samagra.ancillaryscreens.data.prefs.CommonsPrefsHelperImpl
     */
    @Override
    public void moveToNextScreen() {
        if (getMvpInteractor().isLoggedIn()) {
            Timber.d("Moving to Home");
            Intent intent = new Intent(Constants.INTENT_LAUNCH_HOME_ACTIVITY);
            ExchangeObject.SignalExchangeObject signalExchangeObject = new ExchangeObject.SignalExchangeObject(Modules.MAIN_APP, Modules.ANCILLARY_SCREENS, intent, true);
            AncillaryScreensDriver.mainApplication.getEventBus().send(signalExchangeObject);
        } else {
            Timber.d("Launching Login");
            AncillaryScreensDriver.launchLoginScreen(getMvpView().getActivityContext());
        }
    }

    /**
     * This function initialises the {@link SplashActivity} by setting up the layout and updating necessary flags in
     * the {@link android.content.SharedPreferences}.
     */
    @Override
    public void init() {
        getMvpView().showActivityLayout();
        PackageInfo packageInfo = null;
        try {
            packageInfo = getMvpView().getActivityContext().getPackageManager()
                    .getPackageInfo(getMvpView().getActivityContext().getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e, "Unable to get package info");
        }

        boolean firstRun = getMvpInteractor().isFirstRun();
        boolean showSplash = getMvpInteractor().isShowSplash();

        // if you've increased version code, then update the version number and set firstRun to true
        boolean appUpdated = getMvpInteractor().updateVersionNumber(packageInfo);
        if (appUpdated)
            firstRun = true;

        if (firstRun || showSplash)
            getMvpInteractor().updateFirstRunFlag(false);
        getMvpView().showSimpleSplash();
        updateCurrentVersion();
    }


    private void updateCurrentVersion(){
        int currentVersion = BuildConfig.VERSION_CODE;
        int previousSavedVersion = getMvpInteractor().getPreferenceHelper().getPreviousVersion();
        if(previousSavedVersion < currentVersion){
            getMvpInteractor().getPreferenceHelper().updateAppVersion(currentVersion);
            Timber.e("Up version detected");
        }
    }
}
