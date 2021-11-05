package com.example.habitup;


import android.graphics.Bitmap;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * JUNIT Test for HabitEvent Class
 * @see HabitEvent
 */
public class HabitEventJTest {


    /**
     * Tests habitEvent initialization
     */

    @Test
    public void testGet(){
        String reflection = "I was amazing!";
        String location = "58.34N 83.54E";
        String date = "01-11-2021";
        Bitmap image = null;
        HabitEvent testHabitEvent = new HabitEvent(reflection,location,date,image);

        assertEquals(reflection, testHabitEvent.getReflection());
        assertEquals(location, testHabitEvent.getLocation());
        assertEquals(date, testHabitEvent.getDate());
        assertEquals(image, testHabitEvent.getImage());

    }

    /**
     * Tests habit setters
     */


    @Test
    public void testSet(){
        String reflection = "I was amazing!";
        String location = "58.34N 83.54E";
        String date = "01-11-2021";
        Bitmap image = null;
        HabitEvent testHabitEvent = new HabitEvent(reflection,location,date,image);

        String newReflection = "I was horrible!";
        String newLocation = "28.34N 42.54E";
        String newDate = "30-11-2021";
        Bitmap newImage = null;

        testHabitEvent.setReflection(newReflection);
        testHabitEvent.setLocation(newLocation);
        testHabitEvent.setDate(newDate);
        testHabitEvent.setImage(newImage);

        assertEquals(newReflection, testHabitEvent.getReflection());
        assertEquals(newDate, testHabitEvent.getDate());
        assertEquals(newLocation, testHabitEvent.getLocation());
        assertEquals(newImage, testHabitEvent.getImage());
    }
}
