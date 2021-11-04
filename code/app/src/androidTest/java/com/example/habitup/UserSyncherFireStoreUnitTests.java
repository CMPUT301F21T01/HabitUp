package com.example.habitup;

import java.util.ArrayList;

public class UserSyncherFireStoreUnitTests {

    private User mockUser()
    {
        ArrayList<String> test_followers = new ArrayList<String>();
        ArrayList<String> test_requetors = new ArrayList<String>();
        test_followers.add("follow1");
        test_followers.add("follow2");

        test_requetors.add("requester1");
        test_requetors.add("requester2");
        User user = new User("test_username", test_followers, test_requetors, 3);
        return user;

    }

    //test in comparison to the database, actually add a test user with the attributes listed above and ensure they match and sync
    //when a user indicates they want to accept the follow request, we can use compareTo to compare the user list to the user syncher, and to the change in the user


}
