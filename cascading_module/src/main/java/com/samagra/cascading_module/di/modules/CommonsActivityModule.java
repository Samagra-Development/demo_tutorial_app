package com.samagra.cascading_module.di.modules;

import android.app.Activity;
import android.content.Context;

import com.samagra.cascading_module.di.ActivityContext;
import com.samagra.cascading_module.di.ApplicationContext;
import com.samagra.cascading_module.di.PreferenceInfo;
import com.samagra.cascading_module.prefs.CommonsPreferenceHelper;
import com.samagra.cascading_module.prefs.CommonsPrefsHelperImpl;
import com.samagra.commons.Constants;

import dagger.Module;
import dagger.Provides;

/**
 * Classes marked with @{@link Module} are responsible for providing objects that can be injected.
 * Such classes define methods annotated with @{@link Provides}. The returned objects from such methods are
 * available for DI.
 *
 * @author Pranav Sharma
 */
@Module
public class CommonsActivityModule {

    private Activity activity;

    public CommonsActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ApplicationContext
    Context provideApplicationContext() {
        return activity.getApplication().getApplicationContext();
    }

    @Provides
    CommonsPreferenceHelper provideCommonsPreferenceHelper(CommonsPrefsHelperImpl commonsPrefsHelper) {
        return commonsPrefsHelper;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceFileName() {
        return Constants.COMMON_SHARED_PREFS_NAME;
    }

}
