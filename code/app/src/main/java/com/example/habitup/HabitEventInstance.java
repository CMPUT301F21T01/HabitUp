package com.example.habitup;

import android.graphics.Bitmap;

/*
 * Encapsulate data in its own class and expose it as a singleton
 * Reference to: https://eclass.srv.ualberta.ca/mod/forum/discuss.php?d=1791757#p4711112
 * Author: Alexandru Ianta - Friday, 17 September 2021, 4:52 PM
 */
public class HabitEventInstance {

    private static HabitEventInstance instance;

    private HabitEvent habitEvent;

    private String myDate;
    private String myLocation;
    private String myReflection;
    private Bitmap myPhotograph;

    // Constructor of a MedData object
    private HabitEventInstance(String location, String reflection){
        myLocation = location;
        myReflection = reflection;
    }

    // If the instance is not yet create, create a new one and return the instance
    public static HabitEventInstance getInstance(String location, String reflection) {
        if (instance == null) {
            instance = new HabitEventInstance(location, reflection);
        }
        return instance;
    }

    // Return the instance if the instance is already created
    public static HabitEventInstance getInstance(){
        return instance;
    }

    // Setters
    public void setReflection(String reflection) {
        myReflection = reflection;
    }

    public void setLocation(String location) {
        myLocation = location;
    }

    public void setPhoto(Bitmap photo) {
        myPhotograph = photo;
    }

    public void setHabitEvent(HabitEvent habitEvent) {
        this.habitEvent = habitEvent;
    }

    public void setDate(String date) {
        myDate = date;
    }

    // Getters
    public String getDate() {
        return myDate;
    }

    public String getLocation() {
        return myLocation;
    }

    public String getReflection() {
        return myReflection;
    }

    public Bitmap getPhoto() {
        return myPhotograph;
    }

    public HabitEvent getHabitEvent() {
        habitEvent = new HabitEvent(myReflection, myLocation, myPhotograph);
        return habitEvent;
    }

    public HabitEvent editHabitEvent() {
        habitEvent.setLocation(myLocation);
        habitEvent.setReflection(myReflection);
        habitEvent.setImage(myPhotograph);

        return habitEvent;
    }
}
