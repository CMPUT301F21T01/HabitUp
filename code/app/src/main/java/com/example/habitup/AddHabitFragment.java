package com.example.habitup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class AddHabitFragment extends DialogFragment implements DatePickerFragment.onDateSetListener {

    private EditText title;
    private ImageButton startDate;
    private ImageButton endDate;
    private EditText reason;
    public static final int REQUEST_CODE = 9;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onSavePressedAdd(Habit newHabit);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof  OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
            + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_habit_fragment_layout, null);
        title = view.findViewById(R.id.habitEdit);
        reason = view.findViewById(R.id.reasonEdit);
        startDate = (ImageButton) view.findViewById(R.id.startButton);
        endDate = (ImageButton) view.findViewById(R.id.endButton) ;

        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(AddHabitFragment.this, REQUEST_CODE);
                newFragment.show(fm, "datePicker");
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newTitle = title.getText().toString();
                        String newReason = title.getText().toString();
                        listener.onSavePressedAdd(new Habit(newTitle, startDate, endDate, newReason, 0.0));
                    }
                }).create();
    }
}
