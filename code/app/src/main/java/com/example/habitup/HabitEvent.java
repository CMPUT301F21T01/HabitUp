package com.example.habitup;

public class HabitEvent  {

    private String Date;
    private String Image;
    private String Reflection;
    private String Location;

    public HabitEvent(String reflection, String location) {
        Reflection = reflection;
        Location = location;
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


}