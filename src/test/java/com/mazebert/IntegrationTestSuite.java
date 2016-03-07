package com.mazebert;

import com.googlecode.junittoolbox.IncludeCategories;
import com.googlecode.junittoolbox.SuiteClasses;
import com.googlecode.junittoolbox.WildcardPatternSuite;
import com.mazebert.categories.IntegrationTest;
import org.junit.runner.RunWith;

@RunWith(WildcardPatternSuite.class)
@SuiteClasses("**/*Test.class")
@IncludeCategories({IntegrationTest.class})
public class IntegrationTestSuite {
}
