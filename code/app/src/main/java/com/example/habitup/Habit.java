package com.example.habitup;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is the class for the creating a Habit object, storing the variables each Habit needs
 * as well as containing constructors for the getters and setters.
 * Known Issues: None so far...
 */
public class Habit implements Serializable {

    private String title;
    private String startDate;
    private String endDate;
    private ArrayList<String> frequency;
    private String reason;
    private Integer progress;
    //alex and vivian add your habit event variable here
    //alex: didn't have too...

    /**
     * public constructor for Habit.
     * @param title name of habit
     * @param startDate starting date of habit
     * @param endDate ending date of habit
     * @param frequency how many times a week the habit occurs
     * @param reason the user's reason for completing the habit
     * @param progress the user's progress over the the entire time they are completeing the habit
     */
    public Habit(String title, String startDate, String endDate, ArrayList<String> frequency, String reason, Integer progress){
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.frequency = frequency;
        this.reason = reason;
        this.progress = progress;
    }

    public String getTitle() {
        return title;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public ArrayList<String> getFrequency() {
        return frequency;
    }

    public String getReason(){
        return reason;
    }

    public Integer getProgress(){
        return progress;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setFrequency(ArrayList<String> frequency) {
        this.frequency = frequency;
    }

    public void setReason(String reason){
        this.reason = reason;
    }

    public void setProgress(Integer progress){
        this.progress = progress;
    }
}
