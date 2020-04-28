package com.samagra.parent.AppMenu;

import androidx.test.rule.ActivityTestRule;

import com.samagra.parent.R;
import com.samagra.parent.ui.HomeScreen.HomeActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.samagra.parent.EspressoTools.ButtonDisplaywithID;
import static com.samagra.parent.EspressoTools.DisplaywithContentDescription;
import static com.samagra.parent.EspressoTools.EditInputDisplayed;
import static com.samagra.parent.EspressoTools.FabDisplayandClick;
import static com.samagra.parent.EspressoTools.waitFor;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class ProfileActivityTest {


    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);


    @Test
    public void ProfileActivityTest() {

        checkRenderDisplaywithContentDescription("वापस जाएं",R.id.toolbar,R.id.parent,0,1);

        checkRenderButtonDisplaywithID(android.R.id.title,"प्रोफ़ाइल",android.R.id.content,0,0);

        onView(isRoot()).perform(waitFor(1000));

        //Edit Profile(Usenamer
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);

        //If internet off Edit profile item is view
       /* ViewInteraction appCompatEditText_offline = onView(allOf(withId(R.id.edit_user_account_name))).check(matches(isDisplayed()));
        ViewInteraction appCompatEditText3_offline = onView(allOf(withId(R.id.edit_user_phone))).check(matches(isDisplayed()));
        ViewInteraction appCompatEditText5_offline = onView(allOf(withId(R.id.edit_user_email))).check(matches(isDisplayed()));*/


        //Edit Username
        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.edit_user_account_name,"Chakshu");

        //Update Profile(username)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);

        //Snackbar (offline)
        /*onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(("User details failed to update. Please check your internet!"))));
        onView(isRoot()).perform(waitFor(1000));*/


        //Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.successful_update))));
        onView(isRoot()).perform(waitFor(1000));


        //Edit Profile (Mobile No.)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(1000));

        //Edit Phone No. (update mobile no)
        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.edit_user_phone,"8955159325");


        //Update Profile (Mobile No.)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(2000));

        //Snackbar (offline)
        /*onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(("User details failed to update. Please check your internet!"))));
        onView(isRoot()).perform(waitFor(1000));*/

        //Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.successful_update))));
        onView(isRoot()).perform(waitFor(1000));



        //Update Profile (Exist Mobile No.)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(1000));


        //Edit Phone No. (Exist Mobile No.)
        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.edit_user_phone,"9805126955");
        onView(isRoot()).perform(waitFor(1000));

        //Update Profile (Exist Mobile No.)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(2000));

        //Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.mulitple_users_same_phone_found))));
        onView(isRoot()).perform(waitFor(2000));


        //Update Profile (11 No.)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(1000));


        //Edit PExistMobile (11 digit)
        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.edit_user_phone,"89551593230");
        onView(isRoot()).perform(waitFor(1000));
        //Update Profile (11Mobile No.)

        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(2000));

        //Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.invalid_phone_number))));
        onView(isRoot()).perform(waitFor(1000));


        //Edit PExistMobile (10 digit)
        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.edit_user_phone,"8955159323");

        //Update Profile (10Mobile No.)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(2000));

        //Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.successful_update))));
        onView(isRoot()).perform(waitFor(1000));


        //Edit Profile ( Email Address)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(1000));

        //Edit Email (Wrong Email ID)
        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.edit_user_email,"chakshugautam@gmail.com");

        //Update Profile (Email ID)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(2000));

        //Snackbar (offline)
        /*onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(("User details failed to update. Please check your internet!"))));
        Thread.sleep(1000);*/

        //Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.successful_update))));
        onView(isRoot()).perform(waitFor(1000));

        //Edit Profile (Email Address)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(1000));


        //Edit Email
        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.edit_user_email,"chakshu@gmail.com");

        //Update Profile ( Email ID)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(2000));

        //Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.successful_update))));
        onView(isRoot()).perform(waitFor(2000));

        //Edit Profile (ExistEmail Address)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(2000));


        //Edit Email
        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.edit_user_email,"rishabh@samagragovernance.in");

        //Update Profile ( Email ID)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(1000));

        //Snackbar
        /*onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.mulitple_users_same_email_found))));
        onView(isRoot()).perform(waitFor(1000));*/


        //Update Profile ( Email ID)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(1000));


        //Edit Email
        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.edit_user_email,"chakshu@#gmail.com");

        //Update Profile ( Email ID)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(1000));

        //Commit snackbar bcz snackbar is not define in code snipt

        /*onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.invalid_email_address))));

        onView(isRoot()).perform(waitFor(1000));*/

        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(1000));


        //Edit Email
        checkRenderEditInput(com.samagra.ancillaryscreens.R.id.edit_user_email,"chakshu@gmail.com");

        //Update Profile ( Email ID)
        checkRenderFabDisplayandClick(com.samagra.ancillaryscreens.R.id.fab,com.samagra.ancillaryscreens.R.id.parent,android.R.id.content,0,4);
        onView(isRoot()).perform(waitFor(1000));

        //Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.successful_update))));
        onView(isRoot()).perform(waitFor(1000));

        //Move to ResetPassword
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


}
