package com.samagra.user_profile.di.modules;

import com.samagra.user_profile.di.PerActivity;
import com.samagra.user_profile.profile.ProfileContract;
import com.samagra.user_profile.profile.ProfileInteractor;
import com.samagra.user_profile.profile.ProfilePresenter;

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
    abstract ProfileContract.Presenter<ProfileContract.View, ProfileContract.Interactor> provideProfileMvpPresenter(
            ProfilePresenter<ProfileContract.View, ProfileContract.Interactor> presenter);

    @Binds
    @PerActivity
    abstract ProfileContract.Interactor provideProfileMvpInteractor(ProfileInteractor profileInteractor);

}
