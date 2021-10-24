package com.example.habitup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class HabitActivity extends AppCompatActivity implements AddHabitFragment.OnFragmentInteractionListener {

    // Declare the variables so that you will be able to reference it later.
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        habitList = findViewById(R.id.habit_list);
        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

        final FloatingActionButton addHabit = findViewById(R.id.add_fab);
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddHabitFragment().show(getSupportFragmentManager(), "ADD_MED");
            }
        });

    }

    @Override
    public void onSavePressedAdd(Habit newHabit) {
        habitAdapter.add(newHabit);
        habitAdapter.notifyDataSetChanged();
    }
}
