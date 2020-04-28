package com.samagra.user_profile.profile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.samagra.user_profile.InvalidConfigurationException;
import com.samagra.user_profile.R;
import com.samagra.user_profile.R2;
import com.samagra.user_profile.base.BaseActivity;
import com.samagra.user_profile.passReset.ActionListener;
import com.samagra.user_profile.passReset.OTPActivity;
import com.samagra.user_profile.passReset.SendOTPTask;
import com.samagra.user_profile.passReset.SnackbarUtils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.samagra.user_profile.profile.ProfileElementViewHolders.NumberTextViewHolder;
import static com.samagra.user_profile.profile.ProfileElementViewHolders.ProfileElementHolder;
import static com.samagra.user_profile.profile.ProfileElementViewHolders.SpinnerTextViewHolder;

public class ProfileActivity extends BaseActivity implements ProfileContract.View {

    @BindView(R2.id.parent_profile_elements)
    public LinearLayout parentProfileElements;

    private ArrayList<UserProfileElement> userProfileElements;
    private ArrayList<ProfileElementHolder> dynamicHolders;
    private Unbinder unbinder;
    private boolean isInEditMode;
    private Snackbar progressSnackbar = null;

    @Inject
    ProfilePresenter<ProfileContract.View, ProfileContract.Interactor> profilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        unbinder = ButterKnife.bind(this);
        getActivityComponent().inject(this);
        profilePresenter.onAttach(this);
        if (getIntent() != null && getIntent().getParcelableArrayListExtra("config") != null) {
            userProfileElements = getIntent().getParcelableArrayListExtra("config");
            dynamicHolders = new ArrayList<>();
        } else {
            throw new InvalidConfigurationException(ProfileActivity.class);
        }
        initToolbar();
        initUserDetails(userProfileElements);
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialises Calendar that is used to select User's Data of joining.
     */
    @Override
    public void initCalendar() {

    }

    /**
     * This method populates the fields displayed on the screen using multiple {@link UserProfileElement}
     * objects provided to {@link ProfileActivity} during its launch.
     *
     * @param userProfileElements - An {@link ArrayList} of {@link UserProfileElement} objects.
     *                            These list of objects represent individual elements of a User's
     *                            Profile.
     */
    @Override
    public void initUserDetails(ArrayList<UserProfileElement> userProfileElements) {
        Collections.sort(userProfileElements, (userProfileElement, t1) -> userProfileElement.getSection() - t1.getSection());
        int currentSection = -1;
        int prevSection = -1;
        boolean sectionChanged;
        for (UserProfileElement profileElement : userProfileElements) {
            currentSection = profileElement.getSection();
            sectionChanged = prevSection != -1 && prevSection != currentSection;
            if (prevSection != -1 && sectionChanged) {
                parentProfileElements.addView(LayoutInflater.from(this).inflate(R.layout.profile_divider, parentProfileElements, false));
            }
            switch (profileElement.getProfileElementContentType()) {
                case TEXT:
                    View simpleTextView = LayoutInflater.from(this).inflate(R.layout.profile_simple_text_row, parentProfileElements, false);
                    ProfileElementViewHolders.SimpleTextViewHolder simpleTextViewHolder = new ProfileElementViewHolders.SimpleTextViewHolder(simpleTextView, profileElement, profilePresenter.getContentValueFromKey(profileElement.getContent()));
                    dynamicHolders.add(simpleTextViewHolder);
                    parentProfileElements.addView(simpleTextView);
                    break;
                case DATE:
                    View dateTextView = LayoutInflater.from(this).inflate(R.layout.profile_date_text_row, parentProfileElements, false);
                    ProfileElementViewHolders.DateTextViewHolder dateTextViewHolder = new ProfileElementViewHolders.DateTextViewHolder(dateTextView, profileElement, profilePresenter.getContentValueFromKey(profileElement.getContent()));
                    dynamicHolders.add(dateTextViewHolder);
                    parentProfileElements.addView(dateTextView);
                    break;
                case PHONE_NUMBER:
                    View numberTextView = LayoutInflater.from(this).inflate(R.layout.profile_number_text_row, parentProfileElements, false);
                    NumberTextViewHolder numberTextViewHolder = new NumberTextViewHolder(numberTextView, profileElement, profilePresenter.getContentValueFromKey(profileElement.getContent()));
                    dynamicHolders.add(numberTextViewHolder);
                    parentProfileElements.addView(numberTextView);
                    break;
                case SPINNER:
                    View spinnerView = LayoutInflater.from(this).inflate(R.layout.profile_spinner_row, parentProfileElements, false);
                    SpinnerTextViewHolder spinnerTextViewHolder = new SpinnerTextViewHolder(spinnerView, profileElement, profilePresenter.getContentValueFromKey(profileElement.getContent()));
                    dynamicHolders.add(spinnerTextViewHolder);
                    parentProfileElements.addView(spinnerView);
                    break;
            }
            prevSection = currentSection;
        }
    }

    @OnClick(R2.id.fab)
    @Override
    public void onProfileEditButtonClicked(View v) {

        if (profilePresenter.isNetworkConnected()) {
            // save if already in edit mode prior to click.
            if (isInEditMode) {
                if (profilePresenter.validateUpdatedFields(dynamicHolders)) {
                    profilePresenter.updateUserProfileAtRemote(dynamicHolders, "");
                    isInEditMode = !isInEditMode; // update the edit mode flag (accounting for the click)
                }
            } else {
                isInEditMode = true; // update the edit mode flag (accounting for the click)
            }

            if (isInEditMode)
                ((FloatingActionButton) v).setImageResource(R.drawable.ic_save_icon_color_24dp);
            else
                ((FloatingActionButton) v).setImageResource(R.drawable.ic_edit_icon_color_24dp);
            for (ProfileElementHolder elementHolder : dynamicHolders) { elementHolder.toggleHolderEnable(isInEditMode);
            }
        } else {
            showSnackbar(" It seems you are not connected to the internet. Please switch on your mobile data to change password,", 3000);
        }
    }

    @OnClick(R2.id.fab_edit_password)
    @Override
    public void onEditPasswordButtonClicked(View v) {
        showLoading("Sending OTP");
        if(profilePresenter.isNetworkConnected()){
            String phoneNumber= profilePresenter.getContentValueFromKey(dynamicHolders.get(1).getUserProfileElement().getContent());
            new SendOTPTask(new ActionListener() {
                @Override
                public void onSuccess() {
                    hideLoading();
                    Intent otpIntent = new Intent(ProfileActivity.this, OTPActivity.class);
                    otpIntent.putExtra("phoneNumber", phoneNumber);
                    otpIntent.putExtra("last", "profile");
                    startActivity(otpIntent);
                }

                @Override
                public void onFailure(Exception exception) {
                    showSnackbar("Error sending OTP. Please contact admin", 3000);
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, phoneNumber);
        }else{
            hideLoading();
            showSnackbar("It seems you are not connected to internet. Please check.", 3000);
        }


    }

    @Override
    public void showLoading(String message) {
        if (progressSnackbar == null) {
            progressSnackbar = SnackbarUtils.getSnackbarWithProgressIndicator(findViewById(android.R.id
                    .content), this, message);
            progressSnackbar.show();
        } else {
            progressSnackbar.setText(message);
            progressSnackbar.show();
        }
    }

    @Override
    public void hideLoading() {
        if (progressSnackbar != null && progressSnackbar.isShownOrQueued()) {
            progressSnackbar.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profilePresenter.onDestroy();
        unbinder.unbind();
        profilePresenter.onDetach();
    }
}
