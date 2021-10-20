package com.example.habitup;

public class Habit {
    private String name;
    private int frequency;

    public Habit(String name, int frequency) {
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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    //endregion

}
