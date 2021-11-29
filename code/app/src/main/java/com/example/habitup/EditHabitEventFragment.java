package com.example.habitup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * EditHabitEventFragment class by Vivian
 * This is a fragment that allows the user to edit a habit event
 */

public class EditHabitEventFragment extends DialogFragment {

    private EditText addReflections;
    private Button addPhoto;
    private Button addLocation;
    private OnFragmentInteractionListener listner;

    private HabitEventInstance habitEventInstance = HabitEventInstance.getInstance();
    private String location = "";
    private String reflection = "";
    private Bitmap photo = null;

    /**
     * This is the fragment interaction listener and handles when ok is pressed
     */
    public interface OnFragmentInteractionListener {
        void onEditOkPressed(HabitEvent habitEvent);
    }

    /**
     * This is called when a fragment is first attached to its context.
     * @param context
     * the context of current activity
     */
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listner = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }

    }


    /**
     * This initializes the creation of the EditHabitEvent fragment
     * @param savedInstanceState
     * bundle that stores the data of current activity
     */
    @NonNull
    @Override
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_habit_event_fragment_layout, null);

        // Create the habit event instance
        HabitEventInstance.getInstance();
        habitEventInstance = HabitEventInstance.getInstance();

        location = habitEventInstance.getLocation();
        reflection = habitEventInstance.getReflection();
        photo = habitEventInstance.getPhoto();

        addReflections = view.findViewById(R.id.add_reflections);
        addReflections.setText(reflection);

        // OnClickListener for addLocation button
        addLocation = (Button) view.findViewById(R.id.add_location_button);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationIntent = new Intent(EditHabitEventFragment.this.getActivity(), AddLocationActivity.class);
                startActivity(locationIntent);
            }
        });

        // OnClickListener for addPhoto button
        addPhoto = (Button) view.findViewById(R.id.add_photo_button);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(EditHabitEventFragment.this.getActivity(), AddPhotographActivity.class);
                startActivity(photoIntent);
                // photo = (Bitmap) photoIntent.getExtras().get("habit_event_photo");;
            }
        });

        // Construct the EditHabitEvent fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view)
                .setTitle("Edit Habit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Final values used for database reference - Vivian
                        reflection = addReflections.getText().toString();
                        location = habitEventInstance.getLocation();
                        photo = habitEventInstance.getPhoto();

                        habitEventInstance.setReflection(reflection);
                        listner.onEditOkPressed(habitEventInstance.editHabitEvent());
                    }
                }).create();
    }

}

