package com.example.habitup;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserTest {
    /**
     * tests the getter methods for the User class
     * is not integrated with the database rather is just testing the User class itself
     * since we need to use the setters to set up the user's attributes, the setters are also being implicitly tested
     * @return a user object, mocked object for testing
     */
    private User mockGetterTestingUser() {
        ArrayList<String> friends = new ArrayList<String>();
        friends.add("test_harryS");
        friends.add("test_niallH");
        friends.add("test_louisT");

        ArrayList<String> friendRequests = new ArrayList<String>();
        friendRequests.add("test_zaynM");
        friendRequests.add("test_liamP");
        friendRequests.add("test_elleW");


        ArrayList<Habit> habits = new ArrayList<Habit>();
        ArrayList<String> frequency1;
        frequency1 = new ArrayList<String>();
        frequency1.add("M");
        frequency1.add("W");
        frequency1.add("F");
        Habit habit1 = new Habit("sleep early", "nov 4, 2021", "dec 21, 2021", frequency1, "sleep makes me feel happy!", 50, false, 0);


        ArrayList<String> frequency2 = new ArrayList<String>();
        frequency2.add("T");
        frequency2.add("TR");
        Habit habit2 = new Habit("go biking","nov 4, 2021","dec 22, 2021", frequency2, "biking makes me feel happy!", 50, false, 1);

        habits.add(habit1);
        habits.add(habit2);

        User mockGetterTestingUser = new User();
        mockGetterTestingUser.setName("getName");
        mockGetterTestingUser.setUsername("getUsername");

        // Add friends
        mockGetterTestingUser.addFriend("test_harryS");
        mockGetterTestingUser.addFriend("test_niallH");
        mockGetterTestingUser.addFriend("test_louisT");

        // Add requests
        mockGetterTestingUser.addRequest("test_zaynM");
        mockGetterTestingUser.addRequest("test_liamP");
        mockGetterTestingUser.addRequest("test_elleW");

        // Add habits
        mockGetterTestingUser.addHabit(habit1);
        mockGetterTestingUser.addHabit(habit2);

        return mockGetterTestingUser;
    }

    @Test
    public void testGetUsername() {
        User user = mockGetterTestingUser();
        String username = user.getUsername();
        assertEquals("getUsername", username);
    }

    @Test
    public void testGetName() {
        User user = mockGetterTestingUser();
        String name = user.getName();
        assertEquals("getName", name);
    }

    @Test
    public void testGetFriends() {
        User user = mockGetterTestingUser();
        ArrayList<String> friendsGet = user.getFriends();
        ArrayList<String> expectedOutput = new ArrayList<String>();
        expectedOutput.add("test_harryS");
        expectedOutput.add("test_niallH");
        expectedOutput.add("test_louisT");
        assertArrayEquals(expectedOutput.toArray(), friendsGet.toArray());
    }


    @Test
    public void testGetRequests() {
        User user = mockGetterTestingUser();
        ArrayList<String> requestsGet = user.getRequests();
        ArrayList<String> expectedOutput = new ArrayList<String>();
        expectedOutput.add("test_zaynM");
        expectedOutput.add("test_liamP");
        expectedOutput.add("test_elleW");
        assertArrayEquals(expectedOutput.toArray(), requestsGet.toArray());
    }


    @Test
    public void testGetHabits() {
        User user = mockGetterTestingUser();
        ArrayList<Habit> habitsGet = user.getHabits();
        ArrayList<Habit> expectedOutput = new ArrayList<Habit>();
        ArrayList<String> frequency1;
        frequency1 = new ArrayList<String>();
        frequency1.add("M");
        frequency1.add("W");
        frequency1.add("F");


        ArrayList<String> frequency2 = new ArrayList<String>();
        frequency2.add("T");
        frequency2.add("TR");



        Habit habit1 = new Habit("sleep early", "nov 4, 2021", "dec 21, 2021", frequency1, "sleep makes me feel happy!", 50, false, 0);
        Habit habit2 = new Habit("go biking","nov 4, 2021","dec 22, 2021", frequency2, "biking makes me feel happy!", 50, false, 1);
        expectedOutput.add(habit1);
        expectedOutput.add(habit2);
        assertEquals(expectedOutput.get(0).getTitle(), habitsGet.get(0).getTitle());
        assertEquals(expectedOutput.get(1).getTitle(), habitsGet.get(1).getTitle());
    }

    @Test
    public void testAddHabit() {
        User user = mockGetterTestingUser();


        ArrayList<String> frequency3 = new ArrayList<String>();
        frequency3.add("T");
        frequency3.add("TR");
        frequency3.add("F");
        Habit newHabit = new Habit("run 5 km", "nov 5, 2021", "dec 8, 2021", frequency3, "i like running!", 50, false, 0);
        user.addHabit(newHabit);
        ArrayList<Habit> habits = user.getHabits();
        assertTrue(habits.contains(newHabit));
    }


    @Test
    public void testAddFriend() {
        User user = mockGetterTestingUser();
        String newFriend = "test_beyKC";
        user.addFriend(newFriend);
        ArrayList<String> friends = user.getFriends();
        assertTrue(friends.contains(newFriend));
    }

    @Test
    public void testAddRequest() {
        User user = mockGetterTestingUser();
        String newRequest = "test_kenL";
        user.addRequest(newRequest);
        ArrayList<String> requests = user.getRequests();
        assertTrue(requests.contains(newRequest));
    }

    @Test
    public void clearHabits()
    {
        User user = mockGetterTestingUser();
        user.clearHabits();
        ArrayList<Habit> habitCheck = user.getHabits();
        assertTrue(habitCheck.toArray().length == 0);
    }



}




































