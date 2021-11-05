package com.example.habitup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

/**
 * JUNIT test for UserSyncer class.
 * @see UserSyncer
 */
public class UserSyncerTest {

    private UserSyncer mockUserSyncer() { return UserSyncer.getInstance(); }
    private FirebaseFirestore mockDB() { return FirebaseFirestore.getInstance(); }

    @Test
    public void testGetInstance() {
        UserSyncer instance = mockUserSyncer();
        assertNotNull(instance);
    }

    /**
     * Assure data pulled from Firestore is correct and no variables are left uninitialized.
     * @deprecated  This test does not function as it requires 'whitelisted URL patterns in
     *              Google project'. The Dynamic Links domain must be configured within Firebase
     *              to allow 'tools.android.com/tech-docs/unit-testing-support...'.
     */
    //@Test
    public void testInitialization() {
        FirebaseFirestore db = mockDB();
        UserSyncer instance = mockUserSyncer();
        String username = "dummy1";
        String name = "Paige Turner";               // From database (known that this is dummy1's name
        // used to assure name appropriately pulled from db

        User user = instance.initialize(username, db);
        assertNotNull(user);

        // Since Firestore is asynchronous we loop until at least some of the data has been synced
        while(user.getRequests().size() < 1){
            /*
            In the event that UserSyncer fails to sync we will loop indefinitely
            and so the test will fail.
            */
            ;
        }

        // Assert no attributes remain uninitialized
        assertNotNull(instance.getHabitsRef());
        assertNotNull(instance.getRequestsRef());
        assertNotNull(instance.getFriendsRef());
        assertNotNull(instance.getRequestsRef());

        // Assert attributes of User set appropriately
        assertEquals(username, user.getUsername());
        assertEquals(name, user.getName());
        assertTrue(user.getFriends().size() > 0);
        assertTrue(user.getRequests().size() > 0);
    }


}
