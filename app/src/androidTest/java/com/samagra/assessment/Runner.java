package com.samagra.assessment;

import com.samagra.parent.StopOnFailureSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//@RunWith(Suite.class)
@RunWith(StopOnFailureSuite.class)
@Suite.SuiteClasses({
        AssessmentRecordedTest.class,

})
public class Runner {}