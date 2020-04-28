package com.samagra.ancillaryscreens.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.samagra.ancillaryscreens.di.component.ActivityComponent;
import com.samagra.ancillaryscreens.di.component.DaggerActivityComponent;
import com.samagra.ancillaryscreens.di.modules.CommonsActivityModule;
import com.samagra.commons.LocaleManager;

import java.util.Locale;

import static android.content.pm.PackageManager.GET_META_DATA;

/**
 * This abstract class serves as the Base for all other activities used in this module. The class is
 * designed to support MVP Pattern with Dagger support. Any methods that need to be executed in all
 * activities, must be mentioned here. App level configuration changes (like theme change, language change, etc)
 * can be easily made through a BaseActivity. This must implement {@link MvpView}.
 *
 * @author Pranav Sharma
 */
public abstract class BaseActivity extends AppCompatActivity implements MvpView {

    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetTitles();
    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .commonsActivityModule(new CommonsActivityModule(this))
                    .build();
        }
        return activityComponent;
    }


    @Override
    public String fetchString(int stringID) {
       return getActivityContext().getResources().getString(stringID);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    protected void resetTitles() {
        try {
            ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
            if (info.labelRes != 0) {
                setTitle(info.labelRes);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void showSnackbar(String message, int duration) {
        Snackbar.make(findViewById(android.R.id.content), message, duration).show();
    }
}
