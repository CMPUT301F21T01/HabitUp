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
 * Intent test for ViewHabitActivity
 * @see ViewHabitActivity
 */

public class ViewHabitActivityTest {
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
        solo.enterText((EditText) solo.getView(R.id.username), "dummy1");
        solo.clickOnButton("enter");
        assertTrue(solo.waitForActivity(HabitActivity.class, 2000));
    }

    /**
     * Runs all tests sequentially. Add @Test at start to run test.
     */
    @Test
    public void testAllSequentially() {
        testViewHabitActivity();
        testViewHabitActivityEdit();
        testViewHabitActivityDelete();
    }


    /**
     * Test functionality to add habit(s), DatePickerDialog, and progress specifically
     * @see AddHabitFragment
     * @see HabitList
     */
    public void testViewHabitActivity() {
        // Add habit, assert it was added to the list
        solo.clickOnView(solo.getCurrentActivity().findViewById(R.id.add_fab));
        assertTrue(solo.searchText("Add a Habit"));
        solo.enterText((EditText) solo.getView(R.id.habitEdit), "sleep");
        solo.clickOnView(solo.getView(R.id.startButton));
        solo.setDatePicker(0, 2021, 10, 1);
        solo.clickOnButton("OK");
        solo.clickOnView(solo.getView(R.id.endButton));
        solo.setDatePicker(0, 2021, 10, 30);
        solo.clickOnButton("OK");
        solo.clickOnCheckBox(3);
        solo.clickOnCheckBox(5);
        solo.enterText((EditText) solo.getView(R.id.reasonEdit), "sleep good");
        solo.clickOnView(solo.getView(android.R.id.button1));
        assertTrue(solo.searchText("sleep"));

        // Click on habit and assert details are true
        solo.clickInList(4);
        assertTrue(solo.isCheckBoxChecked(3) && solo.isCheckBoxChecked(5));
        assertTrue(solo.searchText("sleep"));
        assertTrue(solo.searchText("01-11-2021"));
        assertTrue(solo.searchText("30-11-2021"));
        assertTrue(solo.searchText("sleep good"));
        solo.goBack();
    }

    /**
     * Test functionality for edit, make sure progress updating
     * @see EditHabitFragment
     * @see HabitList
     */

    public void testViewHabitActivityEdit() {
        solo.clickInList(4);
        assertTrue(solo.searchText("sleep"));
        solo.clickOnButton("edit");
        assertTrue(solo.searchText("Add a Habit"));
        solo.clearEditText((EditText) solo.getView(R.id.habitEdit));
        solo.enterText((EditText) solo.getView(R.id.habitEdit), "work out");
        solo.clickOnView(solo.getView(R.id.startButton));
        solo.setDatePicker(0, 2021, 9, 15);
        solo.clickOnButton("OK");
        solo.clickOnView(solo.getView(R.id.endButton));
        solo.setDatePicker(0, 2021, 10, 29);
        solo.clickOnButton("OK");
        solo.clickOnCheckBox(8);
        solo.clickOnCheckBox(11);
        solo.clickOnCheckBox(13);
        solo.clickOnCheckBox(10);
        solo.clickOnCheckBox(12);
        solo.clearEditText((EditText) solo.getView(R.id.reasonEdit));
        solo.enterText((EditText) solo.getView(R.id.reasonEdit), "get strong!");
        solo.clickOnView(solo.getView(android.R.id.button1));
        assertTrue(solo.searchText("work out"));
        solo.clickInList(4);
        assertTrue(solo.searchText("work out"));
        assertTrue(solo.searchText("15-10-2021"));
        assertTrue(solo.searchText("29-11-2021"));
        assertTrue(solo.searchText("get strong!"));
        solo.goBack();

    }

    /**
     * Testing functionality for delete, makes sure item is removed
     */

    public void testViewHabitActivityDelete() {
        if(solo.searchText("work out")) {
            // Delete habit, assert it is no longer in the ListView
            solo.clickInList(4);
            assertTrue(solo.searchText("work out"));
            solo.clickOnView(solo.getView(R.id.delete_button));
            assertFalse(solo.searchText("work out"));
        }
    }
}

