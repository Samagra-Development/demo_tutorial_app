package com.samagra.parent.helper;

import android.app.Activity;
import android.widget.Spinner;

import com.samagra.parent.UtilityFunctions;

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
        if (this.isUDISEKeyboardShowing) UtilityFunctions.hideKeyboard(this.activity);
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
}
