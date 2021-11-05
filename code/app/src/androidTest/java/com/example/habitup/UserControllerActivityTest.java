package com.example.habitup;

import static org.junit.Assert.assertTrue;

import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class UserControllerActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<UserControllerActivity> rule =
            new ActivityTestRule<>(UserControllerActivity.class, true, true);

    /**
     * Runs before all tests. Initializes solo instance.
     */
    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Tests if the display is proper i.e. title of app exists, box to sign in displayed
     * along with 'sign in' button.
     */
    @Test
    public void testDisplay() {
        solo.assertCurrentActivity("Wrong activity.", UserControllerActivity.class);
        assertTrue(solo.searchText("HabitUp"));
        assertTrue(solo.searchText("Enter a username"));
        assertTrue(solo.searchButton("enter"));
    }

    /**
     * Tests ability to sign in as an extant user.
     */
    @Test
    public void testSignIn() {
        solo.assertCurrentActivity("Wrong activity.", UserControllerActivity.class);

        // Sign in
        solo.waitForText("HabitUp");
        solo.enterText((EditText)solo.getView(R.id.username), "john23");
        solo.clickOnButton("enter");
        solo.waitForActivity(HabitActivity.class, 2000);
    }

}
