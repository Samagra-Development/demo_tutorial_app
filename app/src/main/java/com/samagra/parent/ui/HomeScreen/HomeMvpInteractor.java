package com.samagra.parent.ui.HomeScreen;

import com.samagra.parent.base.MvpInteractor;

/**
 * The interactor 'contract' for the HomeScreen. This interface exposes methods to the presenter ({@link HomePresenter})
 * that provide read/write access to the app data. The source of the data is abstracted to the app.
 * This interface must be a type of {@link MvpInteractor}
 *
 * @author Pranav Sharma
 */
public interface HomeMvpInteractor extends MvpInteractor {
    String getUserName();
}
