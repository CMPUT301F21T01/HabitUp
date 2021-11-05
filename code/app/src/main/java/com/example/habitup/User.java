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

    public User() {
        this.username   = null;
        this.name       = null;
        this.friends    = new ArrayList<>();
        this.requests   = new ArrayList<>();
        this.habits     = new ArrayList<>();
    }

    /* Getters & Setters */

    /**
     * Returns user's username.
     * @return username String corresponding to username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets user's username.
     * @param username String to set username to
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns user's name.
     * @return name String corresponding to name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets user's name.
     * @param name String to set name to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns user's friend list.
     * @return friends Array of strings containing friends
     */
    public ArrayList<String> getFriends() {
        return friends;
    }

    /**
     * Returns user's requests list.
     * @return requests Array of strings containing requests
     */
    public ArrayList<String> getRequests() {
        return requests;
    }

    /**
     * Returns user's habits.
     * @return habits
     */
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