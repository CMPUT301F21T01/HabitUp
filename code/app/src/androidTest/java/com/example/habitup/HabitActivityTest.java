package com.example.habitup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Intent test for HabitActivity
 * @see HabitActivity
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
        solo.enterText((EditText) solo.getView(R.id.username), "dummy2");
        solo.enterText((EditText) solo.getView(R.id.password), "a");
        solo.clickOnButton("sign in");
        assertTrue(solo.waitForActivity(HabitActivity.class, 2000));
    }

    /**
     * Runs all tests sequentially. Add @Test at start to run test through Android Studio.
     * Assure that 'sleep' is not already a habit, as it will be added and deleted here.
     * NOTE * : testHabitList() was hard-coded with the 3 follow habits:
     *              1. "Attend meeting"   2. "Eat food"   3. "Go outside"
     *          This test WILL fail if this is not the case.
     */
    public void testAllSequentially() {
        aTestHabitList();
        bTestActivitySwitch();
        cTestAddHabit();
        dTestDeleteHabit();
        eTestReorder();
    }

    /**
     * Test to see if User's habits are displayed in ListView.
     * NOTE * : testHabitList() was hard-coded with the 3 follow habits:
     *              1. "Attend meeting"   2. "Eat food"   3. "Go outside"
     *          This test WILL fail if this is not the case.
     */
    @Test
    public void aTestHabitList() {
        // habitNames are constants from the database used solely for testing purposes
        String[] habitNames = {"Attend meeting", "Eat food", "Go outside"};
        for(String name : habitNames) {
            assertTrue(solo.searchText(name));
        }
    }

    /**
     * Test to see if activities are switched properly when going to user profile / search.
     */
    @Test
    public void bTestActivitySwitch() {
        solo.clickOnView(solo.getView(R.id.search_activity_btn));
        solo.assertCurrentActivity("Wrong activity.", SearchActivity.class);
        solo.goBack();
        solo.waitForText("Go outside");
        solo.clickOnView(solo.getView(R.id.profile_activity_btn));
        solo.assertCurrentActivity("Wrong activity.", ProfileActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong activity.", HabitActivity.class);
    }


    /**
     * Tests add habit functionality.
     * NOTE * : This test was hard-coded following specifications of testHabitList()
     *          (see testHabitList() description for details).
     */
    @Test
    public void cTestAddHabit() {
        // Add habit, assert it was added to the list
        solo.clickOnView(solo.getView(R.id.add_fab));
        assertTrue(solo.searchText("Add a Habit"));
        solo.enterText((EditText) solo.getView(R.id.habitEdit), "Sleep");
        solo.clickOnCheckBox(3);
        solo.clickOnCheckBox(5);
        solo.enterText((EditText) solo.getView(R.id.reasonEdit), "Sleep good");
        solo.clickOnView(solo.getView(android.R.id.button1));
        assertTrue(solo.searchText("Sleep"));

        // Click on habit and assert details are true
        solo.clickInList(4);
        assertTrue(solo.isCheckBoxChecked(3) && solo.isCheckBoxChecked(5));
        assertTrue(solo.searchText("Sleep"));
        assertTrue(solo.searchText("Sleep good"));
        solo.goBack();
    }

    /**
     * Tests delete habit functionality. Must be called after testAddHabit() as this
     * method deletes the Habit created within said method.
     * NOTE * : This test was hard-coded following specifications of testHabitList()
     *          (see testHabitList() description for details).
     */
    @Test
    public void dTestDeleteHabit() {
        // Delete habit, assert it is no longer in the ListView
        solo.clickInList(4);
        assertTrue(solo.searchText("Sleep"));
        solo.clickOnView(solo.getView(R.id.delete_button));
        assertFalse(solo.searchText("Sleep"));
    }

    @Test
    public void eTestReorder(){
        solo.clickOnButton(1);
        solo.clickOnButton(3);
        solo.clickInList(3);
        assertTrue(solo.searchText("Eat food"));
        solo.goBack();
    }


}
