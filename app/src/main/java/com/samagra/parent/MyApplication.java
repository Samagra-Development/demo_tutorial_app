package com.samagra.parent;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.samagra.ancillaryscreens.AncillaryScreensDriver;
import com.samagra.ancillaryscreens.di.FormManagementCommunicator;
import com.samagra.commons.CommonUtilities;
import com.samagra.commons.ExchangeObject;
import com.samagra.commons.InternetMonitor;
import com.samagra.commons.InternetStatus;
import com.samagra.commons.MainApplication;
import com.samagra.commons.Modules;
import com.samagra.commons.NetworkConnectionInterceptor;
import com.samagra.commons.RxBus;
import com.samagra.commons.TaskScheduler.Manager;
import com.samagra.commons.utils.AlertDialogUtils;
import com.samagra.notification_module.AppNotificationUtils;
import com.samagra.parent.di.component.ApplicationComponent;
import com.samagra.parent.di.component.DaggerApplicationComponent;
import com.samagra.parent.di.modules.ApplicationModule;
import com.samagra.parent.helper.OkHttpClientProvider;

import org.odk.collect.android.application.Collect;
import org.odk.collect.android.application.FormManagmentModuleInitialisationListener;
import org.odk.collect.android.contracts.ComponentManager;
import org.odk.collect.android.contracts.FormManagementSectionInteractor;
import org.odk.collect.android.preferences.GeneralSharedPreferences;
import org.odk.collect.android.utilities.LocaleHelper;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import timber.log.Timber;

import static org.odk.collect.android.preferences.GeneralKeys.KEY_APP_LANGUAGE;


/**
 * The {@link Application} class for the app. This extends {@link Application} because the app module has a dependency on
 * the odk-collect library. Also, since the app module expresses a dependency on the commons module, the {@link Application}
 * class for app module must implement the {@link MainApplication}.
 *
 * @author Pranav Sharma
 * @see MainApplication
 */
public class MyApplication extends Application implements MainApplication, LifecycleObserver {

    protected ApplicationComponent applicationComponent;

    private Activity currentActivity = null;
    private RxBus eventBus = null;
    private static CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static FirebaseRemoteConfig mFirebaseRemoteConfig;
    public static boolean isOnline = true;

