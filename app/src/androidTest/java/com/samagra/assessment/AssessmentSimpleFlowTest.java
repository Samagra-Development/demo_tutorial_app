package com.samagra.assessment;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.samagra.ancillaryscreens.R;
import com.samagra.ancillaryscreens.data.prefs.CommonsPrefsHelperImpl;
import com.samagra.ancillaryscreens.screens.splash.SplashActivity;

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
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4ClassRunner.class)
public class AssessmentSimpleFlowTest {

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
    public void assessmentModuleLaunchingTest() throws InterruptedException {

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.login_username),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        Thread.sleep(1000);
        appCompatEditText.perform(replaceText("110"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.login_password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        Thread.sleep(1000);
        appCompatEditText2.perform(replaceText("mandi12345"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.login_password), withText("mandi12345"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        Thread.sleep(1000);
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.login_submit), withText("Submit"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                3),
                        isDisplayed()));
        appCompatButton2.perform(click());
        Thread.sleep(2000);

        ViewInteraction linearLayout = onView(
                allOf(withId(com.samagra.parent.R.id.assessment),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        2),
                                2),
                        isDisplayed()));
        Thread.sleep(2000);
        linearLayout.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(com.samagra.esamwad_assessment.R.id.next_button), withText("Next"),
                        childAtPosition(
                                allOf(withId(com.samagra.esamwad_assessment.R.id.linearLayout5),
                                        childAtPosition(
                                                withClassName(is("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                                1)),
                                5),
                        isDisplayed()));
        Thread.sleep(2000);
        appCompatButton3.perform(click());
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
