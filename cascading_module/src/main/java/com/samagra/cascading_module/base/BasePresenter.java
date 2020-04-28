package com.samagra.cascading_module.base;

import javax.inject.Inject;

/**
 * A class that serves as a base for all the presenters (handles business logic) for the activities (serves as view).
 * The class uses Java Generics. The V and I stands for View and Interactor respectively. Since View and Interactors
 * are different for each activity, Java Generics are used. The class must implement {@link MvpPresenter}.
 *
 * @author Pranav Sharma
 */
public class BasePresenter<V extends MvpView, I extends MvpInteractor> implements MvpPresenter<V, I> {

    private V mvpView;
    private I mvpInteractor;


    @Inject
    public BasePresenter(I mvpInteractor) {
        this.mvpInteractor = mvpInteractor;
    }

    @Override
    public V getMvpView() {
        return mvpView;
    }

    @Override
    public I getMvpInteractor() {
        return mvpInteractor;
    }


    @Override
    public void onAttach(V mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void onDetach() {
        this.mvpView = null;
    }

    @Override
    public boolean isViewAttached() {
        return this.mvpView != null;
    }
}
