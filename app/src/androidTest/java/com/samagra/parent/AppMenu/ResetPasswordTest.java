package com.samagra.parent.AppMenu;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;

import com.samagra.parent.R;
import com.samagra.parent.ui.HomeScreen.HomeActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.samagra.parent.EspressoTools.ButtonDisplaywithID;
import static com.samagra.parent.EspressoTools.DisplaywithContentDescription;
import static com.samagra.parent.EspressoTools.EditInputDisplayed;
import static com.samagra.parent.EspressoTools.FabDisplayandClick;
import static com.samagra.parent.EspressoTools.waitFor;
import static com.samagra.parent.EspressoTools.waitForNextButtonDisplay;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


public class ResetPasswordTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);


    @Test
    public void ResetPasswordTest() {


        checkRenderDisplaywithContentDescription("वापस जाएं",R.id.toolbar,R.id.parent,0,1);

        checkRenderButtonDisplaywithID(android.R.id.title,"प्रोफ़ाइल",android.R.id.content,0,0);
        onView(isRoot()).perform(waitFor(1000));

        //Reset Password (Not matched)
        checkRenderResetButton(com.samagra.ancillaryscreens.R.id.fab_edit_password,com.samagra.ancillaryscreens.R.id.parent,"पासवर्ड रीसेट",android.R.id.content,0,5);
        onView(isRoot()).perform(waitFor(1000));

        //Snackbar (offline)
        /*onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.internet_not_connected))));
        onView(isRoot()).perform(waitFor(1000)); */

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(com.samagra.ancillaryscreens.R.id.otp),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        onView(isRoot()).perform(waitFor(1000));
        textInputEditText5.perform(replaceText("1000"), closeSoftKeyboard());



        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.new_password,"test123456");


        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.confirm_password,"test123456");

        checkRenderEditInputImeClose(com.samagra.ancillaryscreens.R.id.confirm_password,"test123456");

        checkRenderButtonDisplaywithID(com.samagra.ancillaryscreens.R.id.password_submit,"सबमिट",com.samagra.ancillaryscreens.R.id.parent_ll,2,4);
        onView(isRoot()).perform(waitFor(1000));

        //Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(("This OTP is incorrect."))));
        onView(isRoot()).perform(waitFor(1000));


        checkRenderEditInputImeClose(com.samagra.ancillaryscreens.R.id.otp,"9248");


        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.new_password,"test123456");


        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.confirm_password,"test123456");


        checkRenderEditInputImeClose(com.samagra.ancillaryscreens.R.id.confirm_password,"test123456");

        checkRenderButtonDisplaywithID(com.samagra.ancillaryscreens.R.id.password_submit,"सबमिट",com.samagra.ancillaryscreens.R.id.parent_ll,2,4);

        onView(isRoot()).perform(waitFor(1000));

        //Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(("Passwords didn't match."))));
        onView(isRoot()).perform(waitFor(1000));


        //correct password update // commit bcz otp chane all time
       /* ViewInteraction textInputEditText55 = onView(
                allOf(withId(R.id.otp),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
onView(isRoot()).perform(waitFor(1000));
        textInputEditText55.perform(replaceText("9276"), closeSoftKeyboard());


        ViewInteraction textInputEditText56 = onView(
                allOf(withId(R.id.new_password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        onView(isRoot()).perform(waitFor(1000));
        textInputEditText56.perform(replaceText("mandi12345"), closeSoftKeyboard());

        ViewInteraction textInputEditText57 = onView(
                allOf(withId(R.id.confirm_password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        onView(isRoot()).perform(waitFor(1000));
        textInputEditText57.perform(replaceText("mandi12345"), closeSoftKeyboard());

        ViewInteraction textInputEditText58 = onView(
                allOf(withId(R.id.confirm_password), withText("mandi12345"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        onView(isRoot()).perform(waitFor(1000));
        textInputEditText58.perform(pressImeActionButton());



        ViewInteraction appCompatButton59= onView(allOf(withId(R.id.password_submit), withText("Submit"), childAtPosition(
                childAtPosition(
                        withId(R.id.parent_ll),
                        2),
                4)));

        appCompatButton59.perform(click());
        appCompatButton59.check(matches(isDisplayed()));
        Thread.sleep(2000);

        //Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(("The password has been successfully changed. Redirecting you to profile page"))));
        Thread.sleep(1000);*/




        pressBack();

        //Update Profile (RemoveMobileMobile No.)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(1000));

        //Edit RemoveMobile (10 digit)

        checkRenderEditInputImeClose(com.samagra.ancillaryscreens.R.id.edit_user_phone,"");


        //Update Profile (ExistMobileMobile No.)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(1000));


        checkRenderResetButton(com.samagra.ancillaryscreens.R.id.fab_edit_password,com.samagra.ancillaryscreens.R.id.parent,"पासवर्ड रीसेट",android.R.id.content,0,5);
        onView(isRoot()).perform(waitFor(1000));

        //Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(("Please enter Phone number and save data to change the password."))));
        onView(isRoot()).perform(waitFor(1000));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }


    private void checkRenderEditInput(int id,String text) {
        EditInputDisplayed(id).perform(replaceText(text),closeSoftKeyboard());
    }
    private void checkRenderEditInputImeClose(int id,String text) {
        EditInputDisplayed(id).perform(replaceText(text),pressImeActionButton());
    }

    private void checkRenderDisplaywithContentDescription(String withString, int classid,int childclassid, int postion, int childposition) {
        DisplaywithContentDescription(withString,classid,childclassid,postion,childposition).perform(click());
    }

    private void checkRenderButtonDisplaywithID(int id, String withString, int classid, int postion, int childposition) {
        ButtonDisplaywithID(id,withString,classid,postion,childposition).perform(click());
    }

    private void checkRenderFabDisplayandClick(int id, int childid,int classid, int postion, int childposition) {
        FabDisplayandClick(id,childid,classid,postion,childposition).perform(click());
    }

    private void checkRenderResetButton(int id, int childid, String withString, int classid, int postion, int childposition) {
        waitForNextButtonDisplay(id,childid,withString,classid,postion,childposition).perform(click());
    }

}
