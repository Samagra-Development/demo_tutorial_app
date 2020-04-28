package com.samagra.cascading_module.ui;

import android.widget.Spinner;

import com.samagra.cascading_module.base.MvpView;

/**
 * The view interface 'contract' for the Search Screen. This defines all the functionality required by the
 * Presenter for the view as well as for enforcing certain structure in the Views.
 * The {@link SearchActivity} <b>must</b> implement this interface. This way, the business logic behind the screen
 * can remain unaffected.
 *
 * @author Pranav Sharma
 */
public interface SearchMvpView extends MvpView {

    /**
     * This functions simply shifts a {@link Spinner} to its default state; a state that prevents
     * a user from interacting with the spinner. This is essentially disabling a spinner on the UI.
     *
     * @param spinner - The {@link Spinner} widget to disable
     */
    void makeSpinnerDefault(Spinner spinner);
}
