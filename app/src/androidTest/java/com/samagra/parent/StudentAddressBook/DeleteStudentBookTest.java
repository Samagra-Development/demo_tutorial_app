package com.samagra.parent.StudentAddressBook;

import androidx.test.rule.ActivityTestRule;

import com.samagra.parent.R;
import com.samagra.parent.ui.HomeScreen.HomeActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.samagra.parent.EspressoTools.ButtonDisplayandClick;
import static com.samagra.parent.EspressoTools.EditInputDisplayed;
import static com.samagra.parent.EspressoTools.ListAdapterDisplayandClick;
import static com.samagra.parent.EspressoTools.waitFor;
import static com.samagra.parent.EspressoTools.withIndex;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class DeleteStudentBookTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);


    @Test
    public void DeleteStudentBookTest() {

        checkRenderEditInputClick(R.id.student_address_book);
        onView(isRoot()).perform(waitFor(1000));

        //item selection
        onView(withIndex(withId(R.id.is_selected), 2)).perform(click());

        //fab delete item click
        checkRenderListAdapterDisplayandClick(R.id.fab_delete,android.R.id.content,0,2);
        onView(isRoot()).perform(waitFor(1000));

        //Popup delete data button click
        checkRenderButtonDisplayandClick(R.id.delete_students,"हटाएं","android.widget.LinearLayout",2,0);
        onView(isRoot()).perform(waitFor(1000));

        pressBack();

        onView(isRoot()).perform(waitFor(1000));
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
}
