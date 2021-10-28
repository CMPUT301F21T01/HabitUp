package com.example.habitup;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddHabitFragment.OnFragmentInteractionListener,
        EditHabitFragment.OnFragmentInteractionListener, ViewHabitFragment.OnFragmentInteractionListener {

    // Declare the variables so that you will be able to reference it later.
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;
    int position;


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

        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                Habit viewHabit = habitDataList.get(position);
                // creates new DetailsMedFragment and passes selected Medbook into
                // the class to be displayed.
                ViewHabitFragment viewFrag = ViewHabitFragment.newInstance(viewHabit);
                viewFrag.show(getSupportFragmentManager(), "VIEW_HABIT");
            }
        });
    }

    @Override
    public void onSavePressedAdd(Habit newHabit) {
        habitAdapter.add(newHabit);
        habitAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSavePressedEdit(Habit editHabit){
        habitAdapter.add(editHabit);
        habitAdapter.notifyDataSetChanged();
    }
}
