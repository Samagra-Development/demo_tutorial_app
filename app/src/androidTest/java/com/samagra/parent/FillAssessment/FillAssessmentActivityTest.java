package com.samagra.parent.FillAssessment;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.espresso.action.ViewActions;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.samagra.ESamwadAssessmentDriver;
import com.samagra.ancillaryscreens.screens.splash.SplashActivity;
import com.samagra.parent.R;
import com.samagra.parent.ui.HomeScreen.HomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.samagra.parent.EspressoTools.waitFor;
import static com.samagra.parent.EspressoTools.waitForButtonDisplay;
import static com.samagra.parent.EspressoTools.waitForElementUntilDisplayed;
import static com.samagra.parent.EspressoTools.waitForNextButtonDisplay;
import static com.samagra.parent.EspressoTools.waitForTextInputDisplayed;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4ClassRunner.class)

public class FillAssessmentActivityTest {

  /*  @Rule
    public ActivityTestRule<HomeActivity> activityTestRule =
            new ActivityTestRule<HomeActivity>(HomeActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    clearSharedPrefs(InstrumentationRegistry.getTargetContext());
                    super.beforeActivityLaunched();
                }
            };*/

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    private static final String KEY_SP_PACKAGE = "shared_prefs_odk_ancillary_screen";

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

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
    public void FillAssessmentActivityTest() {

        onView(isRoot()).perform(waitFor(10000));


        checkRenderAndClick("android.widget.RelativeLayout", R.id.assessment, 1000);

        //internet off
        ESamwadAssessmentDriver.mainApplication.updateInternetStatus(false);

        checkRenderForButton(R.id.next_button, R.id.linearLayout5, "अगला", "androidx.coordinatorlayout.widget.CoordinatorLayout", 1, 5);

        onView(isRoot()).perform(ViewActions.swipeUp());
        onView(isRoot()).perform(waitFor(2000));

        checkRenderForButton(R.id.button2, R.id.constraintLayout, "1A", "androidx.constraintlayout.widget.ConstraintLayout", 0, 0);

        checkRenderForButton(R.id.button2, R.id.constraintLayout, "Hindi", "androidx.constraintlayout.widget.ConstraintLayout", 0, 0);

        checkRenderForButton(R.id.radioButton7, R.id.grade_rg, "A", "androidx.constraintlayout.widget.ConstraintLayout", 0, 0);

        checkRenderForNextButton(R.id.next_button, R.id.parent, "अगला", android.R.id.content, 0, 4);

        checkRenderForTextInput(R.id.student_passed_et, 0, "1");
        checkRenderForTextInput(R.id.student_passed_et, 1, "1");
        checkRenderForTextInput(R.id.student_passed_et, 2, "1");
        checkRenderForTextInput(R.id.student_passed_et, 3, "1");
        checkRenderForTextInput(R.id.student_passed_et, 4, "1");
        checkRenderForTextInput(R.id.student_passed_et, 5, "1");
        checkRenderForTextInput(R.id.student_passed_et, 6, "1");
        checkRenderForTextInput(R.id.student_passed_et, 7, "1");
        checkRenderForTextInput(R.id.student_passed_et, 8, "1");
        checkRenderForTextInput(R.id.student_passed_et, 9, "1");

        onView(isRoot()).perform(ViewActions.swipeUp());
        onView(isRoot()).perform(waitFor(2000));

        checkRenderForTextInput(R.id.student_passed_et, 6, "1");
        checkRenderForTextInput(R.id.student_passed_et, 7, "1");
        checkRenderForTextInput(R.id.student_passed_et, 8, "1");
        checkRenderForTextInput(R.id.student_passed_et, 9, "1");
        //checkRenderForTextInput(R.id.student_passed_et, 10, "1");

        checkRenderForNextButton(R.id.next_button, R.id.parent, "अगला", android.R.id.content, 0, 3);

        checkRenderForButton(R.id.button2, R.id.constraintLayout, "English", "androidx.constraintlayout.widget.ConstraintLayout", 0, 0);

        checkRenderForButton(R.id.radioButton7, R.id.grade_rg, "A", "androidx.constraintlayout.widget.ConstraintLayout", 0, 0);

        checkRenderForNextButton(R.id.next_button, R.id.parent, "अगला", android.R.id.content, 0, 4);

        checkRenderForTextInput(R.id.student_passed_et, 0, "1");
        checkRenderForTextInput(R.id.student_passed_et, 1, "1");
        checkRenderForTextInput(R.id.student_passed_et, 2, "1");
        checkRenderForTextInput(R.id.student_passed_et, 3, "1");
        checkRenderForTextInput(R.id.student_passed_et, 4, "1");
        checkRenderForTextInput(R.id.student_passed_et, 5, "1");
        checkRenderForTextInput(R.id.student_passed_et, 6, "1");
        checkRenderForTextInput(R.id.student_passed_et, 7, "1");
        checkRenderForTextInput(R.id.student_passed_et, 8, "1");
       // checkRenderForTextInput(R.id.student_passed_et, 9, "1");

        onView(isRoot()).perform(ViewActions.swipeUp());
        onView(isRoot()).perform(waitFor(1000));

        checkRenderForTextInput(R.id.student_passed_et, 2, "1");
        checkRenderForTextInput(R.id.student_passed_et, 3, "1");
        checkRenderForTextInput(R.id.student_passed_et, 4, "1");
        checkRenderForTextInput(R.id.student_passed_et, 5, "1");
        checkRenderForTextInput(R.id.student_passed_et, 6, "1");
        checkRenderForTextInput(R.id.student_passed_et, 7, "1");
        checkRenderForTextInput(R.id.student_passed_et, 8, "1");
        //checkRenderForTextInput(R.id.student_passed_et, 9, "1");
        //checkRenderForTextInput(R.id.student_passed_et, 10, "1");

        checkRenderForNextButton(R.id.next_button, R.id.parent, "अगला", android.R.id.content, 0, 3);

        checkRenderForButton(R.id.button2, R.id.constraintLayout, "Maths", "androidx.constraintlayout.widget.ConstraintLayout", 0, 0);

        checkRenderForButton(R.id.radioButton7, R.id.grade_rg, "A", "androidx.constraintlayout.widget.ConstraintLayout", 0, 0);
        checkRenderForNextButton(R.id.next_button, R.id.parent, String.valueOf(R.string.next), android.R.id.content, 0, 4);

        checkRenderForTextInput(R.id.student_passed_et, 0, "1");
        checkRenderForTextInput(R.id.student_passed_et, 1, "1");
        checkRenderForTextInput(R.id.student_passed_et, 2, "1");
        checkRenderForTextInput(R.id.student_passed_et, 3, "1");
        checkRenderForTextInput(R.id.student_passed_et, 4, "1");
        checkRenderForTextInput(R.id.student_passed_et, 5, "1");
        checkRenderForTextInput(R.id.student_passed_et, 6, "1");
        checkRenderForTextInput(R.id.student_passed_et, 7, "1");
        checkRenderForTextInput(R.id.student_passed_et, 8, "1");
        checkRenderForTextInput(R.id.student_passed_et, 9, "1");

        checkRenderForNextButton(R.id.next_button, R.id.parent, "अगला", android.R.id.content, 0, 3);

        /*checkRenderForButton(R.id.button2, R.id.constraintLayout, "EVS", "androidx.constraintlayout.widget.ConstraintLayout", 0, 0);

        checkRenderForNextButton(R.id.next_button, R.id.parent, "अगला", android.R.id.content, 0, 4);

        checkRenderForTextInput(R.id.student_passed_et, 0, "1");
        checkRenderForTextInput(R.id.student_passed_et, 1, "1");
        checkRenderForTextInput(R.id.student_passed_et, 2, "1");
        checkRenderForTextInput(R.id.student_passed_et, 3, "1");
        checkRenderForTextInput(R.id.student_passed_et, 4, "1");
        checkRenderForTextInput(R.id.student_passed_et, 5, "1");
        checkRenderForTextInput(R.id.student_passed_et, 6, "1");
        checkRenderForTextInput(R.id.student_passed_et, 7, "1");
        checkRenderForTextInput(R.id.student_passed_et, 8, "1");
        checkRenderForTextInput(R.id.student_passed_et, 9, "1");

        onView(isRoot()).perform(ViewActions.swipeUp());
        onView(isRoot()).perform(waitFor(1000));

        checkRenderForTextInput(R.id.student_passed_et, 7, "1");
        checkRenderForTextInput(R.id.student_passed_et, 8, "1");
        checkRenderForTextInput(R.id.student_passed_et, 9, "1");
        checkRenderForTextInput(R.id.student_passed_et, 10, "1");

        checkRenderForNextButton(R.id.next_button, R.id.parent, "अगला", android.R.id.content, 0, 3);*/

        onView(isRoot()).perform(waitFor(1000));


        checkRenderForNextButton(R.id.send_sms_button, R.id.parent, "एसएमएस भेजें ", android.R.id.content, 0, 6);

        onView(isRoot()).perform(waitFor(1000));

        checkRenderForNextButton(R.id.next_button, R.id.linearLayout10, "अगला", android.R.id.content, 0, 4);

        checkRenderForNextButton(R.id.send_sms_button, R.id.parent, "अभिभावकों को एसएमएस भेजें", android.R.id.content, 0, 3);


        onView(isRoot()).perform(waitFor(3000));

    }

    private void checkRenderAndClick(String className, int id, int waitForTime) {
        waitForElementUntilDisplayed(id, className).perform(click());
    }

    private void checkRenderForTextInput(int id, int index, String inputtext) {
        waitForTextInputDisplayed(id, index).perform(replaceText(inputtext));
    }

    private void checkRenderForButton(int id, int childid, String withString, String classname, int postion, int childposition) {
        waitForButtonDisplay(id, childid, withString, classname, postion, childposition).perform(click());
    }

    private void checkRenderForNextButton(int id, int childid, String withString, int classid, int postion, int childposition) {
        waitForNextButtonDisplay(id, childid, withString, classid, postion, childposition).perform(click());
    }

}
