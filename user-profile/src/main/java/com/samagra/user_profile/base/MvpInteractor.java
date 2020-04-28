package com.samagra.user_profile.base;

import com.samagra.user_profile.data.prefs.CommonsPreferenceHelper;

/**
 * This is the base interface that all 'Interactor Contracts' must extend.
 * Methods may be added as and when required.
 *
 * @author Pranav Sharma
 * @see com.samagra.user_profile.base.MvpInteractor example
 */
public interface MvpInteractor {
    CommonsPreferenceHelper getPreferenceHelper();
}
