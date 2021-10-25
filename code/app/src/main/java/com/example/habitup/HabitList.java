package com.example.habitup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

<<<<<<< HEAD:code/app/src/main/java/com/example/habitup/HabitEventList.java
class HabitEventList extends ArrayAdapter<HabitEvent> {
    private ArrayList<HabitEvent> habitEvents;
    private Context context;

    public HabitEventList(Context context, ArrayList<HabitEvent> habitEvents){
        super(context,0, habitEvents);
        this.habitEvents = habitEvents;
=======
public class HabitList extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;

    public HabitList(Context context, ArrayList<Habit> habits){
        super(context,0, habits);
        this.habits = habits;
>>>>>>> main:code/app/src/main/java/com/example/habitup/HabitList.java
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.habiteventcontent, parent,false);
        }

<<<<<<< HEAD:code/app/src/main/java/com/example/habitup/HabitEventList.java
        HabitEvent habitEvent = habitEvents.get(position);

        TextView habitLocation = view.findViewById(R.id.textViewLocation);
        TextView habitReflection = view.findViewById(R.id.textViewReflection);

        habitLocation.setText(habitEvent.getLocation());
        habitReflection.setText(habitEvent.getReflection());
=======
        Habit habit = habits.get(position);

        TextView habitTitle = view.findViewById(R.id.habit_title);

        habitTitle.setText(habit.getTitle());
>>>>>>> main:code/app/src/main/java/com/example/habitup/HabitList.java

        return view;

    }
}