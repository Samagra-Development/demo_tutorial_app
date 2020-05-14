package com.samagra.parent.ui.HomeScreen;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;
import com.samagra.commons.MainApplication;
import com.samagra.commons.utils.FormDownloadStatus;
import com.samagra.parent.AppConstants;
import com.samagra.parent.MyApplication;
import com.samagra.parent.R;
import com.samagra.parent.UtilityFunctions;
import com.samagra.parent.base.BasePresenter;
import com.samagra.user_profile.profile.UserProfileElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.odk.collect.android.contracts.DataFormDownloadResultCallback;
import org.odk.collect.android.contracts.FormListDownloadResultCallback;
import org.odk.collect.android.contracts.IFormManagementContract;
import org.odk.collect.android.logic.FormDetails;
import org.odk.collect.android.utilities.LocaleHelper;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * The Presenter class for Home Screen. This class controls interaction between the View and Data.
 * This class <b>must</b> implement the {@link HomeMvpPresenter} and <b>must</b> be a type of {@link BasePresenter}.
 *
 * @author Pranav Sharma
 */
public class HomePresenter<V extends HomeMvpView, I extends HomeMvpInteractor> extends BasePresenter<V, I> implements HomeMvpPresenter<V, I> {

    private FormDownloadStatus formsDownloadStatus = FormDownloadStatus.FAILURE;


    @Inject
    public HomePresenter(I mvpInteractor, IFormManagementContract iFormManagementContract) {
        super(mvpInteractor, iFormManagementContract);
    }

    @Override
    public void onFillFormsOptionClicked() {
//        getMvpView().launchSearchModule();
        if (getMvpView() != null) {

            getIFormManagementContract().launchSpecificDataForm(getMvpView().getActivityContext(), "Test Homework");
        }
    }

    @Override
    public void onViewSubmittedFormsOptionsClicked() {
        if (getMvpView() != null) {
            getIFormManagementContract().launchViewSubmittedFormsView(getMvpView().getActivityContext(),
                    UtilityFunctions.generateToolbarModificationObject(true,
                            R.drawable.ic_arrow_back_white_24dp,
                            getMvpView().getActivityContext().getResources().getString(R.string.view_issues), true));
        }
    }


    @Override
    public void onSubmitFormsClicked() {
        if (getMvpView() != null) {
            getIFormManagementContract().launchViewUnsubmittedFormView(getMvpView().getActivityContext(), getClass().getName());
        }
    }

    @Override
    public void onViewHelplineClicked() {
        if (getMvpView() != null) {
            Intent i = new Intent(getMvpView().getActivityContext(), ComingSoon.class);
            getMvpView().getActivityContext().startActivity(i);
        }
    }


    @Override
    public boolean isNetworkConnected() {
        if (getMvpView() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getMvpView()
                    .getActivityContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        } else {
            return MyApplication.isOnline;
        }
    }

    @Override
    public String getYoutubeAPIKey() {
        String youtubeAPIKey = "";
        if (MyApplication.getmFirebaseRemoteConfig() != null) {
            youtubeAPIKey = MyApplication.getmFirebaseRemoteConfig().getString("youtube_api_key");
        }
        return youtubeAPIKey;
    }

    @Override
    public String getTutorialVideoID() {
        String youtubeTutorialVideoID = "";
        if (MyApplication.getmFirebaseRemoteConfig() != null) {
            youtubeTutorialVideoID = MyApplication.getmFirebaseRemoteConfig().getString("youtube_tutorial_video_id");
        }
        return youtubeTutorialVideoID;
    }

    @Override
    public void applySettings() {
        getIFormManagementContract().applyODKCollectSettings(getMvpView().getActivityContext(), R.raw.settings);
    }


    private boolean isUpversioned(String version, String previousVersion) {
        try {
            return Integer.parseInt(version) > Integer.parseInt(previousVersion);
        } catch (Exception e) {
            return false;
        }
    }


