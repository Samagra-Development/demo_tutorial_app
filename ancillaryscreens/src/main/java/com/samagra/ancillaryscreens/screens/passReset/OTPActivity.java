package com.samagra.ancillaryscreens.screens.passReset;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.samagra.ancillaryscreens.R;

public class OTPActivity extends AppCompatActivity implements OTPCallBackListener {

    public OTPCallBackListener otpCallBackListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        // Open fragment to get phone number and send OTP
        // => New fragment to enter OTP and new password.
        // => Snackbar to get the results and redirect to login page.

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            OTPFragment firstFragment = new OTPFragment();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    @Override
    public void Update() {
        this.finish();
    }
}
