package com.example.habitup;

import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;


/**
 * Tests for ViewHabitEventActivity and EditHabitEventFragment
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ViewHabitEventTest {
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
        solo.enterText((EditText) solo.getView(R.id.password), "password");
        solo.clickOnButton("sign in");
        assertTrue(solo.waitForActivity(HabitActivity.class, 2000));
        // Click on first habit. This test assumes there is at least 1 habit
        solo.clickInList(1);
        solo.clickOnButton("Habit Event List");
    }

    /**
     * All tests for ViewHabitEventActivity
     */
    @Test
    public void testHabitEvent() {
        testAddHabitEvent();
        testViewHabitEvent();
        testEditHabitEvent();
    }

    /**
     * Add a new habit event for the following tests
     */
    public void testAddHabitEvent() {
        assertTrue(solo.waitForActivity(HabitEventActivity.class, 2000));
        solo.clickOnView(solo.getView(R.id.add_habit_event_button));
        solo.enterText((EditText) solo.getView(R.id.add_reflections), "Hello World");
        solo.clickOnButton("OK");
        assertTrue(solo.searchText("Hello World"));
    }

    /**
     * Test ViewHabitEventActivity
     */
    public void testViewHabitEvent() {
        assertTrue(solo.searchText("Hello World"));
        // Click on edit button
        ListView listView=(ListView)solo.getView(R.id.habitEvent_list);
        View view=listView.getChildAt(listView.getChildCount()-1);
        Button editButton = (Button)view.findViewById(R.id.edit_habit_event_button);
        solo.clickOnView(editButton);
        // Click back button
        solo.clickOnView(solo.getView(R.id.habit_event_back_button));
    }

    /**
     * Test EditHabitEventFragment
     */
    public void testEditHabitEvent() {
        assertTrue(solo.searchText("Hello World"));
        // Click on edit button
        ListView listView=(ListView)solo.getView(R.id.habitEvent_list);
        View view=listView.getChildAt(listView.getChildCount()-1);
        Button editButton = (Button)view.findViewById(R.id.edit_habit_event_button);
        solo.clickOnView(editButton);
        // Click edit button
        solo.clickOnView(solo.getView(R.id.habit_event_edit_button));
        // Edit the reflection
        EditText enterText = (EditText)solo.getView(R.id.add_reflections);
        enterText.getText().clear();
        solo.enterText((EditText) solo.getView(R.id.add_reflections), "It's a beautiful day");
        solo.clickOnButton("OK");
        // Check whether the changes have been applied
        assertTrue(solo.searchText("It's a beautiful day"));
        // Back to the HabitEventList and try check again
        solo.clickOnView(solo.getView(R.id.habit_event_back_button));
        assertTrue(solo.searchText("It's a beautiful day"));
    }
}

