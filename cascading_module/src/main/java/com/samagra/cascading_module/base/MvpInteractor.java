package com.samagra.cascading_module.base;

import com.samagra.cascading_module.prefs.CommonsPreferenceHelper;

/**
 * This is the base interface that all 'Interactor Contracts' must extend.
 * Methods may be added as and when required.
 *
 * @author Pranav Sharma
 */
public interface MvpInteractor {
    CommonsPreferenceHelper getPreferenceHelper();
}
