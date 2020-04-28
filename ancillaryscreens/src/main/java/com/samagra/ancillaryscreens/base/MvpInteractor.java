package com.samagra.ancillaryscreens.base;

import com.samagra.ancillaryscreens.data.prefs.CommonsPreferenceHelper;

/**
 * This is the base interface that all 'Interactor Contracts' must extend.
 * Methods may be added as and when required.
 *
 * @author Pranav Sharma
 * @see com.samagra.ancillaryscreens.screens.login.LoginContract.Interactor for example
 */
public interface MvpInteractor {
    CommonsPreferenceHelper getPreferenceHelper();
}
