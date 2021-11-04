package com.example.habitup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

public class ViewHabitActivity extends AppCompatActivity implements EditHabitFragment.OnFragmentInteractionListener {
    private TextView title;
    private TextView reason;
    private TextView startText;
    private TextView endText;
    private Button deleteButton, editButton, showListButton;
    private CheckBox uCheck, mCheck, tCheck, wCheck, rCheck, fCheck, sCheck;
    private ArrayList<String> daysSelected;
    private ProgressBar progressBar;
    ArrayList<Habit> dataList;
    ArrayAdapter<Habit> adapter;
    CollectionReference habitsRef;
    final String TAG = "DEBUG_LOG";

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.view_habit);

        Intent myIntent = getIntent();
        Habit habit = (Habit) myIntent.getSerializableExtra("habit");
        int positionOfHabit = myIntent.getIntExtra("position", -1);

        dataList = HabitActivity.habitDataList;
        adapter = HabitActivity.habitAdapter;
        title = findViewById(R.id.view_habit_title);
        reason = findViewById(R.id.view_reason);
        startText = findViewById(R.id.start_date);
        endText = findViewById(R.id.end_date);
        progressBar = findViewById(R.id.view_progress_bar);

        uCheck = findViewById(R.id.view_sunday_check);
        mCheck = findViewById(R.id.view_monday_check);
        tCheck = findViewById(R.id.view_tuesday_check);
        wCheck = findViewById(R.id.view_wednesday_check);
        rCheck = findViewById(R.id.view_thursday_check);
        fCheck = findViewById(R.id.view_friday_check);
        sCheck = findViewById(R.id.view_saturday_check);

        title.setText(habit.getTitle());
        reason.setText(habit.getReason());
        startText.setText(habit.getStartDate());
        endText.setText(habit.getEndDate());
        daysSelected = habit.getFrequency();
        progressBar.setProgress(habit.getProgress());

        checkBoxInit(daysSelected);

        // still trying to figure out how to do this properly as it doesn't work:
        editButton = findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // goto edithabitfragment
                EditHabitFragment editFragment = EditHabitFragment.newInstance(habit, positionOfHabit);
                editFragment.show(getSupportFragmentManager(), "EDIT_HABIT");
            }
        });

        deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send info to habitactivity for firestore shit
                Intent myIntent = new Intent();
                myIntent.putExtra("position", positionOfHabit);
                setResult(RESULT_OK, myIntent);
                finish();
            }
        });

        // Habit event list button - Vivian
        showListButton = findViewById(R.id.habit_event_list_button);
        showListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //HabitEventActivity needs username + habitName passed in!


                Intent intent = new Intent(ViewHabitActivity.this, HabitEventActivity.class);
                //note I use myIntent, not intent to grab username
                String username = (String) myIntent.getStringExtra("username");

                intent.putExtra("username", username);
                intent.putExtra("habitName", habit.getTitle());
                startActivity(intent);
            }
        });
    }

    public void checkBoxInit(@NonNull ArrayList<String> check){
        for (int i = 0; i < check.size(); i++) {
            if (check.get(i).equals("U")) {
                uCheck.setChecked(true);
            }
            if (check.get(i).equals("M")) {
                mCheck.setChecked(true);
            }
            if (check.get(i).equals("T")) {
                tCheck.setChecked(true);
            }
            if (check.get(i).equals("W")) {
                wCheck.setChecked(true);
            }
            if (check.get(i).equals("R")) {
                rCheck.setChecked(true);
            }
            if (check.get(i).equals("F")) {
                fCheck.setChecked(true);
            }
            if (check.get(i).equals("S")) {
                sCheck.setChecked(true);
            }
        }
    }

    @Override
    public void onSavePressedEdit(Habit editHabit, int posOfHabit) {
        // send info to HabitActivity for firestore shit
        Intent myIntent = new Intent();
        myIntent.putExtra("position", posOfHabit);
        myIntent.putExtra("editedHabit", editHabit);
        setResult(RESULT_OK, myIntent);
        finish();
    }
}
