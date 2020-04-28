package com.samagra.ancillaryscreens.di.modules;

import com.samagra.ancillaryscreens.di.PerActivity;
import com.samagra.ancillaryscreens.screens.about.AboutContract;
import com.samagra.ancillaryscreens.screens.about.AboutInteractor;
import com.samagra.ancillaryscreens.screens.about.AboutPresenter;
import com.samagra.ancillaryscreens.screens.login.LoginContract;
import com.samagra.ancillaryscreens.screens.login.LoginInteractor;
import com.samagra.ancillaryscreens.screens.login.LoginPresenter;
import com.samagra.ancillaryscreens.screens.splash.SplashContract;
import com.samagra.ancillaryscreens.screens.splash.SplashInteractor;
import com.samagra.ancillaryscreens.screens.splash.SplashPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * This module is similar to {@link CommonsActivityModule}, it just uses @{@link Binds} instead of @{@link dagger.Provides} for better performance.
 * Using Binds generates a lesser number of files during build times.
 * This class provides the Presenter and Interactor required by the activities.
 *
 * @author Pranav Sharma
 * @see {https://proandroiddev.com/dagger-2-annotations-binds-contributesandroidinjector-a09e6a57758f}
 */
@Module
public abstract class CommonsActivityAbstractProviders {

    @Binds
    @PerActivity
    abstract LoginContract.Presenter<LoginContract.View, LoginContract.Interactor> provideLoginMvpPresenter(
            LoginPresenter<LoginContract.View, LoginContract.Interactor> presenter);

    @Binds
    @PerActivity
    abstract LoginContract.Interactor provideLoginMvpInteractor(LoginInteractor interactor);

    @Binds
    @PerActivity
    abstract SplashContract.Presenter<SplashContract.View, SplashContract.Interactor> provideSplashMvpPresenter(
            SplashPresenter<SplashContract.View, SplashContract.Interactor> presenter);

    @Binds
    @PerActivity
    abstract SplashContract.Interactor provideSplashMvpInteractor(SplashInteractor splashInteractor);

    @Binds
    @PerActivity
    abstract AboutContract.Presenter<AboutContract.View, AboutContract.Interactor> provideAboutMvpPresenter(
            AboutPresenter<AboutContract.View, AboutContract.Interactor> presenter);

    @Binds
    @PerActivity
    abstract AboutContract.Interactor provideAboutMvpInteractor(AboutInteractor aboutInteractor);

}
