package com.cs278.schoolcraig.test;

import android.test.AndroidTestCase;
import com.cs278.schoolcraig.mgmt.UserManagement;

/**
 * Created by Liyan on 11/4/14.
 */

public class UserManagementTest extends AndroidTestCase{
    private UserManagement userMgmt = null;

    public void testAddUserEmailAndGetCurrentUserEmail() {
        assertNotNull(getContext());
        userMgmt = UserManagement.getInstance(getContext());

        userMgmt.addUserEmail("testEmail");
        assertEquals(userMgmt.getCurrentUserEmail(), "testEmail");
    }

    public void testAddUserIdAndGetCurrentUserId() {
        assertNotNull(getContext());
        userMgmt = UserManagement.getInstance(getContext());

        userMgmt.addUserId("testId");
        assertEquals(userMgmt.getCurrentUserId(), "testId");
    }

    public void testClearUserDetails() {
        assertNotNull(getContext());
        userMgmt = UserManagement.getInstance(getContext());

        assertEquals(userMgmt.getCurrentUserEmail(), "testEmail");
        assertEquals(userMgmt.getCurrentUserId(), "testId");

        userMgmt.clearUserDetails();
        assertEquals(userMgmt.getCurrentUserEmail(), null);
        assertEquals(userMgmt.getCurrentUserId(), null);

    }

    public void testUserDetailsExist() {
        assertNotNull(getContext());
        userMgmt = UserManagement.getInstance(getContext());

        userMgmt.addUserEmail("testEmail");
        assertTrue(userMgmt.userDetailsExist());

        userMgmt.clearUserDetails();
        assertFalse(userMgmt.userDetailsExist());
    }
}
