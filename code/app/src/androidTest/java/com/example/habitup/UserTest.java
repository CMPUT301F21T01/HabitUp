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
     * the clear habits method is tested in another file so as not to mess with the tests if the tests run in a different order
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

        Habit habit1 = new Habit("sleep early");
        habit1.setStartDate("nov 4, 2021");
        habit1.setEndDate("dec 21, 2021");
        ArrayList<String> frequency1 = new ArrayList<String>();
        frequency1.add("M");
        frequency1.add("W");
        frequency1.add("F");
        habit1.setFrequency(frequency1);
        habit1.setReason("sleep makes me feel happy!");
        habit1.setProgress(50);


        Habit habit2 = new Habit("go biking");
        habit2.setStartDate("nov 4, 2021");
        habit2.setEndDate("dec 22, 2021");
        ArrayList<String> frequency2 = new ArrayList<String>();
        frequency2.add("T");
        frequency2.add("TR");
        habit2.setFrequency(frequency2);
        habit2.setReason("biking makes me feel happy!");
        habit2.setProgress(50);

        habits.add(habit1);
        habits.add(habit2);

        User mockGetterTestingUser = new User();
        mockGetterTestingUser.setName("getName");
        mockGetterTestingUser.setUsername("getUsername");
        mockGetterTestingUser.setFriends(friends);
        mockGetterTestingUser.setRequests(friendRequests);
        mockGetterTestingUser.setHabits(habits);
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
        Habit habit1 = new Habit("sleep early");
        Habit habit2 = new Habit("go biking");
        expectedOutput.add(habit1);
        expectedOutput.add(habit2);
        assertEquals(expectedOutput.get(0).getTitle(), habitsGet.get(0).getTitle());
        assertEquals(expectedOutput.get(1).getTitle(), habitsGet.get(1).getTitle());
    }

    @Test
    public void testAddHabit() {
        User user = mockGetterTestingUser();
        Habit newHabit = new Habit("run 5 km");
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






































