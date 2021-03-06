package com.example.habitup;

import static org.junit.Assert.assertTrue;
import android.view.View;
import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProfileActivityTest {
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
        assertTrue(solo.waitForActivity(HabitActivity.class, 9000));
        View profileButton = solo.getCurrentActivity().findViewById(R.id.profile_activity_btn); //https://stackoverflow.com/questions/33125017/how-to-access-floatingactionmenu-and-floating-action-button-in-robotium
        solo.clickOnView(profileButton);
        assertTrue(solo.waitForActivity(ProfileActivity.class, 9000));

    }

    //ensures the display shows up as we want it to
    @Test
    public void testDisplay()
    {
        solo.assertCurrentActivity("Wrong activity.", ProfileActivity.class);
        assertTrue(solo.searchButton("View Requests"));
        assertTrue(solo.searchButton("Go back"));
    }

    @Test
    public void testActivitySwitchToRequests() {
        solo.clickOnButton("View Requests");
        solo.assertCurrentActivity("Wrong activity.", RequestsActivity.class);
        solo.goBack();
    }





}