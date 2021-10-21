package com.example.habitup;

public class Habit {
    private String name;
    private String frequency;

    public Habit(String name, String frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    //region Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    //endregion

}
