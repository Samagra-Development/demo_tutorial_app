package com.samagra.parent.StudentAddressBook;


import com.samagra.parent.AppMenu.LoginActivityTest;
import com.samagra.parent.StopOnFailureSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(StopOnFailureSuite.class)
@Suite.SuiteClasses({
        LoginActivityTest.class,
        AddStudentBookTest.class,
        EditStudentBookTest.class,
        DeleteStudentBookTest.class,
})

public class StudentJUnnitTestSuite {
}
