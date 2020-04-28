package com.samagra.cascading_module;


import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;


import com.samagra.cascading_module.models.InstitutionInfo;
import com.samagra.cascading_module.ui.SearchActivity;
import com.samagra.commons.ExchangeObject;
import com.samagra.commons.MainApplication;
import com.samagra.commons.Modules;


/**
 * The driver class for this module, any screen that needs to be launched from outside this module, should be
 * launched using this class.
 * Note: It is essential that you call the {@link CascadingModuleDriver#init(MainApplication, String, String)} to initialise
 * the class prior to using it else an Exception will be thrown.
 *
 * @author Pranav Sharma
 */
public class CascadingModuleDriver {
    public static String BASE_API_URL;
    public static MainApplication application;
    public static String ROOT;

    /**
     *  @param mainApplication
     * @param BASE_URL
     * @param root
     */
    public static void init(@NonNull MainApplication mainApplication, @NonNull String BASE_URL, String root) {
        CascadingModuleDriver.BASE_API_URL = BASE_URL;
        CascadingModuleDriver.ROOT = root;
        CascadingModuleDriver.application = mainApplication;
    }

    public static void launchSearchView(Context context, String path, int requestCode) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    public static void sendBackData(InstitutionInfo selectedHospitalData) {
        ExchangeObject.DataExchangeObject dataExchangeObject = new ExchangeObject.DataExchangeObject<>(Modules.MAIN_APP, Modules.CASCADING_SEARCH,selectedHospitalData);
        application.getEventBus().send(dataExchangeObject);

    }

//    /**
//     * Function to launch the login screen from this module. This function starts the {@link LoginActivity} as a new task,
//     * clearing everything else in the activity back-stack.
//     *
//     * @param context - The current Activity's context.
//     */
//    public static void launchLoginScreen(@NonNull Context context) {
//        Intent intent = new Intent(context, LoginActivity.class);
//        CommonUtilities.startActivityAsNewTask(intent, context);
//    }
//




}
