package com.samagra.parent.AppMenu;


import com.samagra.parent.StopOnFailureSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//@RunWith(Suite.class)
@RunWith(StopOnFailureSuite.class)
@Suite.SuiteClasses({
        ForgotPasswordTest.class,
        LoginActivityTest.class,
        HomeActivityTest.class,
        ProfileActivityTest.class,
        ResetPasswordTest.class,
})
public class JunitTestSuite {
}