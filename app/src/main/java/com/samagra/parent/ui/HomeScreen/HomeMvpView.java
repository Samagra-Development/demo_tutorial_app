package com.samagra.parent.ui.HomeScreen;

import com.samagra.parent.base.MvpView;

/**
 * The view interface 'contract' for the Home Screen. This defines all the functionality required by the
 * Presenter for the view as well as for enforcing certain structure in the Views.
 * The {@link HomeActivity} <b>must</b> implement this interface. This way, the business logic behind the screen
 * can remain unaffected.
 *
 * @author Pranav Sharma
 */
public interface HomeMvpView extends MvpView {
    void customizeToolbar();

    void showOnClickMessage(String message);

    void showLoading(String message);

    void renderLayoutVisible();
    void hideLoading();
}
