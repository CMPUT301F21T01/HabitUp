package com.example.habitup;

import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Robotium UI test for UserControllerActivity.
 * @see UserControllerActivity
 */
public class UserControllerActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<UserControllerActivity> rule =
            new ActivityTestRule<>(UserControllerActivity.class, true, true);

    /**
     * Runs before all tests. Initializes solo instance and asserts correct current activity.
     */
    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Wrong activity.", UserControllerActivity.class);
    }

    /**
     * Tests if the display is proper i.e. title of app exists, box to sign in displayed
     * along with 'sign in' button.
     */
    @Test
    public void testDisplay() {
        assertTrue(solo.searchText("HabitUp"));
        assertTrue(solo.searchText("Username"));
        assertTrue(solo.searchText("Password"));
        assertTrue(solo.searchButton("sign in"));
        assertTrue(solo.searchButton("sign up"));
    }

    /**
     * Tests the ability to attempt to sign in as a non-existing user.
     */
    @Test
    public void testFailSignIn() {
        solo.waitForText("HabitUp");
        solo.enterText((EditText)solo.getView(R.id.username), "not_a_user");
        solo.clickOnButton("sign in");
        solo.assertCurrentActivity("Wrong activity.", UserControllerActivity.class);
    }

    /**
     * Tests the ability to attempt to sign in as an existing user but with incorrect password.
     */
    @Test
    public void testFailSignInPassword() {
        solo.waitForText("HabitUp");
        solo.enterText((EditText)solo.getView(R.id.username), "john23");
        solo.enterText((EditText)solo.getView(R.id.password), "incorrect");
        solo.clickOnButton("sign in");
        solo.assertCurrentActivity("Wrong activity.", UserControllerActivity.class);
    }

    /**
     * Tests the ability to sign in successfully.
     */
    @Test
    public void testSuccessSignIn() {
        solo.waitForText("HabitUp");
        solo.enterText((EditText)solo.getView(R.id.username), "john23");
        solo.enterText((EditText)solo.getView(R.id.password), "password");
        solo.clickOnButton("sign in");
        solo.waitForActivity(HabitActivity.class, 2000);
    }
}
