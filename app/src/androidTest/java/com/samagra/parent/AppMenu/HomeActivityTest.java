package com.samagra.parent.AppMenu;


import androidx.test.espresso.ViewInteraction;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.samagra.ancillaryscreens.R;
import com.samagra.parent.ui.HomeScreen.HomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.samagra.parent.EspressoTools.ButtonDisplaywithID;
import static com.samagra.parent.EspressoTools.DisplaywithContentDescription;
import static com.samagra.parent.EspressoTools.waitFor;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4ClassRunner.class)

public class HomeActivityTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);


    @Test
    public void homeActivityTest() {

        onView(isRoot()).perform(waitFor(10000));
        onView(isRoot()).perform(waitFor(10000));

        ViewInteraction linearLayout = onView(allOf(withId(com.samagra.parent.R.id.student_address_book))).check(matches(isDisplayed()));
        ViewInteraction linearLayout2 = onView(allOf(withId(com.samagra.parent.R.id.send_sms))).check(matches(isDisplayed()));
        ViewInteraction linearLayout3 = onView(allOf(withId(com.samagra.parent.R.id.track_sms))).check(matches(isDisplayed()));
        ViewInteraction linearLayout4 = onView(allOf(withId(com.samagra.parent.R.id.assessment))).check(matches(isDisplayed()));
        ViewInteraction appCompatButton2 = onView(allOf(withId(R.id.helpline_button))).check(matches(isDisplayed()));


        onView(isRoot()).perform(waitFor(1000));

        checkRenderDisplaywithContentDescription("वापस जाएं",R.id.toolbar,R.id.parent,0,1);

       // withClassName(is("com.android.internal.view.menu.ListMenuItemView")),
        checkRenderButtonDisplaywithID(android.R.id.title,"हमारे बारें में",android.R.id.content,0,0);

        onView(isRoot()).perform(waitFor(2000));

        pressBack();

        checkRenderDisplaywithContentDescription("वापस जाएं",R.id.toolbar,R.id.parent,0,1);

        onView(isRoot()).perform(waitFor(1000));

        checkRenderButtonDisplaywithID(android.R.id.title,"ट्युटोरिअल",android.R.id.content,0,0);

        onView(isRoot()).perform(waitFor(2000));
        /*//Snackbar (offline)
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.internet_not_connected))));
        onView(isRoot()).perform(waitFor(1000));*/

        pressBack();

        //Move to Profile

    }

    private void checkRenderDisplaywithContentDescription(String withString, int classid,int childclassid, int postion, int childposition) {
        DisplaywithContentDescription(withString,classid,childclassid,postion,childposition).perform(click());
    }

    private void checkRenderButtonDisplaywithID(int id, String withString, int classid, int postion, int childposition) {
        ButtonDisplaywithID(id,withString,classid,postion,childposition).perform(click());
    }


}
