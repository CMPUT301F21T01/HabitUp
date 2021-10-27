package com.example.habitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class HabitActivity extends AppCompatActivity implements AddHabitFragment.OnFragmentInteractionListener {

    // Variable declarations
    FloatingActionButton searchBtn;
    FloatingActionButton profileBtn;
    FloatingActionButton homeBtn;
    FloatingActionButton addHabitBtn;
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;

    FirebaseFirestore db;
    final String TAG = "DEBUG_LOG";

    // Temporary until we fully implement the addHabit button
    Button tempAddHabitBtn;
    EditText addNameText;
    EditText addFrequencyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variable initializations
        habitList       = findViewById(R.id.habit_list);
        searchBtn       = findViewById(R.id.search_activity_btn);
        profileBtn       = findViewById(R.id.profile_activity_btn);
        homeBtn          = findViewById(R.id.home_activity_btn);
        addHabitBtn     = findViewById(R.id.add_fab);

        habitDataList   = new ArrayList<>();
        habitAdapter    = new HabitList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

        db = FirebaseFirestore.getInstance();


        // Switch to SearchActivity
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchIntent = new Intent(view.getContext(), SearchActivity.class);
                startActivity(switchIntent);
            }
        });

        // Switch to ProfileActivity
        profileBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent profileSwitchIntent = new Intent(view.getContext(), ProfileActivity.class);
                // Get and pass friends

                // Get and pass friend requests

                // Get and pass name

                // Switch activities
                startActivity(profileSwitchIntent);
            }

        });

        // Add habit listener
        addHabitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddHabitFragment().show(getSupportFragmentManager(), "ADD_HABIT");
            }
        });

    }

    @Override
    public void onSavePressedAdd(Habit newHabit) {
        habitAdapter.add(newHabit);
        habitAdapter.notifyDataSetChanged();
    }
}
