package com.samagra.ancillaryscreens.screens.login;

import android.content.Intent;

import com.androidnetworking.error.ANError;
import com.samagra.ancillaryscreens.AncillaryScreensDriver;
import com.samagra.ancillaryscreens.base.BasePresenter;
import com.samagra.ancillaryscreens.data.network.BackendCallHelper;
import com.samagra.ancillaryscreens.data.network.BackendCallHelperImpl;
import com.samagra.ancillaryscreens.data.network.model.LoginRequest;
import com.samagra.commons.Constants;
import com.samagra.commons.ExchangeObject;
import com.samagra.commons.Modules;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * The presenter for the Login Screen. This class controls the interactions between the View and the data.
 * Must implement {@link com.samagra.ancillaryscreens.screens.login.LoginContract.Presenter}
 *
 * @author Pranav Sharma
 */
public class LoginPresenter<V extends LoginContract.View, I extends LoginContract.Interactor> extends BasePresenter<V, I> implements LoginContract.Presenter<V, I> {

    @Inject
    public LoginPresenter(I mvpInteractor, BackendCallHelper apiHelper, CompositeDisposable compositeDisposable) {
        super(mvpInteractor, apiHelper, compositeDisposable);
    }

    /**
     * This function starts the Login process by accepting a {@link LoginRequest} and then executing it.
     *
     * @param loginRequest - The {@link LoginRequest} passed to make the API call via {@link BackendCallHelperImpl#performLoginApiCall(LoginRequest)}
     * @see BackendCallHelperImpl#performLoginApiCall(LoginRequest)
     */
    @Override
    public void startAuthenticationTask(LoginRequest loginRequest) {
        getCompositeDisposable().add(getApiHelper()
                .performLoginApiCall(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginResponse -> {
                    if (LoginPresenter.this.getMvpView() != null) {
                        if (loginResponse.token != null) {
                            Timber.d("Response is %s ", loginResponse.toString());
                            LoginPresenter.this.getMvpView().onLoginSuccess(loginResponse);
                        } else
                            LoginPresenter.this.getMvpView().onLoginFailed();
                    }
                }, throwable -> {
                    if (throwable instanceof ANError)
                        Timber.e("ERROR BODY %s ERROR CODE %s, ERROR DETAIL %s", ((ANError) (throwable)).getErrorBody(), ((ANError) (throwable)).getErrorCode(), ((ANError) (throwable)).getErrorDetail());
                    LoginPresenter.this.getMvpView().onLoginFailed();
                    Timber.e(throwable);
                }));
    }

    /**
     * This function finishes the {@link LoginActivity} and starts the HomeActivity. The HomeActivity is outside this
     * module and can be any activity which has {@link com.samagra.commons.Constants#INTENT_LAUNCH_HOME_ACTIVITY} defined
     * as action in its intent-filter tag in AndroidManifest. This activity is started as a new task.
     * A {@link com.samagra.commons.ExchangeObject.SignalExchangeObject} is used to notify the launch of such activity.
     */
    @Override
    public void finishAndMoveToHomeScreen() {
        Intent intent = new Intent(Constants.INTENT_LAUNCH_HOME_ACTIVITY);
        ExchangeObject.SignalExchangeObject signalExchangeObject = new ExchangeObject.SignalExchangeObject(Modules.MAIN_APP, Modules.ANCILLARY_SCREENS, intent, true);
        AncillaryScreensDriver.mainApplication.getEventBus().send(signalExchangeObject);
        getMvpView().finishActivity();
    }


}
