package com.example.habitup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.provider.ContactsContract;
import android.text.Editable;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
public class ProfileActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<ProfileActivity> rule =
            new ActivityTestRule<>(ProfileActivity.class, true, true);
    @Before
    public void setup()
    {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Wrong activity.", ProfileActivity.class);
        assertTrue(solo.waitForActivity(ProfileActivity.class, 2000));
    }

    @Test
    /**
     * ensures what we want is showing up in terms of the display for this activity
     */
    public void testDisplay()
    {

        assertTrue(solo.searchText("Friends"));
        assertTrue(solo.searchButton("View Requests"));
        assertTrue(solo.searchButton("Go back"));
        assertTrue(solo.waitForView(R.id.friend_list));
    }
    @Test
    /**
     * tests users' abilities to switch to the RequestsActivity
     */
    public void testActivitySwitchToRequests() {
        solo.clickOnButton("View Requests");
        solo.assertCurrentActivity("Wrong activity.", RequestsActivity.class);
        solo.goBack();


    }



}
