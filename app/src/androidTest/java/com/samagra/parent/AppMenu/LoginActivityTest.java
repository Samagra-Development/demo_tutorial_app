package com.samagra.parent.AppMenu;


import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.samagra.StudentAddressBookDriver;
import com.samagra.ancillaryscreens.R;
import com.samagra.ancillaryscreens.screens.login.LoginActivity;
import com.samagra.ancillaryscreens.screens.login.LoginContract;
import com.samagra.ancillaryscreens.screens.login.LoginPresenter;
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
import static com.androidnetworking.AndroidNetworking.initialize;
import static com.samagra.parent.EspressoTools.ButtonDisplayandClick;
import static com.samagra.parent.EspressoTools.EditInputDisplayed;
import static com.samagra.parent.EspressoTools.waitFor;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4ClassRunner.class)

public class LoginActivityTest{

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Test
    public void loginActivityTest()  {

        //Username view
        checkRenderEditInput(R.id.login_username,"2111604801");

        //Password View
        checkRenderEditInput(R.id.login_password,"himachal1234");

        //perform ime click
        checkRenderEditInputImeClose(R.id.login_password,"himachal1234");

        //Submit button click
        checkRenderButtonandClick(R.id.login_submit,"सबमिट","android.widget.LinearLayout",2,3);



        //Snackbar (offline)
    /*    onView(withId(R.id.snackbar_text))
                .check(matches(withText((R.string.internet_not_connected))));
        onView(isRoot()).perform(waitFor(2000));*/

     //Snackbar
 /*       onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.incorrect_credentials))));
        onView(isRoot()).perform(waitFor(1000));*/


        //Username view
        checkRenderEditInput(R.id.login_username,"2111604801");

        //Password View
        checkRenderEditInput(R.id.login_password,"himachal12345");

        //perform ime click
        checkRenderEditInputImeClose(R.id.login_password,"himachal12345");

        //Submit button click
        checkRenderButtonandClick(R.id.login_submit,"सबमिट","android.widget.LinearLayout",2,3);

        onView(isRoot()).perform(waitFor(5000));

        /*//Snackbar (offline)
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.internet_not_connected))));
        onView(isRoot()).perform(waitFor(1000));*/

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //onView(isRoot()).perform(waitFor(10000));
        //Move to Home

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


}