    private String getUserRoleFromPref() {
        return "Sample";
//        return getMvpInteractor().getPreferenceHelper().getUserRoleFromPref(); //Viewing and download of forms is based on User's role, you can configure it via Preferences when logging in as per User's Login response
    }


    private String getRoleFromRoleMappingFirebase(String userRole) {
        if (userRole.equals("")) return "all_grades";
        class RoleMapping {
            private String Designation;
            private String Role;
            private RoleMapping(String Designation, String Role) {
                this.Designation = Designation;
                this.Role = Role;
            }
        }


        String roleMapping = MyApplication.getmFirebaseRemoteConfig().getString("role_mapping");
        Timber.e("Role Mapping :: ");
        Timber.e("Role Mapping from firebase  is %s", roleMapping);
        String role = "";
        ArrayList<RoleMapping> roleMappings = new ArrayList<>();
        if (!roleMapping.equals("") && !userRole.equals("")) {
            try {
                boolean found = false;
                JSONArray jsonArray = new JSONArray(roleMapping);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    roleMappings.add(new RoleMapping(
                            object.getString("Designation"),
                            object.getString("Role"))
                    );
                    if (roleMappings.get(i).Designation.equals(userRole)) {
                        found = true;
                        role = roleMappings.get(i).Role;
                    }
                }
                if (!found) role = "All";
            } catch (JSONException e) {
                e.printStackTrace();
                role = "All";
            }
        }
        return role;
    }

    @Override
    public void checkForFormUpdates() {
        String latestFormVrsion = MyApplication.getmFirebaseRemoteConfig().getString("version");
        String previousVersion = getMvpInteractor().getPreferenceHelper().getFormVersion();
        String formsString = MyApplication.getmFirebaseRemoteConfig().getString(getRoleFromRoleMappingFirebase(getUserRoleFromPref()));
        formsDownloadStatus = FormDownloadStatus.DOWNLOADING;
        Timber.e("Are forms matching: %s", getIFormManagementContract().checkIfODKFormsMatch(formsString));
        if (isUpversioned(latestFormVrsion, previousVersion) || !getIFormManagementContract().checkIfODKFormsMatch(formsString)) {
            getMvpInteractor().getPreferenceHelper().updateFormVersion(latestFormVrsion);
            // Downloading new forms list.
            getIFormManagementContract().startDownloadODKFormListTask(new FormListDownloadListener());
            formsDownloadStatus = FormDownloadStatus.DOWNLOADING;
        } else {
            if (getMvpView() != null) {
                Timber.d("Rendering UI Visible as forms already downloaded");
                getMvpView().renderLayoutVisible();
            }
            formsDownloadStatus = FormDownloadStatus.SUCCESS;
        }
    }

    @Override
    public ArrayList<UserProfileElement> getProfileConfig() {
        String configString = MyApplication.getmFirebaseRemoteConfig().getString("profile_config");
        ArrayList<UserProfileElement> userProfileElements = new ArrayList<>();
        try {
            JSONArray config = new JSONArray(configString);
            for (int i = 0; i < config.length(); i++) {
                JSONArray spinnerExtra = config.getJSONObject(i).optJSONArray("spinnerExtra");
                ArrayList<String> spinnerValues = null;
                if (spinnerExtra != null) {
                    spinnerValues = new ArrayList<>();
                    for (int j = 0; j < spinnerExtra.length(); j++) {
                        spinnerValues.add(spinnerExtra.get(j).toString());
                    }
                }
                userProfileElements.add(new UserProfileElement(config.getJSONObject(i).get("base64Icon").toString(),
                        config.getJSONObject(i).get("title").toString(),
                        config.getJSONObject(i).get("content").toString(),
                        (Boolean) config.getJSONObject(i).get("isEditable"),
                        (int) config.getJSONObject(i).get("section"),
                        UserProfileElement.ProfileElementContentType.valueOf(config.getJSONObject(i).get("type").toString()),
                        spinnerValues,
                        getMvpInteractor().getPreferenceHelper().getValueForKey(config.getJSONObject(i).get("content").toString())
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userProfileElements;
    }

    String fetchUserID() {
       return getMvpInteractor().getPreferenceHelper().getCurrentUserId();
    }


    @Override
    public void updateLanguageSettings() {
        String language = getMvpInteractor().getPreferenceHelper().updateAppLanguage();
        getMvpView().updateLocale(language);
    }

    @Override
    public void resetODKData() {
        getIFormManagementContract().resetEverythingODK();
    }

    class FormListDownloadListener implements FormListDownloadResultCallback {
        @Override
        public void onSuccessfulFormListDownload(HashMap<String, FormDetails> latestFormListFromServer) {
            Timber.d("FormList download complete %s", latestFormListFromServer);
            String formsString = MyApplication.getmFirebaseRemoteConfig().getString(getRoleFromRoleMappingFirebase(getUserRoleFromPref()));
            HashMap<String, String> userRoleBasedForms = getIFormManagementContract().downloadFormList(formsString);
            // Download Forms if updates available or if forms not downloaded. Delete forms if not applied for the role.
            HashMap<String, FormDetails> formsToBeDownloaded = getIFormManagementContract().downloadNewFormsBasedOnDownloadedFormList(userRoleBasedForms, latestFormListFromServer);
            if (formsToBeDownloaded.size() > 0)
                formsDownloadStatus = FormDownloadStatus.DOWNLOADING;
            else {
                formsDownloadStatus = FormDownloadStatus.SUCCESS;
            }
            if (formsDownloadStatus == FormDownloadStatus.DOWNLOADING)
                getIFormManagementContract().downloadODKForms(new FormDownloadListener(), formsToBeDownloaded);
        }

        @Override
        public void onFailureFormListDownload(boolean isAPIFailure) {
            if (isAPIFailure) {
                Timber.e("There has been an error in downlaoding the forms from Aggregagte");
                getMvpView().showSnackbar("There has been an error in downloading the forms from Aggregagte. \n" +
                        "Please check if URL is valid or not and ODK configs are alright.", Snackbar.LENGTH_LONG);
                getMvpView().renderLayoutVisible();
                formsDownloadStatus = FormDownloadStatus.FAILURE;
            }
            //+ Show error Message
//            checkForFormUpdates();
        }
    }


    class FormDownloadListener implements DataFormDownloadResultCallback {
        @Override
        public void formsDownloadingSuccessful(HashMap<FormDetails, String> result) {
            Timber.d("Form Download Complete %s", result);
            formsDownloadStatus = FormDownloadStatus.SUCCESS;
            if (getMvpView() != null)
                getMvpView().renderLayoutVisible();
        }

        @Override
        public void formsDownloadingFailure() {

        }

        @Override
        public void progressUpdate(String currentFile, int progress, int total) {
            Timber.v("Form Download InProgress = " + currentFile + " Progress" + progress + " Out of=" + total);
            Timber.d(String.valueOf(total));
            Timber.d(String.valueOf(progress));
            int formProgress = (progress * 100) / total;
            Timber.d("Form Download Progress: %s", formProgress);
            if (formProgress == 100) {
                if (getMvpView() != null) {
                    Timber.d("Rendering UI Visible as forms already downloadded not, but now downloaded");
                    getMvpView().renderLayoutVisible();
                    getMvpView().showSnackbar("ODK forms as requested have been downloaded.", Snackbar.LENGTH_LONG);
                }
                formsDownloadStatus = FormDownloadStatus.SUCCESS;
            }
        }

        @Override
        public void formsDownloadingCancelled() {
            getMvpView().showSnackbar("Unable to download the forms.", Snackbar.LENGTH_LONG);
            Timber.e("Form Download Cancelled");
        }
    }


}