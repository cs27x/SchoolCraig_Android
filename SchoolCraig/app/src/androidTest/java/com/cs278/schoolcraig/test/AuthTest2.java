package com.cs278.schoolcraig.test;

import android.test.AndroidTestCase;
import com.cs278.schoolcraig.data.Auth;


/**
 * Created by Liyan on 11/18/14.
 */
public class AuthTest2 extends AndroidTestCase {
    public void testGetEmail() {
        Auth auth = new Auth("testEmail", "testPassword");
        assertEquals(auth.getEmail(), "testEmail");
    }

    public void testSetEmail() {
        Auth auth2 = new Auth();
        auth2.setEmail("testEmail2");
        assertEquals(auth2.getEmail(), "testEmail2");
    }

    public void testGetPassword() {
        Auth auth3 = new Auth("testEmail3", "testPassword3");
        assertEquals(auth3.getPassword(), "testPassword3");
    }

    public void testSetPassword() {
        Auth auth4 = new Auth();
        auth4.setPassword("testPassword4");
        assertEquals(auth4.getPassword(), "testPassword4");
    }
}
