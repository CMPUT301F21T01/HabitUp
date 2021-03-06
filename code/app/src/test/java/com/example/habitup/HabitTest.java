package com.example.habitup;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * JUNIT Test for Habit Class
 * @see Habit
 */

public class HabitTest {

    /**
     * Tests habit initialization
     */


    @Test
    public void testGet(){
        String title = "work out";
        String startDate = "01-11-2021";
        String endDate = "31-11-2021";
        ArrayList<String> frequency = new ArrayList<>();
        frequency.add("M");
        frequency.add("F");
        String reason = "get strong";
        Integer progress = 40;
        Boolean type = true;
        int position = 0;
        Habit testHabit = new Habit(title, startDate, endDate, frequency,
                reason, progress, type, position);

        assertEquals(title, testHabit.getTitle());
        assertEquals(startDate, testHabit.getStartDate());
        assertEquals(endDate, testHabit.getEndDate());
        assertEquals(frequency, testHabit.getFrequency());
        assertEquals(reason, testHabit.getReason());
        assertEquals(progress, testHabit.getProgress());
        assertEquals(type, testHabit.getType());
    }

    /**
     * Tests habit setters
     */


    @Test
    public void testSet(){
        String title = "work out";
        String startDate = "01-11-2021";
        String endDate = "31-11-2021";
        ArrayList<String> frequency = new ArrayList<>();
        frequency.add("M");
        frequency.add("F");
        String reason = "get strong";
        Integer progress = 40;
        Boolean type = true;
        int position = 0;
        Habit testHabit = new Habit(title, startDate, endDate, frequency,
                reason, progress, type, position);

        String newTitle = "sleep";
        String newStartDate = "15-10-2021";
        String newEndDate = "30-11-2021";
        ArrayList<String> newFrequency = new ArrayList<>();
        newFrequency.add("W");
        String newReason = "need rest";
        Integer newProgress = 30;
        Boolean newType = false;
        int newPosition = 2;

        testHabit.setTitle(newTitle);
        testHabit.setStartDate(newStartDate);
        testHabit.setEndDate(newEndDate);
        testHabit.setFrequency(newFrequency);
        testHabit.setReason(newReason);
        testHabit.setProgress(newProgress);
        testHabit.setType(newType);
        testHabit.setPosition(newPosition);

        assertEquals(newTitle, testHabit.getTitle());
        assertEquals(newStartDate, testHabit.getStartDate());
        assertEquals(newEndDate, testHabit.getEndDate());
        assertEquals(newFrequency, testHabit.getFrequency());
        assertEquals(newReason, testHabit.getReason());
        assertEquals(newProgress, testHabit.getProgress());
        assertEquals(newType, testHabit.getType());
    }


}
