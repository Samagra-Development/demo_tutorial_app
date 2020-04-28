package com.samagra.parent.StudentAddressBook;

import androidx.test.rule.ActivityTestRule;

import com.samagra.parent.R;
import com.samagra.parent.ui.HomeScreen.HomeActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.samagra.parent.EspressoTools.ButtonDisplayandClick;
import static com.samagra.parent.EspressoTools.EditInputDisplayed;
import static com.samagra.parent.EspressoTools.ListAdapterDisplayandClick;
import static com.samagra.parent.EspressoTools.waitFor;
import static com.samagra.parent.EspressoTools.waitForButtonDisplay;
import static com.samagra.parent.EspressoTools.withIndex;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class EditStudentBookTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);


    @Test
    public void EditStudentBookTest()  {

        checkRenderEditInputClick(R.id.student_address_book);
        onView(isRoot()).perform(waitFor(1000));

        //Edit Student Data

        onView(withIndex(withId(R.id.edit), 2)).perform(click());
        onView(isRoot()).perform(waitFor(1000));

        //Update Student Name
        checkRenderEditInput(R.id.name,"Hello");

        checkRenderButtonDisplayandClick(R.id.add_student,"UPDATE","android.widget.LinearLayout",11,0);
        onView(isRoot()).perform(waitFor(1000));


        pressBack();

       /* //Popup Save data buyyon click
        ViewInteraction appCompatButton_Save = onView(
                allOf(withId(R.id.save_students), withText(R.string.save),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        onView(isRoot()).perform(waitFor(1000));
        appCompatButton_Save.perform(click());

        onView(isRoot()).perform(waitFor(1000));



        //Popup Add Now Button Click
        ViewInteraction appCompatButton_addnow = onView(
                allOf(withId(R.id.add_students), withText(R.string.update),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        onView(isRoot()).perform(waitFor(1000));
        appCompatButton_addnow.perform(click());
        onView(isRoot()).perform(waitFor(1000));*/

        //Moving to Delete StudentBook

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

    private void checkRenderEditInput(int id,String text) {
        EditInputDisplayed(id).perform(replaceText(text),closeSoftKeyboard());
    }
    private void checkRenderEditInputImeClose(int id,String text) {
        EditInputDisplayed(id).perform(replaceText(text),pressImeActionButton());
    }

    private void checkRenderwaitForButtonDisplay(int id, int childid, String withString, String classname, int postion, int childposition) {
        waitForButtonDisplay(id, childid, withString, classname, postion,childposition).perform(scrollTo(), click());
    }

}
