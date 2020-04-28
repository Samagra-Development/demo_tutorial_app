package com.samagra.ancillaryscreens.screens.about;

import com.samagra.customworkmanager.Data;

import com.samagra.ancillaryscreens.base.BasePresenter;
import com.samagra.ancillaryscreens.data.network.BackendCallHelper;
import com.samagra.commons.TaskScheduler.ScheduledOneTimeWork;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * The presenter for the Splash Screen. This class controls the interactions between the View and the data.
 * Must implement @{@link com.samagra.ancillaryscreens.screens.about.AboutContract.Presenter}
 *
 * @author Pranav Sharma
 */
public class AboutPresenter<V extends AboutContract.View, I extends AboutContract.Interactor> extends BasePresenter<V, I> implements AboutContract.Presenter<V, I> {

    @Inject
    public AboutPresenter(I mvpInteractor, BackendCallHelper apiHelper, CompositeDisposable compositeDisposable) {
        super(mvpInteractor, apiHelper, compositeDisposable);
    }


    @Override
    public void test(int x, int y, float z) {
        Data data = new Data.Builder()
                .putInt("X", x)
                .putInt("Y", y)
                .putFloat("Z", z)
                .build();
        ScheduledOneTimeWork.from(SampleWorker.class, data).enqueueTask(getMvpView().getActivityContext());
    }
}
