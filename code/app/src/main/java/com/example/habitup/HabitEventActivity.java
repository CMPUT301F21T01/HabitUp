package com.example.habitup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HabitEventActivity extends AppCompatActivity {


    ListView habitEventList;
    ArrayAdapter<HabitEvent> habitEventAdapter;
    ArrayList<HabitEvent> habitEventDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event);

        habitEventList = findViewById(R.id.habitEvent_list);

        String []Locations ={"Take a daily nap", "Eat salads", "Smile", "Read a book","Make your bed","Do your homework","Workout"};
        String []Reflections = {"AB", "BC", "ON", "ON","DS","SD","DS"};


        habitEventDataList = new ArrayList<>();

        for(int i=0;i<Locations.length;i++){
            habitEventDataList.add((new HabitEvent(Reflections[i], Locations[i])));
        }

        habitEventAdapter = new HabitEventList(this, habitEventDataList);

        habitEventList.setAdapter(habitEventAdapter);
    }
}