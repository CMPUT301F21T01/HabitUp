package com.example.habitup;

import android.graphics.Bitmap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is the class for HabitEvent, storing the variables each HabitEvent needs
 * it as well has constructors and the getters and setters
 * Known Issues: None
 */
public class HabitEvent  {

    private String Date;
    private Bitmap Image;
    private String Reflection;
    private String Location;

    private String URL = "";

    /**
     * Constructor for HabitEvent. If no date is provided, we generate a date from current time
     * @param reflection
     * reflection for the habit event
     * @param location
     * location for the habit event
     * @param image
     * image for the habit event
     */
    public HabitEvent(String reflection, String location, Bitmap image) {

        // Get current date
        // https://www.javatpoint.com/java-get-current-date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Date = dtf.format(now);

        Reflection = reflection;
        Location = location;
        Image = image;
    }

    /**
     * Test constructor. Used for the android robotium tests
     * @param reflection the reflection text of the habitEvent
     * @param location the location text of the habitEvent
     * @param date the date text of the habitEvent
     */
    //this is a test constructor for testing without an image
    public  HabitEvent(String reflection, String location, String date){
        Date = date;
        Reflection = reflection;
        Location = location;

    }

    /**
     * Test constructor. Used for the android robotium tests
     * @param reflection the reflection text of the habitEvent
     * @param location the location text of the habitEvent
     * @param date the date text of the habitEvent
     * @param image the image of the habitEvent in Bitmap format
     */
    //another test constructor
    public  HabitEvent(String reflection, String location, String date,Bitmap image){
        Date = date;
        Reflection = reflection;
        Location = location;
        Image = image;
    }

    /**
     * returns the reflection text as a string
     * @return Reflection string
     */
    public String getReflection() {
        return Reflection;
    }

    /**
     * sets the reflection text
     * @param reflection the text we set the reflection too
     */
    public void setReflection(String reflection) {
        Reflection = reflection;
    }

    /**
     * returns the location text as a string
     * @return Location string
     */
    public String getLocation() {
        return Location;
    }

    /**
     * sets the location text
     * @param location the text we set the location too
     */
    public void setLocation(String location) {
        Location = location;
    }

    /**
     * returns the Date, string format
     * @return Date string
     */
    public String getDate() {
        return Date;
    }

    /**
     * sets the date
     * @param date the text we set the date too
     */
    public void setDate(String date) {
        Date = date;
    }

    /**
     * returns the image in Bitmap format
     * @return Image Bitmap
     */
    public Bitmap getImage() {
        return Image;
    }

    /**
     * sets the image
     * @param image the Bitmap we set the image too
     */
    public void setImage(Bitmap image) { Image = image; }

    /**
     * returns the url
     * @return URL string
     */
    public String getURL() {
        return URL;
    }

    /**
     * sets the URL
     * @param url the string we set the URL too
     */
    public void setURL(String url) { URL = url; }
}