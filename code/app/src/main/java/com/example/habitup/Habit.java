package com.example.habitup;

import java.io.Serializable;
import java.util.ArrayList;

public class Habit implements Serializable {

    private String title;
    private String startDate;
    private String endDate;
    private ArrayList<String> frequency;
    private String reason;
    private Integer progress;
    //private ArrayList<HabitEvent> habitEvent;

    Habit(String title, String startDate, String endDate, ArrayList<String> frequency, String reason, Integer progress){
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
