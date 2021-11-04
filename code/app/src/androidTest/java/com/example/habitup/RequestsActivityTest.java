package com.example.habitup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.provider.ContactsContract;
import android.text.Editable;
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
    public ActivityTestRule<RequestsActivity> rule =
            new ActivityTestRule<>(RequestsActivity.class, true, true);
    @Before
    public void setup()
    {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        // Sign in
        solo.assertCurrentActivity("Wrong activity.", RequestsActivity.class);
        solo.waitForText("Requests");
        assertTrue(solo.waitForActivity(RequestsActivity.class, 2000));
    }

    @Test
    public void testActivitySwitch() {
        solo.clickOnButton("BACK");
        solo.assertCurrentActivity("Wrong activity.", HabitActivity.class);
        solo.goBack();
    }
}