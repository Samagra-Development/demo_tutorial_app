package com.samagra.parent.ui.HomeScreen;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.samagra.parent.MyApplication;
import com.samagra.parent.base.BasePresenter;

import javax.inject.Inject;

/**
 * The Presenter class for Home Screen. This class controls interaction between the View and Data.
 * This class <b>must</b> implement the {@link HomeMvpPresenter} and <b>must</b> be a type of {@link BasePresenter}.
 *
 * @author Pranav Sharma
 */
public class HomePresenter<V extends HomeMvpView, I extends HomeMvpInteractor> extends BasePresenter<V, I> implements HomeMvpPresenter<V, I> {

    @Inject
    public HomePresenter(I mvpInteractor) {
        super(mvpInteractor);
    }

    @Override
    public void onFillFormsOptionClicked() {
        if (getMvpView() != null) {
            getMvpView().showOnClickMessage("You have clicked to view Fill Forms option.");
        }
    }

    @Override
    public void onViewSubmittedFormsOptionsClicked() {
        if (getMvpView() != null) {
            getMvpView().showOnClickMessage("You have clicked to view submitted Forms option.");
        }
    }



    @Override
    public void onSubmitFormsClicked() {
        if (getMvpView() != null) {
            getMvpView().showOnClickMessage("You have clicked to view Sync offline submitted Forms option.");
        }
    }

    @Override
    public void onViewHelplineClicked() {
        if (getMvpView() != null) {
            getMvpView().showOnClickMessage("You have clicked to view Contact Helpline option.");
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


}