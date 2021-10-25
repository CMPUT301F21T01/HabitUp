package com.example.habitup;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddHabitEventFragment extends DialogFragment {

    private EditText addReflections;
    private Button addPhoto;
    private Button addLocation;
    private OnFragmentInterationListner listner;

    public interface OnFragmentInterationListner {
        void onOkPressed();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof OnFragmentInterationListner) {
            listner = (OnFragmentInterationListner) context;
        } else {
            throw new RuntimeException(context.toString());
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_habit_event_fragment_layout, null);

        addReflections = view.findViewById(R.id.add_reflections);

        // OnClickListener for addLocation button
        addLocation = (Button) view.findViewById(R.id.add_location_button);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(AddHabitEventFragment.this.getActivity(), AddLocation.class);
                startActivity(myIntent);
            }
        });

        // OnClickListener for addPhoto button
        addPhoto = (Button) view.findViewById(R.id.add_photo_button);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(AddHabitEventFragment.this.getActivity(), AddPhotograph.class);
                startActivity(myIntent);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view)
                .setTitle("Add Habit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String reflectionText = addReflections.getText().toString();
                        listner.onOkPressed();
                    }
                }).create();
    }

}


