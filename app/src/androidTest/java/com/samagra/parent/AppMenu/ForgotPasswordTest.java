package com.samagra.parent.AppMenu;


import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.samagra.ancillaryscreens.R;
import com.samagra.ancillaryscreens.screens.login.LoginActivity;
import com.samagra.ancillaryscreens.screens.passReset.ChangePasswordActivity;
import com.samagra.ancillaryscreens.screens.splash.SplashActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.samagra.parent.EspressoTools.ButtonDisplayandClick;
import static com.samagra.parent.EspressoTools.ButtonDisplaywithID;
import static com.samagra.parent.EspressoTools.EditInputDisplayed;
import static com.samagra.parent.EspressoTools.TextViewDisplayandClick;
import static com.samagra.parent.EspressoTools.waitFor;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
@RunWith(AndroidJUnit4ClassRunner.class)

public class ForgotPasswordTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);


    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Test
    public void forgotPasswordTest() {


        //Username view
        checkRenderEditInput(R.id.login_username,"110");

        //Password View
        checkRenderEditInput(R.id.login_password,"mandi1234");

        //perform ime click
        checkRenderEditInputImeClose(R.id.login_password,"mandi1234");

        //Perform submit action
        checkRenderButtonandClick(R.id.login_submit,"सबमिट","android.widget.LinearLayout",2,3);


        /*//Snackbar (offline)
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.internet_not_connected))));
        onView(isRoot()).perform(waitFor(1000));*/

        //Forgot Password text click
        checkRenderTextViewDisplayandClick(R.id.forgot_password,"पासवर्ड भूल गए?");

                /*//Snackbar (offline)
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.internet_not_connected))));
        onView(isRoot()).perform(waitFor(1000));*/

        checkRenderEditInput(R.id.user_phone,"8955159323");
        onView(isRoot()).perform(waitFor(1000));

        checkRenderButtonDisplaywithID(R.id.phone_submit,"ओटीपी भेजें",R.id.parent_ll,2,1);
        onView(isRoot()).perform(waitFor(2000));

        checkRenderEditInput(R.id.otp,"0926");

        checkRenderEditInput(R.id.new_password,"test12345");

        checkRenderEditInput(R.id.confirm_password,"test12345");

        checkRenderEditInputImeClose(R.id.confirm_password,"test12345");

        checkRenderTextViewDisplayandClick(R.id.password_submit,"सबमिट");

        //Move to Login
    }

    private void checkRenderEditInput(int id,String text) {
        EditInputDisplayed(id).perform(replaceText(text),closeSoftKeyboard());
    }
    private void checkRenderEditInputImeClose(int id,String text) {
        EditInputDisplayed(id).perform(replaceText(text),pressImeActionButton());
    }

    private void checkRenderButtonandClick(int id, String withString, String classname, int postion, int childposition) {
        ButtonDisplayandClick( id,withString,classname,postion,childposition).perform(click());
    }

    private void checkRenderTextViewDisplayandClick(int id, String withString) {
        TextViewDisplayandClick(id,withString).perform(click());
    }

    private void checkRenderButtonDisplaywithID(int id, String withString, int classid, int postion, int childposition) {
        ButtonDisplaywithID(id,withString,classid,postion,childposition).perform(click());
    }




}
