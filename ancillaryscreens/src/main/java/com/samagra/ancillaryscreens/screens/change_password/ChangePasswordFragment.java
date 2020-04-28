package com.samagra.ancillaryscreens.screens.change_password;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.samagra.ancillaryscreens.R;
import com.samagra.ancillaryscreens.screens.passReset.ChangePasswordActionListener;
import com.samagra.ancillaryscreens.screens.passReset.OTPFragment;
import com.samagra.ancillaryscreens.screens.passReset.SendOTPTask;
import com.samagra.ancillaryscreens.utils.SnackbarUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener, ChangePasswordActionListener {

    private EditText phoneNumber;
    private View parent;

    String phone;
    String lastPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.change_password_view, container, false);

        String title = getActivity().getResources().getString(R.string.forgot_pass);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(view1 -> {
            getFragmentManager().popBackStack();
            getActivity().finish();
        });
        Button button = view.findViewById(R.id.phone_submit);
        phoneNumber = view.findViewById(R.id.user_phone);
        parent = view.findViewById(R.id.rootView);
        button.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.phone_submit) {
            if (validate(phoneNumber.getText().toString()))
                new SendOTPTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, phoneNumber.getText().toString());
            else {
                SnackbarUtils.showLongSnackbar(parent, getActivity().getResources().getString(R.string.invalid_phone_number));
            }
        }
    }

    private boolean validate(String phoneNumber) {
        Pattern p = Pattern.compile("[6-9][0-9]{9}");
        Matcher m = p.matcher(phoneNumber);
        return (m.find() && m.group().equals(phoneNumber));
    }

    @Override
    public void onSuccess() {
        OTPFragment otpFragment = new OTPFragment();
        Bundle arguments = new Bundle();
        arguments.putString( "phoneNumber" , phoneNumber.getText().toString());
        otpFragment.setArguments(arguments);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, otpFragment, "NewFragmentTag");
        ft.commit();
        ft.addToBackStack(null);
        if(parent != null) {
            SnackbarUtils.showLongSnackbar(parent, "OTP successfully sent to this number " + phoneNumber.getText().toString());
        }
    }

    @Override
    public void onFailure(Exception exception) {
        if(parent != null) SnackbarUtils.showLongSnackbar(parent, exception.getMessage());
    }


}