    /**
     * All the external modules must be initialised here. This includes any modules that have an init
     * function in their drivers. Also, any application level subscribers for the event bus,
     * in this case {@link RxBus} must be defined here.
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Collect.getInstance().init(this, getApplicationContext(), new FormManagmentModuleInitialisationListener() {
            @Override
            public void onSuccess() {
                Timber.d("Form Module has been initialised correctly");
            }

            @Override
            public void onFailure(String message) {
                Timber.d("Form Module could not be initialised correctly");
                AlertDialogUtils.createErrorDialog(getApplicationContext(), "Could not start app as Form Module couldn't be initialised properly.", true);
            }
        });
        eventBus = new RxBus();
        setupRemoteConfig();
        setupActivityLifecycleListeners();
        InternetMonitor.init(this);
        initializeFormManagementPackage();
        AppNotificationUtils.createNotificationChannel(this);
        AncillaryScreensDriver.init(this, AppConstants.BASE_API_URL,
                AppConstants.SEND_OTP_URL,
                AppConstants.UPDATE_PASSWORD_URL,
                getApplicationId());
        initBus();
    }

    private void initializeFormManagementPackage() {
        ComponentManager.registerFormManagementPackage(new FormManagementSectionInteractor());
        FormManagementCommunicator.setContract(ComponentManager.iFormManagementContract);
        ComponentManager.iFormManagementContract.setODKModuleStyle(this, R.drawable.login_bg, R.style.BaseAppTheme,
                R.style.FormEntryActivityTheme, R.style.BaseAppTheme_SettingsTheme_Dark, Long.MAX_VALUE);
    }
    public static String getApplicationId() {
        return "4b49c1c8-f90e-41e9-99ab-16d4af9eb269";
    }

    private void initBus() {
        compositeDisposable.add(this.getEventBus()
                .toObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(exchangeObject -> {
                    if (exchangeObject instanceof ExchangeObject) {
                        if (((ExchangeObject) exchangeObject).to == Modules.MAIN_APP
                                && ((ExchangeObject) exchangeObject).from == Modules.ANCILLARY_SCREENS
                                && isSignalExchangeType((ExchangeObject) exchangeObject)) {
                            ExchangeObject.SignalExchangeObject signalExchangeObject = (ExchangeObject.SignalExchangeObject) exchangeObject;
                            if (signalExchangeObject.shouldStartAsNewTask)
                                CommonUtilities.startActivityAsNewTask(signalExchangeObject.intentToLaunch, currentActivity);
                            else
                                startActivity(signalExchangeObject.intentToLaunch);
                        } else if (exchangeObject instanceof ExchangeObject.EventExchangeObject) {
                            // TODO : Remove this just for test
                            ExchangeObject.EventExchangeObject eventExchangeObject = (ExchangeObject.EventExchangeObject) exchangeObject;
                            Timber.d("Event Received %s ", eventExchangeObject.customEvents);
                            if (eventExchangeObject.to == Modules.MAIN_APP || eventExchangeObject.to == Modules.PROJECT) {
                                Timber.d("Event Received %s ", eventExchangeObject.customEvents);
                            }
                        } else if (exchangeObject instanceof ExchangeObject.NotificationExchangeObject) {
                            PendingIntent pendingIntent = ((ExchangeObject.NotificationExchangeObject) exchangeObject).data.getIntent();
                            int notificationID = ((ExchangeObject.NotificationExchangeObject) exchangeObject).data.getNotificationID();
                            int title = ((ExchangeObject.NotificationExchangeObject) exchangeObject).data.getTitle();
                            String body = ((ExchangeObject.NotificationExchangeObject) exchangeObject).data.getBody();
                            AppNotificationUtils.showNotification(getApplicationContext(), pendingIntent, notificationID, title, body);
                            Timber.d("Event Received for Push Notification %s ", title);
                        } else if(!((((ExchangeObject) exchangeObject).type != ExchangeObject.ExchangeObjectTypes.SIGNAL) && ((InternetStatus) ((ExchangeObject.DataExchangeObject) exchangeObject).data).isCurrentStatus())){
                            try{
                                boolean status = ((InternetStatus) ((ExchangeObject.DataExchangeObject) exchangeObject).data).isCurrentStatus();
                                updateInternetStatus(status);
                            }catch (Exception e){}
                        }else {
                            Timber.e("Exchange Object");
                            Timber.e(exchangeObject.toString());
                            Timber.e("Received but not intended");
                        }
                    }
                }, Timber::e));
    }

    private boolean isSignalExchangeType(ExchangeObject exchangeObject) {
        return exchangeObject.type == ExchangeObject.ExchangeObjectTypes.SIGNAL;
    }

    @Override
    public void updateInternetStatus(Boolean status) {
        isOnline = status;
    }

    @Override
    public boolean isOnline(){
        return isOnline;
    }

    @Override
    public OkHttpClient provideOkHttpClient(){
        NetworkConnectionInterceptor networkConnectionInterceptor = OkHttpClientProvider.getInterceptor(this);
        return OkHttpClientProvider.provideOkHttpClient(networkConnectionInterceptor);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    /**
     * Must provide a {@link androidx.annotation.NonNull} activity instance of the activity running in foreground.
     * You can use {@link Application#registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks)} to
     * get the currently resumed activity (activity in foreground)
     */
    @Override
    public Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * Must provide a {@link androidx.annotation.NonNull} instance of the current {@link Application}.
     */
    @Override
    public Application getCurrentApplication() {
        return this;
    }

    /**
     * Must provide a {@link androidx.annotation.NonNull} instance of {@link RxBus} which acts as an event bus
     * for the app.
     */
    @Override
    public RxBus getEventBus() {
        return bus();
    }

    /**
     * Optional method to teardown a module after its use is complete.
     * Not all modules require to be teared down.
     */
    @Override
    public void teardownModule(Modules module) {

    }

    private void setupActivityLifecycleListeners() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                currentActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public void setupRemoteConfig() {
        FirebaseApp.initializeApp(this);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings).addOnCompleteListener(task -> mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                Timber.e("Remote config activate successful. Config params updated :: %s", task1.getResult());
            } else {
                Timber.e("Remote config activate failed.");
            }
        }));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Collect.defaultSysLanguage = newConfig.locale.getLanguage();
        boolean isUsingSysLanguage = GeneralSharedPreferences.getInstance().get(KEY_APP_LANGUAGE).equals("");
        if (!isUsingSysLanguage) {
            new LocaleHelper().updateLocale(this);
        }

    }

    public static FirebaseRemoteConfig getmFirebaseRemoteConfig() {
        return mFirebaseRemoteConfig;
    }

    private RxBus bus() {
        return eventBus;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onAppBackgrounded() {
        InternetMonitor.stopMonitoringInternet();
        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }


    /**
     * Returns the Lifecycle of the provider.
     *
     * @return The lifecycle of the provider.
     */
    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return ProcessLifecycleOwner.get().getLifecycle();
    }
}
