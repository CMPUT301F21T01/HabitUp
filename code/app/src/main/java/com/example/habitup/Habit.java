package com.example.habitup;

public class Habit {

    private String title;
    private String startDate;
    private String endDate;
    //private String[] frequency;
    private String reason;
    private Float progress;
    //private HabitEvent[] habitEvent;

    Habit(String title, String startDate, String endDate, String reason, Double progress){
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        //this.frequency = frequency;
        this.reason = reason;
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

  //  public String[] getFrequency() {
    //    return frequency;
   // }

    public String getReason(){
        return reason;
    }

    public Float getProgress(){
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

    //public void setFrequency(String[] frequency) {
      //  this.frequency = frequency;
    //}

    public void setReason(String reason){
        this.reason = reason;
    }

    public void setProgress(Float progress){
        this.progress = progress;
    }
}
