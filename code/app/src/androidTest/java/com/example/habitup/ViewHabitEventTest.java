package com.example.habitup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
 * Tests for HabitEvent and other 'Event' classes
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
        solo.clickOnButton("enter");
        assertTrue(solo.waitForActivity(HabitActivity.class, 2000));
        // Click on first habit. This test assumes there is at least 1 habit
        solo.clickInList(1);
        solo.clickOnButton("Habit Event List");
    }


    @Test
    public void testAddHabitEvent() {
        assertTrue(solo.waitForActivity(HabitEventActivity.class, 2000));
        solo.clickOnView(solo.getView(R.id.add_habit_event_button));
        solo.enterText((EditText) solo.getView(R.id.add_reflections), "Hello World");
        solo.clickOnButton("OK");
        assertTrue(solo.searchText("Hello World"));
    }

    //this test assumes the ADDEVENTTEST habitEvent is added from the previous test,
    // and that there is no other habitEvent with the same name already
    @Test
    public void testViewHabitEvent() {

        assertTrue(solo.searchText("Hello World"));
        // Delete habitEvent, assert it is no longer in the ListView
        ListView listView=(ListView)solo.getView(R.id.habitEvent_list);
        View view=listView.getChildAt(listView.getChildCount()-1);
        ImageButton editButton = (ImageButton)view.findViewById(R.id.edit_habit_event_button);
        solo.clickOnView(editButton);
        Button backButton = (Button)view.findViewById(R.id.habit_event_back_button);
        solo.clickOnView(backButton);

    }

}

