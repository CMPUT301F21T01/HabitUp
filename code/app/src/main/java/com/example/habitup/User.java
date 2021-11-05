package com.example.habitup;

import java.util.ArrayList;

/**
 * This class represents the data of a user. It will be populated with data pulled from
 * the Firestore database once a user is authenticated (i.e. signs in successfully).
 * @see UserSyncer
 */
public class User {
    private String username;
    private String name;
    private ArrayList<String> friends;
    private ArrayList<String> requests;
    private ArrayList<Habit> habits;

    /**
     * Constructor for User. Attributes are initialized to 'null' or empty and are later populated
     * as data is pulled from Firestore.
     */
    public User() {
        this.username   = null;
        this.name       = null;
        this.friends    = new ArrayList<>();
        this.requests   = new ArrayList<>();
        this.habits     = new ArrayList<>();
    }

    /* Getters & Setters */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    public ArrayList<Habit> getHabits() {
        return habits;
    }

    /**
     * Adds a habit to user's private habit list.
     * @param habit Habit object to add
     * @see Habit
     */
    public void addHabit(Habit habit) {
        this.habits.add(habit);
    }

    /**
     * Adds a friend to given instance of User.
     * @param name Name of user to add to friends
     */
    public void addFriend(String name) {
        this.friends.add(name);
    }

    /**
     * Adds a request to given instance of User.
     * @param name Name of user to add to requests
     */
    public void addRequest(String name) {
        this.requests.add(name);
    }

    /**
     * Clears all habits of a user.
     */
    public void clearHabits() {
        this.habits.clear();
    }
}
