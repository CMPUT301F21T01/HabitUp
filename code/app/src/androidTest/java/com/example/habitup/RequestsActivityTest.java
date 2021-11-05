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
public class RequestsActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<RequestsActivity> rule =
            new ActivityTestRule<>(RequestsActivity.class, true, true);
    @Before
    public void setup()
    {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        solo.assertCurrentActivity("Wrong activity.", RequestsActivity.class);
        assertTrue(solo.waitForActivity(RequestsActivity.class, 2000));
    }
    @Test
    /**
     * ensures what we want is showing up in terms of the display for this activity
     */
    public void testDisplay()
    {

        assertTrue(solo.searchText("Requests"));
        assertTrue(solo.searchButton("Go back"));
        assertTrue(solo.waitForView(R.id.requests_list));
    }

    /**
    @Test
     * tests users' abilities to go back to the Profile Activity
     * still being worked on to effectively use solo to do the switches between these activities

    public void testActivitySwitch() {
        solo.clickOnButton("Go back");
        solo.goBackToActivity("ProfileActivity");
        solo.assertCurrentActivity("Wrong activity.", ProfileActivity.class);
        solo.goBackToActivity("RequestsActivity");
    }
     */
}