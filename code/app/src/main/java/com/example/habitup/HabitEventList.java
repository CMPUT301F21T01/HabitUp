package com.example.habitup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

class HabitEventList extends ArrayAdapter<HabitEvent> {
    private ArrayList<HabitEvent> habitEvents;
    private Context context;

    public HabitEventList(Context context, ArrayList<HabitEvent> habitEvents){
        super(context,0, habitEvents);
        this.habitEvents = habitEvents;
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

        HabitEvent habitEvent = habitEvents.get(position);

        TextView habitDate = view.findViewById(R.id.textViewDate);
        TextView habitLocation = view.findViewById(R.id.textViewLocation);
        TextView habitReflection = view.findViewById(R.id.textViewReflection);
        ImageView habitPhoto = view.findViewById(R.id.imageViewPhoto);

        habitDate.setText(habitEvent.getDate());
        habitLocation.setText(habitEvent.getLocation());
        habitReflection.setText(habitEvent.getReflection());
        habitPhoto.setImageBitmap(habitEvent.getImage());

        // Edit habit event button
        // https://stackoverflow.com/questions/3045872/listview-and-buttons-inside-listview
        ImageButton btn = view.findViewById(R.id.edit_habit_event_button);
        btn.setFocusable(false);
        btn.setClickable(false);

        return view;

    }


}