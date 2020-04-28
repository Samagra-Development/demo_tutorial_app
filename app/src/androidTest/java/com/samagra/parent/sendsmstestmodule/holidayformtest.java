package com.samagra.parent.sendsmstestmodule;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.samagra.parent.R;
import com.samagra.parent.ui.HomeScreen.HomeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.samagra.parent.EspressoTools.ButtonDisplayandClick;
import static com.samagra.parent.EspressoTools.EditInputDisplayed;
import static com.samagra.parent.EspressoTools.ListAdapterDisplayandClick;
import static com.samagra.parent.EspressoTools.waitFor;
import static com.samagra.parent.EspressoTools.waitForNextButtonDisplay;
import static com.samagra.parent.EspressoTools.waitForTextInputDisplayed;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4ClassRunner.class)

public class holidayformtest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void holidayformtest() throws InterruptedException {

        onView(isRoot()).perform(waitFor(5000));

        //click send sms 1st time
        checkRenderEditInputClick(R.id.send_sms);
        onView(isRoot()).perform(waitFor(1000));

        /*//Snackbar
        onView(withId(com.samagra.ancillaryscreens.R.id.snackbar_text))
                .check(matches(withText(("Setting up credentials on the forms"))));
        onView(isRoot()).perform(waitFor(2000));

        onView(isRoot()).perform(waitFor(30000));

        //click send sms 2nd time time
        ViewInteraction linearLayout_sendsms = onView(allOf(withId(com.samagra.odktest.R.id.send_sms))).check(matches(isDisplayed()));
        linearLayout.perform(click());
        onView(isRoot()).perform(waitFor(1000));

        //Snackbar
        onView(withId(com.samagra.ancillaryscreens.R.id.snackbar_text))
                .check(matches(withText(("Setting up credentials on the forms"))));
        onView(isRoot()).perform(waitFor(2000));

        onView(isRoot()).perform(waitFor(30000));

        //click send sms 2nd time time
        ViewInteraction linearLayout_sendsms1 = onView(allOf(withId(com.samagra.odktest.R.id.send_sms))).check(matches(isDisplayed()));
        linearLayout.perform(click());
        onView(isRoot()).perform(waitFor(1000));*/

        checkRenderListAdapterDisplayandClick(android.R.id.list,R.id.llParent,0,0);
        onView(isRoot()).perform(waitFor(2000));

        /*ViewInteraction button3 = onView(
                allOf(withId(com.samagra.ancillaryscreens.R.id.simple_button), withText(R.string.select_date),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("org.odk.collect.android.widgets.DateWidget")),
                                        2),
                                0),
                        isDisplayed()));
        button3.perform(click());*/

        checkRenderwaitForTextInputDisplayed(R.id.simple_button,0);
        onView(isRoot()).perform(waitFor(2000));


        checkRenderButtonDisplayandClick(android.R.id.button1,"ठीक है","android.widget.ScrollView",0,3);

        onView(isRoot()).perform(waitFor(1000));

        checkRenderwaitForTextInputDisplayed(R.id.simple_button,1);
        onView(isRoot()).perform(waitFor(2000));

        checkRenderButtonDisplayandClick(android.R.id.button1,"ठीक है","android.widget.ScrollView",0,3);

        onView(isRoot()).perform(waitFor(1000));

        checkRenderwaitForTextInputDisplayed(R.id.simple_button,2);
        onView(isRoot()).perform(waitFor(2000));

        checkRenderButtonDisplayandClick(android.R.id.button1,"ठीक है","android.widget.ScrollView",0,3);
        onView(isRoot()).perform(waitFor(2000));

        checkRenderwaitForNextButtonDisplay(R.id.form_forward_button,R.id.buttonholder,"आगामी",R.id.navigation_view,0,2);
        onView(isRoot()).perform(waitFor(2000));

        checkRenderwaitForNextButtonDisplay(R.id.form_forward_button,R.id.buttonholder,"आगामी",R.id.navigation_view,0,2);
        onView(isRoot()).perform(waitFor(2000));

        checkRenderButtonDisplayandClick(R.id.save_exit_button,"अभिभावकों को  एसएमएस भेजें","android.widget.ScrollView",0,6);
        onView(isRoot()).perform(waitFor(2000));

        /*//Snackbar
        onView(withId(com.samagra.ancillaryscreens.R.id.snackbar_text))
                .check(matches(withText(("Data has been captured. SMS will be sent within 48 hours."))));
        onView(isRoot()).perform(waitFor(2000));*/
    }

    private void checkRenderEditInputClick(int id) {
        EditInputDisplayed(id).perform(click());
    }

    private void checkRenderListAdapterDisplayandClick(int id, int classid, int postion, int childposition) {
        ListAdapterDisplayandClick(id,classid,postion,childposition).perform(click());
    }

    private void checkRenderButtonDisplayandClick(int id, String withString, String classname, int postion, int childposition) {
        ButtonDisplayandClick(id,withString, classname,postion,childposition).perform(click());
    }

    private void checkRenderwaitForNextButtonDisplay(int id, int childid, String withString, int classid, int postion, int childposition) {
        waitForNextButtonDisplay(id, childid, withString, classid, postion, childposition).perform(click());
    }

    private void checkRenderwaitForTextInputDisplayed(int id, int index) {
        waitForTextInputDisplayed(id, index).perform(click());
    }

}
