package com.mazebert;

import com.googlecode.junittoolbox.IncludeCategories;
import com.googlecode.junittoolbox.ParallelSuite;
import com.googlecode.junittoolbox.SuiteClasses;
import com.mazebert.categories.ParallelIntegrationTest;
import org.junit.runner.RunWith;

@RunWith(ParallelSuite.class)
@SuiteClasses("**/*Test.class")
@IncludeCategories({ParallelIntegrationTest.class})
public class ParallelIntegrationTestSuite {
}
