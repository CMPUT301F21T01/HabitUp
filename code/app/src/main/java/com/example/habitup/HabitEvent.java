package com.example.habitup;

import android.graphics.Bitmap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is the class for HabitEvent, storing the variables each HabitEvent needs
 * it as well has constructors and the getters and setters
 * Known Issues: need to edit the main constructor to accept a Date field that can be null(read comment for constructor below)
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

    public String getReflection() {
        return Reflection;
    }

    public void setReflection(String reflection) {
        Reflection = reflection;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) { Image = image; }

    public String getURL() {
        return URL;
    }

    public void setURL(String url) { URL = url; }
}