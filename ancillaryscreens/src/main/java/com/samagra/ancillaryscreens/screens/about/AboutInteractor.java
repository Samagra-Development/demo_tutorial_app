package com.samagra.ancillaryscreens.screens.about;

import com.samagra.ancillaryscreens.base.BaseInteractor;
import com.samagra.ancillaryscreens.data.prefs.CommonsPreferenceHelper;

import javax.inject.Inject;

/**
 * This class interacts with the {@link AboutContract.Presenter} and the stored app data.
 * This class should abstract the source of the originating data - This means {@link com.samagra.ancillaryscreens.screens.about.AboutContract.Presenter}
 * has no idea if the data provided by the {@link com.samagra.ancillaryscreens.screens.about.AboutContract.Interactor} is
 * from network, database or SharedPreferences.
 *
 * @author Pranav Sharma
 */
public class AboutInteractor extends BaseInteractor implements AboutContract.Interactor {

    @Inject
    public AboutInteractor(CommonsPreferenceHelper preferenceHelper) {
        super(preferenceHelper);
    }
}
