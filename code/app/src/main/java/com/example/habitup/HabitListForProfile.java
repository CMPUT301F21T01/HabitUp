package com.example.habitup;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * HabitList acts as HabitActivity's adapter as it is used to display each habit's content
 * in the habit's list in HabitActivity.
 */
public class HabitListForProfile extends ArrayAdapter<Habit>{

    private ArrayList<Habit> habits;
    private Context context;

    /**
     * a constructor for HabitList
     * @param context  context of current activity
     * @param habits the array of habits
     */
    public HabitListForProfile(Context context, ArrayList<Habit> habits){
        super(context,0, habits);
        this.habits = habits;
        this.context = context;
    }

    /**
     * This function "generates" each habit, adding listeners for when they are clicked on as well
     * @param position the position of the habit in the list
     * @param convertView the view we get references from
     * @param parent the parent ViewGroup
     * @return returns the View
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.friendhabitlist, parent,false);
        }

        Habit habit = habits.get(position);

        // Get and set text view
        TextView habitTitle = view.findViewById(R.id.habit_title);
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        TextView progressText = view.findViewById(R.id.progress_text);

        habitTitle.setText(habit.getTitle());
        progressBar.setProgress((int) habit.getProgress());
        progressText.setText(habit.getProgress() + "%");

        return view;

    }
}