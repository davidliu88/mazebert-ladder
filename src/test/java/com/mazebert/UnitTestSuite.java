package com.mazebert;

import com.googlecode.junittoolbox.ExcludeCategories;
import com.googlecode.junittoolbox.ParallelSuite;
import com.googlecode.junittoolbox.SuiteClasses;
import com.mazebert.categories.IntegrationTest;
import org.junit.runner.RunWith;

@RunWith(ParallelSuite.class)
@SuiteClasses("**/*Test.class")
@ExcludeCategories({IntegrationTest.class})
public class UnitTestSuite {
}
