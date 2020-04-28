package com.samagra.ancillaryscreens.screens.passReset;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.samagra.ancillaryscreens.R;
import com.samagra.ancillaryscreens.screens.login.LoginActivity;
import com.samagra.ancillaryscreens.utils.SnackbarUtils;

public class OTPFragment extends Fragment implements View.OnClickListener, ChangePasswordActionListener {

    private OTPCallBackListener mCallback;

    private EditText password;
    private EditText confirmPassword;
    private EditText otp;
    private String phoneNumber;
    private TextView timer;
    private ConstraintLayout parent;
    private String lastPage = "lastPage";
    private Button submitButton;
    private View.OnClickListener resendListener;
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OTPCallBackListener) activity;
        } catch (ClassCastException e) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otp_view, container, false);
        password = view.findViewById(R.id.new_password);
        confirmPassword = view.findViewById(R.id.confirm_password);
        progressBar = view.findViewById(R.id.progress_bar);
        otp = view.findViewById(R.id.otp);
        parent = view.findViewById(R.id.parent);
        timer = view.findViewById(R.id.countdown_timer);
        ImageView iv = view.findViewById(R.id.otp_govt_logo);
        iv.setImageResource(R.drawable.samagra_name_logo);
        startTimer();
        String title = "Reset Password";
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(view1 -> {
            if (lastPage.equals("profile")) {
                mCallback.Update();
            } else {
                getFragmentManager().popBackStackImmediate();
            }
        });

        Bundle arguments = getArguments();

        if (arguments != null) {
            phoneNumber = arguments.getString("phoneNumber");
            //lastPage = arguments.getString("last");
            if (arguments.getString("last") != null) {
                lastPage = arguments.getString("last");
            }
        }

        submitButton = (Button) view.findViewById(R.id.password_submit);
        password.addTextChangedListener(getWatcher(otp, password, confirmPassword, submitButton));
        confirmPassword.addTextChangedListener(getWatcher(otp, password, confirmPassword, submitButton));

        resendListener = v -> {
            if (isNetworkDisonnected()) {
                SnackbarUtils.showLongSnackbar(parent, OTPFragment.this.getActivity().getResources().getString(R.string.internet_not_connected));
            } else {
                showProgressBar();
                new SendOTPTask(new ChangePasswordActionListener() {
                    @Override
                    public void onSuccess() {
                        hideProgressBar();
                        startTimer();
                        SnackbarUtils.showLongSnackbar(parent, "OTP send. Please retry changing the password");
                        submitButton.setText("Submit");
                        submitButton.setOnClickListener(OTPFragment.this::onClick);
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        hideProgressBar();
                        SnackbarUtils.showLongSnackbar(parent, OTPFragment.this.getActivity().getResources().getString(R.string.error_sending_otp));
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, phoneNumber);
            }
        };

        submitButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (isNetworkDisonnected()) {
            SnackbarUtils.showLongSnackbar(parent, this.getResources().getString(R.string.internet_not_connected));
        } else {
            if (v.getId() == R.id.password_submit) {
                String pass = password.getText().toString();
                String confPass = confirmPassword.getText().toString();
                if (pass.equals("")) {
                    SnackbarUtils.showLongSnackbar(parent, getResources().getString(R.string.pass_cannot_be_empty));
                } else if (pass.length() < 8 || confPass.length() < 8) {
                    SnackbarUtils.showLongSnackbar(parent, getString(R.string.less_than_8_pass));
                } else if (pass.equals(confPass)) {
                    new UpdatePasswordTask(this).executeOnExecutor(
                            AsyncTask.THREAD_POOL_EXECUTOR, phoneNumber,
                            otp.getText().toString(),
                            pass);
                } else {
                    SnackbarUtils.showLongSnackbar(parent, this.getResources().getString(R.string.pass_did_not_match));
                }
            }
        }
    }

    @Override
    public void onSuccess() {
        SnackbarUtils.showLongSnackbar(parent, this.getResources().getString(R.string.pass_change_successful));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            // Logout the user.
            SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putBoolean("isLoggedIn", false);
            editor1.apply();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }, 5000);
    }

    @Override
    public void onFailure(Exception exception) {
        if (parent != null) {
            SnackbarUtils.showLongSnackbar(parent, exception.getMessage());
        }
    }

    private TextWatcher getWatcher(EditText otp, EditText password, EditText confirmPassword, Button login) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String _otp = otp.getText().toString().trim();
                String _password = password.getText().toString().trim();
                String _confirmPassword = confirmPassword.getText().toString().trim();
                if (validateInputs(_otp, _password, _confirmPassword)) {
                    login.setBackgroundColor(getResources().getColor(R.color.button_colors));
//                    login.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.appBlue), PorterDuff.Mode.MULTIPLY);
                    login.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                } else {
                    login.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    login.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), PorterDuff.Mode.MULTIPLY);
                    login.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private boolean validateInputs(String otp, String password, String confirmPassword) {
        return otp.length() == 4
                && password.length() >= 8
                && confirmPassword.equals(password);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText(getString(R.string.seconds_remaining) + millisUntilFinished / 1000);
            }

            public void onFinish() {
                submitButton.setText((getContext().getResources().getString(R.string.resend_otp)));
                submitButton.setOnClickListener(resendListener);
            }
        };
        countDownTimer.start();
    }

    private void showProgressBar() {
        parent.setClickable(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        parent.setClickable(true);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private boolean isNetworkDisonnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isConnected();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}

