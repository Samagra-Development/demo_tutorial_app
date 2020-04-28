package com.samagra.user_profile.base;

import com.samagra.user_profile.data.network.BackendCallHelper;

import io.reactivex.disposables.CompositeDisposable;

/**
 * This is the base interface that all the 'Presenter Contracts' must extend.
 * Methods maybe added to it as and when required.
 *
 * @author Pranav Sharma
 * @see com.samagra.user_profile.base.MvpPresenter for example
 */
public interface MvpPresenter<V extends MvpView, I extends MvpInteractor> {
    V getMvpView();

    I getMvpInteractor();

    BackendCallHelper getApiHelper();

    CompositeDisposable getCompositeDisposable();

    void onAttach(V mvpView);

    void onDetach();

    boolean isViewAttached();
}
