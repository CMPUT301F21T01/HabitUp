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
    private Boolean type;
    private Integer position;
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
    public Habit(String title, String startDate, String endDate, ArrayList<String> frequency, String reason, Integer progress, Boolean type, Integer position){
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.frequency = frequency;
        this.reason = reason;
        this.progress = progress;
        this.type = type;
        this.position = position;
    }

    /**
     * Returns the title of of calling habit.
     * @return title (String)
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the start date of of calling habit.
     * @return startDate (String)
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Returns the end date of calling habit.
     * @return endDate (String)
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Returns the frequency of calling habit.
     * @return frequency (ArrayList of strings representing frequencies)
     */
    public ArrayList<String> getFrequency() {
        return frequency;
    }

    /**
     * Returns the reason of calling habit.
     * @return reason (String)
     */
    public String getReason(){
        return reason;
    }

    /**
     * Returns the progress of calling habit.
     * @return progress (int)
     */
    public Integer getProgress(){
        return progress;
    }

    /**
     * Return the type of calling habit.
     * @return type (boolean)
     */
    public Boolean getType(){
        return type;
    }

    /**
     * Return the position of calling habit.
     * @return position (integer)
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Sets the title of calling habit.
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the start date of calling habit.
     * @param startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Sets the end date of calling habit.
     * @param endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Sets the frequency of calling habit.
     * @param frequency
     */
    public void setFrequency(ArrayList<String> frequency) {
        this.frequency = frequency;
    }

    /**
     * Sets the reason of calling habit.
     * @param reason
     */
    public void setReason(String reason){
        this.reason = reason;
    }

    /**
     * Sets the progress of calling habit.
     * @param progress
     */
    public void setProgress(Integer progress){
        this.progress = progress;
    }

    /**
     * Sets the type of calling habit.
     * @param type
     */
    public void setType(Boolean type){
        this.type = type;
    }

    /**
     * Sets the position of calling habit.
     * @param position
     */
    public void setPosition(Integer position) {
        this.position = position;
    }
}
