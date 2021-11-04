package com.example.habitup;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HabitActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<HabitActivity> rule =
            new ActivityTestRule<>(HabitActivity.class, true, true);

    /**
     * Runs before all tests. Initializes solo instance.
     */
    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

}
