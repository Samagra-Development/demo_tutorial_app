package com.samagra.ancillaryscreens.screens.login;

import android.widget.EditText;

import com.samagra.ancillaryscreens.base.MvpInteractor;
import com.samagra.ancillaryscreens.base.MvpPresenter;
import com.samagra.ancillaryscreens.base.MvpView;
import com.samagra.ancillaryscreens.data.network.BackendCallHelperImpl;
import com.samagra.ancillaryscreens.data.network.model.LoginRequest;
import com.samagra.ancillaryscreens.data.network.model.LoginResponse;

/**
 * The interface contract for Login Screen. This interface contains the methods that the Model, View & Presenter
 * for Login Screen must implement
 *
 * @author Pranav Sharma
 */
public interface LoginContract {
    interface View extends MvpView {

        /**
         * Validates the login credentials inputted by the user and returns appropriate messages in case of failed login attempt.
         *
         * @param editTextUsername - The {@link EditText} in which user is supposed to type in the username.
         * @param editTextPassword - The {@link EditText} in which user is supposed to type in the password.
         * @return a boolean indicating the result of validation.
         */
        boolean validateInputs(EditText editTextUsername, EditText editTextPassword);


        void performLogin();

        void changePassword();

        /**
         * This function should be called to inform the UI that the Login Task has been completed <b>successfully</b>.
         * The UI update to reflect successful login should be done here.
         *
         * @param loginResponse - The response in the form of {@link LoginResponse} sent by the API.
         */
        void onLoginSuccess(LoginResponse loginResponse);

        /**
         * This function should be called to inform the UI that the Login Task has been completed <b>unsuccessfully</b>
         * The UI update to reflect unsuccessful login should be done here.
         */
        void onLoginFailed();

        void showProgressDialog();

        void hideProgressDialog();

        void finishActivity();
    }

    interface Interactor extends MvpInteractor {

        /**
         * This function persists the user data received through {@link BackendCallHelperImpl#performLoginApiCall(LoginRequest)}
         * in {@link android.content.SharedPreferences} after parsing it.
         *
         * @param loginResponse - The {@link LoginResponse} received from {@link BackendCallHelperImpl#performLoginApiCall(LoginRequest)}
         */
        void persistUserData(LoginResponse loginResponse);

        boolean isFirstLogin();
    }


    interface Presenter<V extends View, I extends Interactor> extends MvpPresenter<V, I> {

        /**
         * This function starts the Login process by accepting a {@link LoginRequest} and then executing it.
         *
         * @param loginRequest - The {@link LoginRequest} passed to make the API call via {@link BackendCallHelperImpl#performLoginApiCall(LoginRequest)}
         * @see BackendCallHelperImpl#performLoginApiCall(LoginRequest)
         */
        void startAuthenticationTask(LoginRequest loginRequest);


        /**
         * This function finishes the {@link LoginActivity} and starts the HomeActivity. The HomeActivity is outside this
         * module and can be any activity which has {@link com.samagra.commons.Constants#INTENT_LAUNCH_HOME_ACTIVITY} defined
         * as action in its intent-filter tag in AndroidManifest. This activity is started as a new task.
         */
        void finishAndMoveToHomeScreen();
    }
}
