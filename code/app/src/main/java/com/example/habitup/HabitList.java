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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * HabitEventList acts as HabitActivity's adapter as it is used to display each habit's content
 * in the habit's list in HabitActivity.
 * Known Issues: None so far...
 */
public class HabitList extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;

    public HabitList(Context context, ArrayList<Habit> habits){
        super(context,0, habits);
        this.habits = habits;
        this.context = context;
    }

    /**
     * The getView loads information into the display view content from each Habit
     * and tests to see if any habit is due today.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        Habit habit = habits.get(position);

        // Get and set text view
        TextView habitTitle = view.findViewById(R.id.habit_title);
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        TextView progressText = view.findViewById(R.id.progress_text);
        /* Button upButton = view.findViewById(R.id.button_up);
        Button downButton = view.findViewById(R.id.button_down); */

        habitTitle.setText(habit.getTitle());
        progressBar.setProgress((int) habit.getProgress());
        progressText.setText(habit.getProgress() + "%");

        // changing bg color based on if habit is due today:
        setColor(habit, view);

        return view;

    }

    private void setColor(Habit habit, View view){
        Calendar calendar = Calendar.getInstance();
        String day = LocalDate.now().getDayOfWeek().name();
        if (habit.getFrequency().contains("M") && day.equals("MONDAY"))
            view.setBackgroundColor(Color.parseColor("#8C4CE6"));
        else if (habit.getFrequency().contains("T") && day.equals("TUESDAY"))
            view.setBackgroundColor(Color.parseColor("#8C4CE6"));
        else if (habit.getFrequency().contains("W") && day.equals("WEDNESDAY"))
            view.setBackgroundColor(Color.parseColor("#8C4CE6"));
        else if (habit.getFrequency().contains("R") && day.equals("THURSDAY"))
            view.setBackgroundColor(Color.parseColor("#8C4CE6"));
        else if (habit.getFrequency().contains("F") && day.equals("FRIDAY"))
            view.setBackgroundColor(Color.parseColor("#8C4CE6"));
        else if (habit.getFrequency().contains("S") && day.equals("SATURDAY"))
            view.setBackgroundColor(Color.parseColor("#8C4CE6"));
        else if (habit.getFrequency().contains("U") && day.equals("SUNDAY"))
            view.setBackgroundColor(Color.parseColor("#8C4CE6"));
        else
            view.setBackgroundColor(Color.parseColor("#2B4470"));
    }
}