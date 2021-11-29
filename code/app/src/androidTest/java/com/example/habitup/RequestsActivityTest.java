package com.example.habitup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.provider.ContactsContract;
import android.text.Editable;
import android.view.View;
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
    public ActivityTestRule<UserControllerActivity> rule =
            new ActivityTestRule<>(UserControllerActivity.class, true, true);

    /**
     * Runs before all tests. Initializes solo instance and signs in.
     */
    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        // Sign in
        solo.assertCurrentActivity("Wrong activity.", UserControllerActivity.class);
        solo.waitForText("HabitUp");
        solo.enterText((EditText) solo.getView(R.id.username), "john23");
        solo.clickOnButton("enter");
        assertTrue(solo.waitForActivity(HabitActivity.class, 9000));
        View profileButton = solo.getCurrentActivity().findViewById(R.id.profile_activity_btn); //https://stackoverflow.com/questions/33125017/how-to-access-floatingactionmenu-and-floating-action-button-in-robotium
        solo.clickOnView(profileButton);
        assertTrue(solo.waitForActivity(ProfileActivity.class, 9000));
        solo.clickOnButton("View Requests");
        assertTrue(solo.waitForActivity(RequestsActivity.class, 9000));

    }
    @Test
    /**
     * ensures what we want is showing up in terms of the display for this activity
     */
    public void testDisplay()
    {
        solo.assertCurrentActivity("Wrong activity.", RequestsActivity.class);
        assertTrue(solo.searchText("Requests"));
        assertTrue(solo.searchButton("Accept"));
        assertTrue(solo.searchButton("Decline"));
        assertTrue(solo.searchButton("Go back"));
    }



}