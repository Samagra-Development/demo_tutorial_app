package com.samagra.ancillaryscreens.screens.splash;

import android.content.pm.PackageInfo;

import com.samagra.ancillaryscreens.base.MvpInteractor;
import com.samagra.ancillaryscreens.base.MvpPresenter;
import com.samagra.ancillaryscreens.base.MvpView;
import com.samagra.commons.MainApplication;

/**
 * The interface contract for Splash Screen. This interface contains the methods that the Model, View & Presenter
 * for Splash Screen must implement
 *
 * @author Pranav Sharma
 */
public interface SplashContract {
    interface View extends MvpView {
        void endSplashScreen();

        /**
         * This function configures the Splash Screen
         * and renders it on screen. This includes the Splash screen image and other UI configurations.
         *
         */
        void showSimpleSplash();

        void finishActivity();

        /**
         * This function sets the activity layout and binds the UI Views.
         * This function should be called after the relevant permissions are granted to the app by the user
         */
        void showActivityLayout();
    }

    interface Interactor extends MvpInteractor {
        boolean isFirstRun();

        boolean isShowSplash();

        /**
         * This function updates the version number and sets firstRun flag to true.
         * Call this method if you have for some reason updated the version code of the app.
         *
         * @param packageInfo - {@link PackageInfo} to get the the current version code of the app.
         * @return boolean - {@code true} if current package version code is higher than the stored version code
         * (indicating an app update), {@code false} otherwise
         */
        boolean updateVersionNumber(PackageInfo packageInfo);

        /**
         * Updates the first Run flag according to the conditions.
         *
         * @param value - the updated value of the first run flag
         */
        void updateFirstRunFlag(boolean value);

        boolean isLoggedIn();
    }

    interface Presenter<V extends View, I extends Interactor> extends MvpPresenter<V, I> {
        /**
         * Decides the next screen and moves to the decided screen.
         * This decision is based on the Login status which is managed by the {@link com.samagra.ancillaryscreens.screens.login.LoginActivity}
         * in this module.
         *
         * @see com.samagra.ancillaryscreens.screens.login.LoginActivity
         * @see com.samagra.ancillaryscreens.data.prefs.CommonsPrefsHelperImpl
         */
        void moveToNextScreen();

        void init();
    }
}
