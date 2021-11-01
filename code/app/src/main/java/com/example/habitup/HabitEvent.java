package com.example.habitup;

import android.graphics.Bitmap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabitEvent  {

    private String Date;
    private Bitmap Image;
    private String Reflection;
    private String Location;

    public HabitEvent(String reflection, String location, Bitmap image) {

        // Get current date
        // https://www.javatpoint.com/java-get-current-date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
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

    public void setImage(Bitmap image) {
        Image = image;
    }
}