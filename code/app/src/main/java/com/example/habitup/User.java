package com.example.habitup;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class User
{
    private String username; //will need to be validated upon listening
    private String name; //implement getters and setters
    private ArrayList<String> followers; //an attribute of users is a list of their current followers
    private ArrayList<String> followerRequests;
    //not on the CRC cards but since our design involves listing the number of habits a user actively has (like when someone goes to request them)
    private int numberOfHabits;


    public User(String username, ArrayList<String> followers, ArrayList<String> followerRequests, int numberOfHabits)
    {
        this.username = username;
        this.followers = followers;
        this.followerRequests = followerRequests;
        this.numberOfHabits = numberOfHabits;
    }
    //getters and setters
    //getters
    public String getUsername()
    {
        return username;
    }

    public String getName() { return name; }

    public ArrayList<String> getFollowers()
    {
        return followers;
    }

    public ArrayList<String> getFollowerRequests()
    {
        return followerRequests;
    }

    public int getNumberOfHabits()
    {
        return numberOfHabits;
    }

    //setters
    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setFollowers(ArrayList<String> followers)
    {
        this.followers = followers;
    }

    public void setFollowerRequests(ArrayList<String> followerRequests)
    {
        this.followerRequests = followerRequests;
    }

    public void setNumberOfHabits(int numberOfHabits)
    {
        this.numberOfHabits = numberOfHabits;
    }
}