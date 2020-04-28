package com.samagra.ancillaryscreens.PresenterTests;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.samagra.ancillaryscreens.data.network.BackendCallHelper;
import com.samagra.ancillaryscreens.data.network.model.LoginRequest;
import com.samagra.ancillaryscreens.data.network.model.LoginResponse;
import com.samagra.ancillaryscreens.screens.login.LoginContract;
import com.samagra.ancillaryscreens.screens.login.LoginPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Mock
    LoginContract.View loginMvpView;
    @Mock
    LoginContract.Interactor loginMvpInteractor;
    @Mock
    BackendCallHelper apiHelper;
    @Mock
    LoginResponse loginResponse;

    private TestScheduler testScheduler;

    private static final String MOCK_USERNAME = "username";
    private static final String MOCK_PASSWORD = "password";
    private static final String MOCK_USER_ID = "7";
    private static final String MOCK_API_KEY = "somerandommockkey";

    private LoginPresenter<LoginContract.View, LoginContract.Interactor> loginPresenter;

    // TODO: Refactor this to an activity rule and use it everywhere instead of adding it to every test.
    @BeforeClass
    public static void setUpRxSchedulers() {
        Scheduler immediate = new Scheduler() {
            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run, false);
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
    }


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        loginPresenter = new LoginPresenter<>(loginMvpInteractor, apiHelper, new CompositeDisposable());
        loginPresenter.onAttach(loginMvpView);
        testScheduler = new TestScheduler();
    }

    @Test
    public void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest(MOCK_USERNAME, MOCK_PASSWORD);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.token = new JsonPrimitive("aksjdasl");
        JsonObject object = new JsonObject();
        object.addProperty("a", "ada");
        loginResponse.user = object;
        when(apiHelper.performLoginApiCall(loginRequest)).thenReturn(Single.just(loginResponse));
        loginPresenter.startAuthenticationTask(loginRequest);
        testScheduler.triggerActions();
        verify(loginMvpView).onLoginSuccess(loginResponse);
    }

    @Test
    public void testLogin_Failure() {
        LoginRequest loginRequest = new LoginRequest(MOCK_USERNAME, MOCK_PASSWORD);
        when(apiHelper.performLoginApiCall(loginRequest)).thenReturn(Single.just(loginResponse));
        loginPresenter.startAuthenticationTask(loginRequest);
        testScheduler.triggerActions();
        verify(loginMvpView).onLoginFailed();
    }

    @Test
    public void testLogin_Exception() {
        LoginRequest loginRequest = new LoginRequest(MOCK_USERNAME, MOCK_PASSWORD);
        when(apiHelper.performLoginApiCall(loginRequest)).thenReturn(Single.just(loginResponse).map(loginResponse -> {
            throw new Exception();
        }));
        loginPresenter.startAuthenticationTask(loginRequest);
        testScheduler.triggerActions();
        verify(loginMvpView).onLoginFailed();
    }

    @After
    public void teardown() {
        loginPresenter.onDetach();
    }
}
