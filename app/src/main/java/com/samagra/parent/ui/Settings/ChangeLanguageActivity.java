package com.samagra.parent.ui.Settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.Toolbar;

import com.samagra.commons.CustomEvents;
import com.samagra.commons.ExchangeObject;
import com.samagra.commons.MainApplication;
import com.samagra.commons.Modules;
import com.samagra.parent.LocaleManager;
import com.samagra.parent.R;
import com.samagra.parent.base.BaseActivity;
import com.samagra.parent.base.NonMvpBaseActivity;
import com.samagra.parent.base.ODKTestActivity;
import com.samagra.parent.ui.HomeScreen.HomeActivity;

public class ChangeLanguageActivity extends BaseActivity implements ODKTestActivity {

    private String title;
    RadioGroup rg;
    private String finalLanguage;
    private String originalLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        if (getIntent() != null && getIntent().hasExtra("title"))
            title = getIntent().getStringExtra("title");
        setupToolbar();
        rg = findViewById(R.id.change_lang_rg);
        Button update = findViewById(R.id.update);
        Button cancel = findViewById(R.id.cancel);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currentLanguage = sharedPreferences.getString("currentLanguage", "hi");
        originalLanguage = currentLanguage;
        if (currentLanguage.equals(LocaleManager.HINDI)) {
            rg.check(rg.getChildAt(2).getId());
        } else {
            rg.check(rg.getChildAt(1).getId());
        }


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    String checkedLang = checkedRadioButton.getText().toString();
                    if (checkedLang.equals(getApplicationContext().getResources().getString(R.string.english))) {
                        finalLanguage = LocaleManager.ENGLISH;
                    } else {
                        finalLanguage = LocaleManager.HINDI;
                    }
                }
            }
        });

        update.setOnClickListener(v -> {
            if(originalLanguage.equals(finalLanguage)){
                finish();
            }else {
                setNewLocale(this, finalLanguage);
                ExchangeObject.EventExchangeObject eventExchangeObject = new ExchangeObject.EventExchangeObject(Modules.MAIN_APP, Modules.MAIN_APP, CustomEvents.LANG_CHANGE);
                ((MainApplication)getApplicationContext()).getEventBus().send(eventExchangeObject);
                sharedPreferences.edit().putString("currentLanguage", finalLanguage).apply();
                sharedPreferences.edit().putBoolean("isLanguageChanged", true).apply();
                finish();
            }
        });

        cancel.setOnClickListener(v -> finish());

    }

    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);
   }

    private void setActions() {
        Intent mStartActivity = new Intent(this, HomeActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        assert mgr != null;
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        customizeToolbar();
    }

    private void customizeToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    /**
     * Only set the title and action bar here; do not make further modifications.
     * If you wish to prevent modifications
     * from being overwritten, do them after onCreate is complete.
     * This method should be called in onCreate of your activity.
     */
    @Override
    public void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(v -> finish());
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (originalLanguage.equals(finalLanguage)) {
//        } else {
//            this.finishAffinity();
//        }
    }

}
