package com.example.habitup;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class HabitList extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;

    public HabitList(Context context, ArrayList<Habit> habits){
        super(context,0, habits);
        this.habits = habits;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        Habit habit = habits.get(position);

        // changing text based on if habit is due today
        Calendar calendar = Calendar.getInstance();
        String day = LocalDate.now().getDayOfWeek().name();
        //int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (habit.getFrequency().contains("M") && day.equals("MONDAY"))
            view.setBackgroundColor(Color.BLACK);
        else if (habit.getFrequency().contains("T") && day.equals("TUESDAY"))
            view.setBackgroundColor(Color.BLACK);
        else if (habit.getFrequency().contains("W") && day.equals("WEDNESDAY"))
            view.setBackgroundColor(Color.BLACK);
        else if (habit.getFrequency().contains("R") && day.equals("THURSDAY"))
            view.setBackgroundColor(Color.BLACK);
        else if (habit.getFrequency().contains("F") && day.equals("FRIDAY"))
            view.setBackgroundColor(Color.BLACK);
        else if (habit.getFrequency().contains("S") && day.equals("SATURDAY"))
            view.setBackgroundColor(Color.BLACK);
        else if (habit.getFrequency().contains("U") && day.equals("SUNDAY"))
            view.setBackgroundColor(Color.BLACK);
        else
            view.setBackgroundColor(Color.parseColor("#2B4470"));


        // Get and set text view
        TextView habitTitle = view.findViewById(R.id.habit_title);
        // to be uncommented when firebase is able to add:
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        TextView progressText = view.findViewById(R.id.progress_text);

        habitTitle.setText(habit.getTitle());
        // to be uncommented when firebase is ready to add:
        progressBar.setProgress(habit.getProgress());
        progressText.setText(habit.getProgress().toString());

        return view;

    }
}