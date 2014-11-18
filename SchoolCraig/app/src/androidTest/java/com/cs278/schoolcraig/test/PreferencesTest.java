package com.cs278.schoolcraig.test;

import android.test.AndroidTestCase;
import com.cs278.schoolcraig.mgmt.Preferences;

/**
 * Created by Liyan on 11/18/14.
 */
public class PreferencesTest extends AndroidTestCase {

    public void testWritePreferenceAndTestGetSavedValue() {
        assertNotNull(getContext());
        Preferences.getInstance().Initialize(getContext());

        Preferences.getInstance().writePreference("email", "testEmail");
        assertEquals(Preferences.getInstance().getSavedValue("email"), "testEmail");
    }

    public void testDetailsExistsAndClear() {
        assertNotNull(getContext());
        Preferences.getInstance().Initialize(getContext());

        assertTrue(Preferences.getInstance().detailsExist("email"));
        assertFalse(Preferences.getInstance().detailsExist("falseKey"));

        Preferences.getInstance().clear();
        assertFalse(Preferences.getInstance().detailsExist("email"));
    }

}
