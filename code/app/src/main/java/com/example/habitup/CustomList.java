package com.example.habitup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<Habit> {

//    private ArrayList<City> cities;
    private ArrayList<Habit> habits;
    private Context context;

    public CustomList(Context context, ArrayList<Habit> habits){
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        Habit habit = habits.get(position);

        // Get text views
        TextView habitName = view.findViewById(R.id.name_text);
        TextView provinceName = view.findViewById(R.id.frequency_text);
        // Set text views
        habitName.setText(habit.getName());
        provinceName.setText(habit.getFrequency());

        return view;
    }
}
