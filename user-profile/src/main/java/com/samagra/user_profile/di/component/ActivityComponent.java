package com.samagra.user_profile.di.component;

import com.samagra.user_profile.di.PerActivity;
import com.samagra.user_profile.di.modules.CommonsActivityAbstractProviders;
import com.samagra.user_profile.di.modules.CommonsActivityModule;
import com.samagra.user_profile.profile.ProfileActivity;

import dagger.Component;

/**
 * A {@link Component} annotated interface defines connection between provider of objects ({@link dagger.Module})
 * and the objects which express a dependency. It is implemented internally by Dagger at build time.
 * The modules mentioned in {@link Component} are the classes that are required to inject the activities mentioned
 * in this interface methods.
 *
 * @author Pranav Sharma
 */
@PerActivity
@Component(modules = {CommonsActivityModule.class, CommonsActivityAbstractProviders.class})
public interface ActivityComponent {

    void inject(ProfileActivity profileActivity);
}
