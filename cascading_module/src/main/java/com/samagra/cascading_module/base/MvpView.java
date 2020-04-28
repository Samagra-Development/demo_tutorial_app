package com.samagra.cascading_module.base;

import android.content.Context;

/**
 * This is the Base interface that all 'View Contracts' must extend. For instance,
 * Contract. Methods maybe added to it as and when required.
 *
 * @author Pranav Sharma
 */
public interface MvpView {

    Context getActivityContext();

    void showSnackbar(String message, int duration);
}
