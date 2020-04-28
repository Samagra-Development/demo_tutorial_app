package com.samagra.parent.ui.HomeScreen;

import com.samagra.parent.base.BaseInteractor;
import com.samagra.parent.data.prefs.PreferenceHelper;

import javax.inject.Inject;

/**
 * This class interacts with the {@link HomeMvpPresenter} and the stored app data. The class abstracts
 * the source of the originating data - This means {@link HomeMvpPresenter} has no idea if the data provided
 * by the {@link HomeInteractor} is from network, database or SharedPreferences
 * This class <b>must</b> implement {@link HomeMvpInteractor} and <b>must</b> extend {@link BaseInteractor}.
 *
 * @author Pranav Sharma
 */
public class HomeInteractor extends BaseInteractor implements HomeMvpInteractor {

    /**
     * This injected value of {@link PreferenceHelper} is provided through
     * {@link com.samagra.parent.di.modules.ApplicationModule}
     */
    @Inject
    public HomeInteractor(PreferenceHelper preferenceHelper) {
        super(preferenceHelper);
    }

    @Override
    public String getUserName() {
        return getPreferenceHelper().getCurrentUserName();
    }
}
