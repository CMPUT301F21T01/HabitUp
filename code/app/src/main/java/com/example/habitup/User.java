package com.example.habitup;

import java.util.ArrayList;

/**
 * This class represents the data of a user. It will be populated with data pulled from
 * the Firestore database once a user is authenticated (i.e. signs in successfully).
 */
public class User {
    private String username;
    private String name;
    private ArrayList<String> friends;
    private ArrayList<String> requests;
    private ArrayList<Habit> habits;

    public User
            (String username, String name, ArrayList<String> friends, ArrayList<String> requests) {

        this.username = username;
        this.name = name;
        this.friends = friends;
        this.requests = requests;
        this.habits = null;             // To be implemented later
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

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<String> requests) {
        this.requests = requests;
    }

    public ArrayList<Habit> getHabits() {
        return habits;
    }

    public void setHabits(ArrayList<Habit> habits) {
        this.habits = habits;
    }
}
