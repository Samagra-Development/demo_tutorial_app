package com.samagra.parent.StudentAddressBook;

import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;

import com.samagra.ESamwadAssessmentDriver;
import com.samagra.StudentAddressBookDriver;
import com.samagra.parent.R;
import com.samagra.parent.ui.HomeScreen.HomeActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.samagra.parent.EspressoTools.ButtonDisplayandClick;
import static com.samagra.parent.EspressoTools.EditInputDisplayed;
import static com.samagra.parent.EspressoTools.ListAdapterDisplayandClick;
import static com.samagra.parent.EspressoTools.childAtPosition;
import static com.samagra.parent.EspressoTools.waitFor;
import static com.samagra.parent.EspressoTools.waitForButtonDisplay;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;


public class AddStudentBookTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);


    @Test
    public void StudentAddressBookTest(){

        onView(isRoot()).perform(waitFor(10000));

        checkRenderEditInputClick(R.id.student_address_book);
        onView(isRoot()).perform(waitFor(1000));

        // if internet not connected (Spinner view is Display or not)
       /* ViewInteraction spinner_grade_spinner = onView(allOf(withId(com.samagra.odktest.R.id.grade_spinner),withText("Class 1"))).check(matches(isDisplayed()));
        ViewInteraction spinner_section_spinner = onView(allOf(withId(com.samagra.odktest.R.id.student_address_book),withText("Section A"))).check(matches(isDisplayed()));

*/
        StudentAddressBookDriver.mainApplication.updateInternetStatus(false);

        //Add Student Action Button
        /*checkRenderListAdapterDisplayandClick(R.id.fab_add_student,android.R.id.content,0,3);
        onView(isRoot()).perform(waitFor(2000));*/

        ViewInteraction floatingActionButton_AddStudent = onView(
                allOf(withId(R.id.fab_add_student),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        onView(isRoot()).perform(waitFor(1000));
        floatingActionButton_AddStudent.perform(click());


        // if internet not connected (Spinner view is Display or not)

      /*  ViewInteraction spinner_grade_spinner = onView(allOf(withId(com.samagra.odktest.R.id.grade_spinner),withText("Class 1"))).check(matches(isDisplayed()));
        ViewInteraction spinner_section_spinner = onView(allOf(withId(com.samagra.odktest.R.id.student_address_book),withText("Section A"))).check(matches(isDisplayed()));
        ViewInteraction appCompatEditText_studentName_offline = onView(allOf(withId(com.samagra.odktest.R.id.name))).check(matches(isDisplayed()));
        ViewInteraction appCompatEditText_student_FathersName_offline = onView(allOf(withId(com.samagra.odktest.R.id.father_name))).check(matches(isDisplayed()));
        ViewInteraction appCompatEditText_student_contact_number_offline = onView(allOf(withId(com.samagra.odktest.R.id.contact_number))).check(matches(isDisplayed()));
        ViewInteraction appCompatEditText_student_roll_number_offline = onView(allOf(withId(com.samagra.odktest.R.id.admission_number))).check(matches(isDisplayed()));
        ViewInteraction appCompatButton_Addstudent_offline = onView(allOf(withId(com.samagra.odktest.R.id.admission_number))).check(matches(isDisplayed()));
        ViewInteraction appCompatButton_Addstudent = onView(allOf(withId(com.samagra.odktest.R.id.add_student), withText("Add Student"))).check(matches(isDisplayed()));
*/

        //both online and offine case are same (snackbar msg change)
        //Add Student Name
        checkRenderEditInput(R.id.name,"Raj");
        onView(isRoot()).perform(waitFor(1000));

        //Add Student Fathers Name
        checkRenderEditInput(R.id.father_name,"Rajesh");
        onView(isRoot()).perform(waitFor(1000));

        //Add Student contact_number(wrong)
        checkRenderEditInput(R.id.contact_number,"89551593230");
        onView(isRoot()).perform(waitFor(1000));


        /*//Add Student roll_number(wrong 10001)
        ViewInteraction appCompatEditText_student_roll_number_invalid = onView(allOf(withId(com.samagra.odktest.R.id.admission_number),
                childAtPosition(
                        childAtPosition(
                                withClassName(is("android.widget.ScrollView")),
                                0),
                        7))).check(matches(isDisplayed()));
        appCompatEditText_student_roll_number_invalid.perform(scrollTo(), replaceText("10001"), closeSoftKeyboard());
        onView(isRoot()).perform(waitFor(1000));
        appCompatEditText_student_roll_number_invalid.perform(pressImeActionButton());
        onView(isRoot()).perform(waitFor(1000));*/


        //Add Student roll_number
        checkRenderEditInputImeClose(R.id.admission_number,"123356");
        onView(isRoot()).perform(waitFor(1000));

        //Add Student Gender_Male
        checkRenderwaitForButtonDisplay(R.id.gender_male,R.id.gender,"Male","android.widget.LinearLayout",8,1);

        //Add Student Gender_Female
        checkRenderwaitForButtonDisplay(R.id.gender_female,R.id.gender,"Female","android.widget.LinearLayout",8,2);
        onView(isRoot()).perform(waitFor(1000));

        //Add Student Category_general
       /* checkRenderwaitForButtonDisplay(R.id.category_general,R.id.category,"General","android.widget.LinearLayout",9,1);
        onView(isRoot()).perform(waitFor(1000));
        //Add Student Category_Obc
        checkRenderwaitForButtonDisplay(R.id.category_OBC,R.id.category,"OBC","android.widget.LinearLayout",9,2);
        onView(isRoot()).perform(waitFor(1000));
        //Add Student Category_SC
        checkRenderwaitForButtonDisplay(R.id.category_SC,R.id.category,"SC","android.widget.LinearLayout",9,3);
        onView(isRoot()).perform(waitFor(1000));
        //Add Student Category_ST
        checkRenderwaitForButtonDisplay(R.id.category_ST,R.id.category,"ST","android.widget.LinearLayout",9,4);
        onView(isRoot()).perform(waitFor(1000));*/

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.category_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        9),
                                1)));
        isDisplayed();
        appCompatSpinner.perform(scrollTo(), click());

        onView(withText(containsString("OBC"))).inRoot(isPlatformPopup()).check(matches(isDisplayed())).perform(click());
        onView(isRoot()).perform(waitFor(1000));

     /* DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        isDisplayed();
        appCompatTextView.perform(click());*/
        swipeUp();
        onView(isRoot()).perform(waitFor(1000));

        ViewInteraction appCompatRadioButton2 = onView(
                allOf(withId(R.id.cwsn_yes), withText("Yes"),
                        childAtPosition(
                                allOf(withId(R.id.is_cwsn),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                11)),
                                1)));
        appCompatRadioButton2.perform(scrollTo(), click());

        ViewInteraction appCompatRadioButton3 = onView(
                allOf(withId(R.id.cwsn_no), withText("No"),
                        childAtPosition(
                                allOf(withId(R.id.is_cwsn),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                11)),
                                2)));
        appCompatRadioButton3.perform(scrollTo(), click());

        //Add Student Cwsn_yes
        //checkRenderwaitForButtonDisplay(R.id.cwsn_yes,R.id.is_cwsn,"Yes","android.widget.LinearLayout",0,1);

        //Add Student Cwsn_NO
        //checkRenderwaitForButtonDisplay(R.id.cwsn_no,R.id.is_cwsn,"No","android.widget.LinearLayout",0,2);
        onView(isRoot()).perform(waitFor(1000));

        //Add Student Data
        checkRenderButtonDisplayandClick(R.id.add_student,"छात्र जोड़ें","android.widget.LinearLayout",12,0);
        onView(isRoot()).perform(waitFor(1000));

       /* //Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(("The mobile number is invalid. Please enter a valid mobile number."))));
        Thread.sleep(1000);*/

        //Add Student contact_number
        checkRenderEditInputImeClose(R.id.contact_number,"8955159323");
        onView(isRoot()).perform(waitFor(1000));

        //Add Student Data
        checkRenderButtonDisplayandClick(R.id.add_student,"छात्र जोड़ें","android.widget.LinearLayout",12,0);
        onView(isRoot()).perform(waitFor(1000));


        //offline snackbar (save click)
       /* onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.no_internet_data_saved_locally))));
        Thread.sleep(1000);*/

        //Fab Save data
        checkRenderListAdapterDisplayandClick(R.id.fab_save,android.R.id.content,0,1);
        onView(isRoot()).perform(waitFor(1000));

        //Popup add now button click
        checkRenderButtonDisplayandClick(R.id.add_student,"अपडेट","android.widget.LinearLayout",2,0);
        onView(isRoot()).perform(waitFor(1000));

        //offline snackbar (save click)
        /*onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText((R.string.no_internet_data_saved_locally))));
       onView(isRoot()).perform(waitFor(1000));*/

        pressBack();

        ViewInteraction linearLayout_studentbook = onView(allOf(withId(R.id.student_address_book))).check(matches(isDisplayed()));
        linearLayout_studentbook.perform(click());
        onView(isRoot()).perform(waitFor(1000));

        //intetnet on
        StudentAddressBookDriver.mainApplication.updateInternetStatus(true);

        //Fab Save data
        checkRenderListAdapterDisplayandClick(R.id.fab_save,android.R.id.content,0,1);
        onView(isRoot()).perform(waitFor(1000));

        //Popup add now button click
        checkRenderButtonDisplayandClick(R.id.add_student,"अपडेट","android.widget.LinearLayout",2,0);
        onView(isRoot()).perform(waitFor(2000));

        pressBack();

        //Moving to Edit StudentBook
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


