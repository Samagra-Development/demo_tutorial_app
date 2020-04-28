package com.samagra.cascading_module.ui;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;


import java.lang.reflect.Method;

// TODO : Add documentation
public class KeyboardHandler {
    public boolean isDropDownOpen;
    public boolean isUDISEKeyboardShowing;
    public Spinner spinner;
    Activity activity;

    public KeyboardHandler(boolean isDropDownOpen, boolean isUDISEKeyboardShowing, Spinner spinner, Activity activity) {
        this.isDropDownOpen = isDropDownOpen;
        this.isUDISEKeyboardShowing = isUDISEKeyboardShowing;
        this.spinner = spinner;
        this.activity = activity;
    }

    public void closeDropDown() {
        // If DROPDOWN and UDISE clicked, close DROPDOWN
        if (this.isDropDownOpen) hideSpinnerDropDown();
        this.isDropDownOpen = false;
    }

    public void closeUDISEKeyboard() {
        // If UDISE and DROPDOWN clicked, close UDISE
        if (this.isUDISEKeyboardShowing) hideKeyboard(this.activity);
        this.isUDISEKeyboardShowing = false;
    }

    public void hideSpinnerDropDown() {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(this.spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
