package com.example.habitup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.text.Editable;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HabitActivityTest {
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
    }

    /**
     * Runs all tests sequentially. Add @Test at start to run test.
     * Assure that 'sleep' is not already a habit, as it will be added and deleted here.
     * NOTE * : testHabitList() was designed with only 2 habits in the list: "drink" and "eat".
     *          It will fail if this is not the case.
     */
    public void testAllSequentially() {
        testHabitList();
        testActivitySwitch();
        testAddHabit();
        testDeleteHabit();
    }

    /**
     * Test to see if User's habits are displayed in ListView.
     */
    @Test
    public void testHabitList() {
        String[] habitNames = {"drink", "eat"};
        for(String name : habitNames) {
            assertTrue(solo.searchText(name));
        }
    }

    /**
     * Test to see if activities are switched properly when going to user profile / search.
     */
    @Test
    public void testActivitySwitch() {
        solo.clickOnImageButton(2);
        solo.assertCurrentActivity("Wrong activity.", SearchActivity.class);
        solo.goBack();
        solo.clickOnImageButton(1);
        solo.assertCurrentActivity("Wrong activity.", ProfileActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong activity.", HabitActivity.class);
    }


    /**
     * Test functionality to add / delete habit(s)
     */
    @Test
    public void testAddHabit() {
        // Add habit, assert it was added to the list
        solo.clickOnImageButton(3);
        assertTrue(solo.searchText("Add a Habit"));
        solo.enterText((EditText) solo.getView(R.id.habitEdit), "sleep");
        solo.clickOnCheckBox(3);
        solo.clickOnCheckBox(5);
        solo.enterText((EditText) solo.getView(R.id.reasonEdit), "sleep good");
        solo.clickOnView(solo.getView(android.R.id.button1));
        assertTrue(solo.searchText("sleep"));

        // Click on habit and assert details are true
        solo.clickInList(3);
        assertTrue(solo.isCheckBoxChecked(3) && solo.isCheckBoxChecked(5));
        assertTrue(solo.searchText("sleep"));
        assertTrue(solo.searchText("sleep good"));
        solo.goBack();
    }

    /**
     * Test delete habit functionality. Must be called after testAddHabit() as this
     * method deletes the Habit created within said method.
     */
    @Test
    public void testDeleteHabit() {
        if(solo.searchText("sleep")) {
            // Delete habit, assert it is no longer in the ListView
            solo.clickInList(3);
            solo.clickOnView(solo.getView(R.id.delete_button));
            assertFalse(solo.searchText("sleep"));
        }

    }


}
