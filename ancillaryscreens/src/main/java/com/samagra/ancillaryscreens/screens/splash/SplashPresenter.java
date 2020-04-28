package com.samagra.ancillaryscreens.screens.splash;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.google.android.material.snackbar.Snackbar;
import com.samagra.ancillaryscreens.AncillaryScreensDriver;
import com.samagra.ancillaryscreens.BuildConfig;
import com.samagra.ancillaryscreens.R;
import com.samagra.ancillaryscreens.base.BasePresenter;
import com.samagra.ancillaryscreens.data.network.BackendCallHelper;
import com.samagra.commons.Constants;
import com.samagra.commons.ExchangeObject;
import com.samagra.commons.Modules;
import com.samagra.commons.firebase.FirebaseUtilitiesWrapper;
import com.samagra.commons.firebase.IFirebaseRemoteStorageFileDownloader;
import com.samagra.commons.utils.AlertDialogUtils;
import com.samagra.commons.utils.FileUnzipper;
import com.samagra.commons.utils.UnzipTaskListener;


import org.odk.collect.android.contracts.AppPermissionUserActionListener;
import org.odk.collect.android.contracts.IFormManagementContract;
import org.odk.collect.android.contracts.PermissionsHelper;
import org.odk.collect.android.utilities.ApplicationConstants;

import java.io.File;

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

    private static final String ROOT = Environment.getExternalStorageDirectory()
            + File.separator + "odk";
    private static final boolean EXIT = true;

    @Inject
    public SplashPresenter(I mvpInteractor, BackendCallHelper apiHelper, CompositeDisposable compositeDisposable, IFormManagementContract iFormManagementContract) {
        super(mvpInteractor, apiHelper, compositeDisposable, iFormManagementContract);
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
            getIFormManagementContract().resetEverythingODK();
            getIFormManagementContract().resetODKForms(getMvpView().getActivityContext());
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
        startUnzipTask();
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

    @Override
    public void requestStoragePermissions() {
        PermissionsHelper permissionUtils = new PermissionsHelper();
        if (!PermissionsHelper.areStoragePermissionsGranted(getMvpView().getActivityContext())) {
            permissionUtils.requestStoragePermissions((SplashActivity) getMvpView().getActivityContext(), new AppPermissionUserActionListener() {
                @Override
                public void granted() {
                    try {
                        getIFormManagementContract().createODKDirectories();
                    } catch (RuntimeException e) {
                        AlertDialogUtils.showDialog(AlertDialogUtils.createErrorDialog((SplashActivity) getMvpView().getActivityContext(),
                                e.getMessage(), EXIT), (SplashActivity) getMvpView().getActivityContext());
                        return;
                    }
                    init();
                }

                @Override
                public void denied() {
                    getMvpView().finishActivity();
                }
            });
        } else {
            init();
        }
    }


    @Override
    public void startUnzipTask() {
        FileUnzipper fileUnzipper = new FileUnzipper(getMvpView().getActivityContext(), ROOT + "/data2.json", R.raw.data2, new UnzipTaskListener() {
            @Override
            public void unZipSuccess() {
                getMvpView().showSnackbar("Remote file from Firebase has been unzipped successfully", Snackbar.LENGTH_LONG);
            }

            @Override
            public void unZipFailure(Exception exception) {
                getMvpView().showSnackbar("Remote file from Firebase couldn't be downloaded. Using local file only", Snackbar.LENGTH_LONG);
            }
        });
        fileUnzipper.unzipFile();
//        getMvpView().renderLayoutVisible();
    }

    @Override
    public void downloadFirebaseRemoteStorageConfigFile() {
        FirebaseUtilitiesWrapper.downloadFile(ROOT + "/data2.json.gzip", new IFirebaseRemoteStorageFileDownloader() {

            @Override
            public void onFirebaseRemoteStorageFileDownloadFailure(Exception exception) {
                getMvpView().showSnackbar("Remote file from Firebase failed with error. " + exception.getMessage() + " Using local file only, ", Snackbar.LENGTH_LONG);
                startUnzipTask();
            }

            @Override
            public void onFirebaseRemoteStorageFileDownloadProgressState(long progressPercentage) {

            }

            @Override
            public void onFirebaseRemoteStorageFileDownloadSuccess() {
                getMvpView().showSnackbar("Remote file from Firebase has been downloaded successfully", Snackbar.LENGTH_LONG);
                startUnzipTask();
            }
        });
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
