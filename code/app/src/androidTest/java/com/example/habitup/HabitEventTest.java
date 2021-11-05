package com.example.habitup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


/**
 * Tests for HabitEvent and other 'Event' classes
 */
public class HabitEventTest {
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
        assertTrue(solo.waitForActivity(HabitActivity.class, 2000));
        // Click on first habit. This test assumes there is at least 1 habit
        solo.clickInList(1);
        solo.clickOnButton("Habit Event List");
    }

    /**
     * Runs all tests sequentially. Add @Test at start to run test.
     */

    public void testAllSequentially() {
        testAddHabitEvent();
    }

    @Test
    public void testAddHabitEvent() {
        assertTrue(solo.waitForActivity(HabitEventActivity.class, 2000));
        solo.clickOnView(solo.getView(R.id.add_habit_event_button));
        solo.enterText((EditText) solo.getView(R.id.add_reflections), "ADDEVENTTEST");
        solo.clickOnButton("OK");
        assertTrue(solo.searchText("ADDEVENTTEST"));
    }
}

