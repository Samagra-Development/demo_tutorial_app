package com.samagra.parent.sendsmstestmodule;


import com.samagra.parent.AppMenu.LoginActivityTest;
import com.samagra.parent.StopOnFailureSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//@RunWith(Suite.class)
@RunWith(StopOnFailureSuite.class)
@Suite.SuiteClasses({
        LoginActivityTest.class,
        attendanceformtest.class,
        homeworkformtest.class,
        assessmentAnnouncementtest.class,
        smcformtest.class,
        holidayformtest.class,

})
public class sendsmstestsuite {
}
