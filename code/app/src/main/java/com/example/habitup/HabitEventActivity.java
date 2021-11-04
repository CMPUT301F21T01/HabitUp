package com.example.habitup;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HabitEventActivity extends AppCompatActivity implements AddHabitEventFragment.OnFragmentInterationListener, EditHabitEventFragment.OnFragmentInterationListener {


    ListView habitEventList;
    ArrayAdapter<HabitEvent> habitEventAdapter;
    ArrayList<HabitEvent> habitEventDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event);

        habitEventList = findViewById(R.id.habitEvent_list);

//        String []Locations ={"Take a daily nap", "Eat salads", "Smile", "Read a book","Make your bed","Do your homework","Workout"};
//        String []Reflections = {"AB", "BC", "ON", "ON","DS","SD","DS"};
//

        habitEventDataList = new ArrayList<>();

//        for(int i=0;i<Locations.length;i++){
//            habitEventDataList.add((new HabitEvent(Reflections[i], Locations[i])));
//        }

        habitEventAdapter = new HabitEventList(this, habitEventDataList);

        habitEventList.setAdapter(habitEventAdapter);


        final FloatingActionButton addHabitEventButton = findViewById(R.id.add_habit_event_button);
        addHabitEventButton.setOnClickListener((v) -> {
            new AddHabitEventFragment().show(getSupportFragmentManager(), "ADD_HABIT_EVENT");
        });

        // Handle edit button
        habitEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                HabitEventInstance habitEventInstance = HabitEventInstance.getInstance();
                HabitEvent currentHabitEvent = habitEventDataList.get(pos);

                habitEventInstance.setHabitEvent(currentHabitEvent);
                habitEventInstance.setLocation(currentHabitEvent.getLocation());
                habitEventInstance.setReflection(currentHabitEvent.getReflection());
                habitEventInstance.setPhoto(currentHabitEvent.getImage());

//                ImageButton editHabitEventButton = findViewById(R.id.edit_habit_event_button);
//                editHabitEventButton.setOnClickListener((u) -> {
                new EditHabitEventFragment().show(getSupportFragmentManager(), "EDIT_HABIT_EVENT");
//                });

            }
        });
    }

    public void onOkPressed(HabitEvent habitEvent) {
        habitEventAdapter.add(habitEvent);
        habitEventAdapter.notifyDataSetChanged();
    }

    public void onEditOkPressed(HabitEvent habitEvent) {
        habitEventAdapter.notifyDataSetChanged();
    }

}