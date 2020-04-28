package com.samagra.cascading_module.di.modules;

import com.samagra.cascading_module.di.PerActivity;
import com.samagra.cascading_module.ui.SearchInteractor;
import com.samagra.cascading_module.ui.SearchMvpInteractor;
import com.samagra.cascading_module.ui.SearchMvpPresenter;
import com.samagra.cascading_module.ui.SearchMvpView;
import com.samagra.cascading_module.ui.SearchPresenter;

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

//    @Binds
//    @PerActivity
//    abstract LoginContract.Presenter<LoginContract.View, LoginContract.Interactor> provideLoginMvpPresenter(
//            LoginPresenter<LoginContract.View, LoginContract.Interactor> presenter);
//
//    @Binds
//    @PerActivity
//    abstract LoginContract.Interactor provideLoginMvpInteractor(LoginInteractor interactor);


    @Binds
    @PerActivity
    abstract SearchMvpPresenter<SearchMvpView, SearchMvpInteractor> provideSearchMvpPresenter(
            SearchPresenter<SearchMvpView, SearchMvpInteractor> presenter);

    @Binds
    @PerActivity
    abstract SearchMvpInteractor provideSearchMvpInteractor(SearchInteractor homeInteractor);
}
