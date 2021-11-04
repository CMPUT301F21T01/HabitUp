package com.example.habitup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.annotation.UiThread;
import androidx.test.annotation.UiThreadTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SearchActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<SearchActivity> rule =
            new ActivityTestRule<>(SearchActivity.class, true, true);

    @Before
    public void setup() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Wrong activity.", SearchActivity.class);
    }

    /**
     * Tests display i.e. assures there exists a search bar, a button to search, etc.
     */
    @Test
    public void testDisplay() {
        assertTrue(solo.searchText("Enter username"));
        assertTrue(solo.searchButton("Search"));
        assertTrue(solo.searchButton("Go back"));
    }

    /**
     * Tests search functionality. Assures that when searching for an existing user they are
     * appropriately displayed and the negation (i.e. search for non-existing user).
     */
    @Test
    public void testSearch() {
        EditText searchField = (EditText) solo.getView(R.id.search_field);

        // Non-existing user search 1
        solo.enterText(searchField, "blake_029132");
        solo.clickOnButton("Search");
        assertTrue(solo.searchText("No users found."));

        // Existing user search 1
        solo.clearEditText(0);
        solo.enterText(searchField, "katie_j");
        solo.clickOnButton("Search");
        assertTrue(solo.searchText("Katie Jackson"));

        // Existing user search 2
        solo.clearEditText(0);
        solo.enterText(searchField, "john23");
        solo.clickOnButton("Search");
        assertTrue(solo.searchText("Harry S."));

        // Non-existing user search 2
        solo.clearEditText(0);
        solo.enterText(searchField, "sarah31");
        solo.clickOnButton("Search");
        assertTrue(solo.searchText("No users found."));
    }
}
