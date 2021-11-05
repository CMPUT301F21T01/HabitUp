package com.example.habitup;

import android.graphics.Bitmap;

/**
 * HabitEventInstance class by Vivian
 * This is a class that encapsulate a HabitEvent and expose it as a singleton
 * Reference to: https://eclass.srv.ualberta.ca/mod/forum/discuss.php?d=1791757#p4711112
 * Author: Alexandru Ianta - Friday, 17 September 2021, 4:52 PM
 * Issues: None so far
 */

public class HabitEventInstance {

    private static HabitEventInstance instance;

    private HabitEvent habitEvent;

    private String myDate;
    private String myLocation;
    private String myReflection;
    private Bitmap myPhotograph;

    /**
     * This is the constructor of a HabitEventInstance object
     */
    private HabitEventInstance(String location, String reflection, Bitmap photograph){
        myLocation = location;
        myReflection = reflection;
        myPhotograph = photograph;
    }

    /**
     * This creates an instance of the HabitEventInstance object if it has not yet been created
     */
    public static HabitEventInstance getInstance(String location, String reflection, Bitmap photograph) {
        if (instance == null) {
            instance = new HabitEventInstance(location, reflection, photograph);
        }
        return instance;
    }

    /**
     * This returns the instance of the HabitEventInstance object if the instance is already created
     */
    public static HabitEventInstance getInstance(){
        return instance;
    }

    /**
     * This sets the reflection of the habit event instance
     * @param reflection
     *   This is the candidate reflection to be assigned to the habit event instance
     */
    public void setReflection(String reflection) {
        myReflection = reflection;
    }

    /**
     * This sets the location of the habit event instance
     * @param location
     *   This is the candidate location to be assigned to the habit event instance
     */
    public void setLocation(String location) {
        myLocation = location;
    }

    /**
     * This sets the photograph of the habit event instance
     * @param photo
     *   This is the candidate photograph to be assigned to the habit event instance
     */
    public void setPhoto(Bitmap photo) {
        myPhotograph = photo;
    }

    /**
     * This sets the habitEvent of the habit event instance
     * @param habitEvent
     *   This is the candidate habitEvent to be assigned to the habit event instance
     */
    public void setHabitEvent(HabitEvent habitEvent) {
        this.habitEvent = habitEvent;
    }

    /**
     * This sets the date of the habit event instance
     * @param date
     *   This is the candidate date to be assigned to the habit event instance
     */
    public void setDate(String date) {
        myDate = date;
    }

    /**
     * This returns the location of the habit event instance
     * @return
     *   Return location
     */
    public String getLocation() {
        return myLocation;
    }

    /**
     * This returns the reflection of the habit event instance
     * @return
     *   Return reflection
     */
    public String getReflection() {
        return myReflection;
    }

    /**
     * This returns the photograph of the habit event instance
     * @return
     *   Return photograph
     */
    public Bitmap getPhoto() {
        return myPhotograph;
    }

    /**
     * This returns the date of the habit event instance
     * @return
     *   Return date
     */
    public String getDate() {
        return myDate;
    }

    /**
     * This returns the habitEvent of the habit event instance
     * @return
     *   Return habitEvent
     */
    public HabitEvent getHabitEvent() {
        habitEvent = new HabitEvent(myReflection, myLocation, myPhotograph);
        return habitEvent;
    }

    /**
     * This modifies the current habitEvent and returns the habitEvent of the habit event instance
     * @return
     *   Return habitEvent
     */
    public HabitEvent editHabitEvent() {
        habitEvent.setLocation(myLocation);
        habitEvent.setReflection(myReflection);
        habitEvent.setImage(myPhotograph);

        return habitEvent;
    }
}
