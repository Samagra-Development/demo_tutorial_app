package com.samagra.parent.FillAssessment;


import com.samagra.parent.AppMenu.LoginActivityTest;
import com.samagra.parent.StopOnFailureSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(StopOnFailureSuite.class)
@Suite.SuiteClasses({
        LoginActivityTest.class,
        FillAssessmentActivityTest.class,
})
public class FillAssessmentSuite {
}
