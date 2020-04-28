package com.samagra.ancillaryscreens.screens.login;

import com.samagra.ancillaryscreens.base.BaseInteractor;
import com.samagra.ancillaryscreens.data.network.BackendCallHelperImpl;
import com.samagra.ancillaryscreens.data.network.model.LoginRequest;
import com.samagra.ancillaryscreens.data.network.model.LoginResponse;
import com.samagra.ancillaryscreens.data.prefs.CommonsPreferenceHelper;
import com.samagra.ancillaryscreens.screens.login.LoginContract.Interactor;
import com.samagra.ancillaryscreens.screens.login.LoginContract.Presenter;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * This class interacts with the {@link Presenter} and the stored app data. The class abstracts
 * the source of the originating data - This means {@link Presenter} has no idea if the data provided
 * by the {@link Interactor} is from network, database or SharedPreferences
 *
 * @author Pranav Sharma
 */
public class LoginInteractor extends BaseInteractor implements LoginContract.Interactor {

    @Inject
    public LoginInteractor(CommonsPreferenceHelper preferenceHelper) {
        super(preferenceHelper);
    }

    /**
     * This function persists the user data received through {@link BackendCallHelperImpl#performLoginApiCall(LoginRequest)}
     * in {@link android.content.SharedPreferences} after parsing it.
     *
     * @param loginResponse - The {@link LoginResponse} received from {@link BackendCallHelperImpl#performLoginApiCall(LoginRequest)}
     */
    @Override
    public void persistUserData(LoginResponse loginResponse) {
        try {
            getPreferenceHelper().setCurrentUserLoginFlags();
            getPreferenceHelper().setCurrentUserDetailsFromLogin(loginResponse);
            getPreferenceHelper().setCurrentUserAdditionalDetailsFromLogin(loginResponse);
        } catch (Exception e) {
            Timber.e("Exception in persisting user data in shared prefs %s", e.getMessage());
            Timber.e(e);
        }
    }

    @Override
    public boolean isFirstLogin() {
        return getPreferenceHelper().isFirstLogin();
    }
}
