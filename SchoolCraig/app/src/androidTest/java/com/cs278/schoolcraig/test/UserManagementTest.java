package com.cs278.schoolcraig.test;

import android.test.AndroidTestCase;
import com.cs278.schoolcraig.UserManagement;

/**
 * Created by Liyan on 11/4/14.
 */

public class UserManagementTest extends AndroidTestCase{

    public void testAddUserEmailAndGetCurrentUserEmail() {
        assertNotNull(getContext());
        UserManagement userMgmt = UserManagement.getInstance(getContext());

        userMgmt.addUserEmail("testEmail");
        assertEquals(userMgmt.getCurrentUserEmail(), "testEmail");
    }

    public void testClearUserDetails() {
        assertNotNull(getContext());
        UserManagement userMgmt = UserManagement.getInstance(getContext());

        assertEquals(userMgmt.getCurrentUserEmail(), "testEmail");

        userMgmt.clearUserDetails();
        assertEquals(userMgmt.getCurrentUserEmail(), "");
    }

    public void testUserDetailsExist() {
        assertNotNull(getContext());
        UserManagement userMgmt = UserManagement.getInstance(getContext());

        userMgmt.addUserEmail("testEmail");
        assertTrue(userMgmt.userDetailsExist());

        userMgmt.clearUserDetails();
        assertFalse(userMgmt.userDetailsExist());
    }
}
