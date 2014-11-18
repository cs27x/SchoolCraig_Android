package com.cs278.schoolcraig.test;

import android.test.AndroidTestCase;
import com.cs278.schoolcraig.data.Category;


/**
 * Created by Liyan on 11/18/14.
 */
public class CategoryTest extends AndroidTestCase {
    public void testGetId() {
        Category category = new Category("testId", "testName");
        assertEquals(category.getId(), "testId");
    }

    public void testSetId() {
        Category category2 = new Category("testName2");
        category2.setId("testId2");
        assertEquals(category2.getId(), "testId2");
    }

    public void testGetName() {
        Category category3 = new Category("testName3");
        assertEquals(category3.getName(), "testName3");
    }

    public void testSetName() {
        Category category4 = new Category();
        category4.setName("testName4");
        assertEquals(category4.getName(), "testName4");
    }
}
