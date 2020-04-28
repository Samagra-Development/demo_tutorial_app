package com.samagra.ancillaryscreens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.samagra.ancillaryscreens.models.AboutBundle;
import com.samagra.ancillaryscreens.screens.about.AboutActivity;
import com.samagra.ancillaryscreens.screens.login.LoginActivity;
import com.samagra.ancillaryscreens.screens.tutorials.TutorialActivity;
import com.samagra.commons.CommonUtilities;
import com.samagra.commons.CustomEvents;
import com.samagra.commons.ExchangeObject;
import com.samagra.commons.MainApplication;
import com.samagra.commons.Modules;

import timber.log.Timber;

/**
 * The driver class for this module, any screen that needs to be launched from outside this module, should be
 * launched using this class.
 * Note: It is essential that you call the {@link AncillaryScreensDriver#init(MainApplication, String, String, String, String)} to initialise
 * the class prior to using it else an {@link InvalidConfigurationException} will be thrown.
 *
 * @author Pranav Sharma
 */
public class AncillaryScreensDriver {
    public static String UPDATE_PASSWORD_URL;
    public static MainApplication mainApplication = null;
    public static String BASE_API_URL;
    public static String SEND_OTP_URL;
    public static String APPLICATION_ID;

    /**
     *
     * @param mainApplication
     * @param BASE_URL
     * @param SEND_OTP_URL
     * @param UPDATE_PASSWORD_URL
     * @param APPLICATION_ID
     */
    public static void init(@NonNull MainApplication mainApplication, @NonNull String BASE_URL, @NonNull String SEND_OTP_URL,
                            @NonNull String UPDATE_PASSWORD_URL, @NonNull String APPLICATION_ID) {
        AncillaryScreensDriver.mainApplication = mainApplication;
        AncillaryScreensDriver.BASE_API_URL = BASE_URL;
        AncillaryScreensDriver.SEND_OTP_URL = SEND_OTP_URL;
        AncillaryScreensDriver.UPDATE_PASSWORD_URL = UPDATE_PASSWORD_URL;
        AncillaryScreensDriver.APPLICATION_ID = APPLICATION_ID;
    }

    /**
     * Function to launch the login screen from this module. This function starts the {@link LoginActivity} as a new task,
     * clearing everything else in the activity back-stack.
     *
     * @param context - The current Activity's context.
     */
    public static void launchLoginScreen(@NonNull Context context) {
        checkValidConfig();
        Intent intent = new Intent(context, LoginActivity.class);
        CommonUtilities.startActivityAsNewTask(intent, context);
    }

    /**
     * Function to launch the about activity from this module.
     *
     * @param context     - The current Activity's context.
     * @param aboutBundle - A Java object containing values to configure {@link AboutActivity}'s appearance.
     * @see AboutBundle#AboutBundle(String, String, String, int, int, int)
     */
    public static void launchAboutActivity(@NonNull Context context, @NonNull AboutBundle aboutBundle) {
        checkValidConfig();
        Intent intent = new Intent(context, AboutActivity.class);
        intent.putExtra("config", aboutBundle.aboutExchangeBundle);
        context.startActivity(intent);
    }

    /**
     * Function to launch the about activity from this module.
     *
     * @param context     - The current Activity's context.
     * @param videoDetailsBundle - A Java object containing values to configure {@link AboutActivity}'s appearance.
     * @see AboutBundle#AboutBundle(String, String, String, int, int, int)
     */
    public static void launchTutorialActivity(@NonNull Context context, @NonNull Bundle videoDetailsBundle) {
        checkValidConfig();
        Intent intent = new Intent(context, TutorialActivity.class);
        intent.putExtra("video_details_bundle", videoDetailsBundle);
        context.startActivity(intent);
    }


    /**
     * Function to perform the logout operations. This function uses the updates the flags and other data set by the
     * {@link LoginActivity} to reflect that the user has logged out. This function also notifies the logout progress using
     * RxJava EventBus system
     *
     * @param context - The current Activity's context.
     * @see AncillaryScreensDriver#notifyLogoutInitiated()
     * @see AncillaryScreensDriver#notifyLogoutCompleted()
     */
    public static void performLogout(@NonNull Context context) {
        // TODO : Logout button => Logout from fusionAuth => Update user by removing registration token => Login splash_ss
        checkValidConfig();
        notifyLogoutInitiated();
        notifyLogoutCompleted();
        logoutUserLocally(context);
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("loggedOut", true);
        CommonUtilities.startActivityAsNewTask(intent, context);
    }

    /**
     * This function updates the login related flags set by the {@link LoginActivity} to reflect that the user is now
     * logged out.
     *
     * @param context - The current Activity/Application context.
     */
    private static void logoutUserLocally(@NonNull Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("updatedMappingThroughFirebase2");
        editor.remove("formVersion");
        editor.apply();
    }

    /**
     * Function to notify the {@link com.samagra.commons.RxBus} that logout process has been <b>initiated</b>.
     * This function sends an {@link com.samagra.commons.ExchangeObject.EventExchangeObject} on current {@link com.samagra.commons.RxBus}
     * so that any activities subscribed to it can be notified of the event.
     *
     * @see com.samagra.commons.ExchangeObject.EventExchangeObject#EventExchangeObject(Modules, Modules, CustomEvents)
     * @see CustomEvents#LOGOUT_INITIATED
     */
    private static void notifyLogoutInitiated() {
        Timber.i("Logout initiated");
        ExchangeObject.EventExchangeObject eventExchangeObject = new ExchangeObject.EventExchangeObject(Modules.MAIN_APP, Modules.ANCILLARY_SCREENS, CustomEvents.LOGOUT_INITIATED);
        mainApplication.getEventBus().send(eventExchangeObject);
    }

    /**
     * Function to notify the {@link com.samagra.commons.RxBus} that logout process has been <b>completed</b>.
     * This function sends an {@link com.samagra.commons.ExchangeObject.EventExchangeObject} on current {@link com.samagra.commons.RxBus}
     * so that any activities subscribed to it can be notified of the event.
     *
     * @see com.samagra.commons.ExchangeObject.EventExchangeObject#EventExchangeObject(Modules, Modules, CustomEvents)
     * @see CustomEvents#LOGOUT_COMPLETED
     */
    private static void notifyLogoutCompleted() {
        Timber.i("Logout completed");
        ExchangeObject.EventExchangeObject eventExchangeObject = new ExchangeObject.EventExchangeObject(Modules.MAIN_APP, Modules.ANCILLARY_SCREENS, CustomEvents.LOGOUT_COMPLETED);
        mainApplication.getEventBus().send(eventExchangeObject);
    }

    /**
     * Function to check if the mainApplication is initialised indicating if {@link AncillaryScreensDriver#init(MainApplication, String)} is called or not.
     * If not, it throws {@link InvalidConfigurationException}
     *
     * @throws InvalidConfigurationException - This Exception means that the module is not configured by the user properly. The exception generates
     *                                       detailed message depending on the class that throws it.
     */
    private static void checkValidConfig() {
        if (mainApplication == null)
            throw new InvalidConfigurationException(AncillaryScreensDriver.class);
    }
}
