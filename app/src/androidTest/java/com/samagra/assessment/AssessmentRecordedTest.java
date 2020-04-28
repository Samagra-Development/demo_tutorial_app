package com.samagra.assessment;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import com.samagra.ESamwadAssessmentDriver;
import com.samagra.ancillaryscreens.screens.splash.SplashActivity;
import com.samagra.parent.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.samagra.parent.EspressoTools.waitFor;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AssessmentRecordedTest {


    @Rule
    public ActivityTestRule<SplashActivity> activityTestRule =
            new ActivityTestRule<SplashActivity>(SplashActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    clearSharedPrefs(InstrumentationRegistry.getTargetContext());
                    super.beforeActivityLaunched();
                }
            };

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    private static final String KEY_SP_PACKAGE = "shared_prefs_odk_ancillary_screen";

    /**
     * Clears everything in the SharedPreferences
     */
    private void clearSharedPrefs(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(KEY_SP_PACKAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }

    @Test
    public void assessmentRecordedTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.login_username),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));

        appCompatEditText.perform(replaceText("110"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.login_password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("mandi12345"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.login_password), withText("mandi12345"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.login_submit), withText("Submit"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());
        onView(isRoot()).perform(waitFor(1000));

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.assessment),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        2),
                                2),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.next_button), withText("Next"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout5),
                                        childAtPosition(
                                                withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                                1)),
                                5),
                        isDisplayed()));
        appCompatButton2.perform(click());
        onView(isRoot()).perform(waitFor(1000));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.button2), withText("1A"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatButton3.perform(click());
        onView(isRoot()).perform(waitFor(1000));

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.button2), withText("Hindi"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        appCompatButton4.perform(click());
        onView(isRoot()).perform(waitFor(1000));

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.next_button), withText("Next"),
                        childAtPosition(
                                allOf(withId(R.id.parent),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        appCompatButton5.perform(click());
        onView(isRoot()).perform(waitFor(1000));

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.next_button), withText("Next"),
                        childAtPosition(
                                allOf(withId(R.id.parent),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatButton6.perform(click());

        onView(isRoot()).perform(waitFor(1000));


        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.send_sms_button), withText("Send SMS"),
                        childAtPosition(
                                allOf(withId(R.id.parent),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                6),
                        isDisplayed()));
        appCompatButton7.perform(click());

        onView(isRoot()).perform(waitFor(1000));

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("वापस जाएं"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("वापस जाएं"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        onView(isRoot()).perform(waitFor(1000));

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.button2), withText("1D"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatButton8.perform(click());

        onView(isRoot()).perform(waitFor(1000));

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.button2), withText("Hindi"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatButton9.perform(click());

        onView(isRoot()).perform(waitFor(1000));

        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.radioButton6), withText("B"),
                        childAtPosition(
                                allOf(withId(R.id.grade_rg),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatRadioButton.perform(click());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(R.id.next_button), withText("Next"),
                        childAtPosition(
                                allOf(withId(R.id.parent),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatButton10.perform(click());

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(R.id.next_button), withText("Next"),
                        childAtPosition(
                                allOf(withId(R.id.parent),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatButton11.perform(click());

        ViewInteraction appCompatButton12 = onView(
                allOf(withId(R.id.send_sms_button), withText("Send SMS"),
                        childAtPosition(
                                allOf(withId(R.id.parent),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                6),
                        isDisplayed()));
        appCompatButton12.perform(click());

        ViewInteraction appCompatButton13 = onView(
                allOf(withId(R.id.next_button), withText("Next"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout10),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatButton13.perform(click());

        ViewInteraction appCompatButton14 = onView(
                allOf(withId(R.id.send_sms_button), withText("Send SMS to Parents"),
                        childAtPosition(
                                allOf(withId(R.id.parent),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatButton14.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("वापस जाएं"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withContentDescription("वापस जाएं"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton4.perform(click());
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
}
