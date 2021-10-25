package com.example.habitup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class AddHabitFragment extends DialogFragment implements DatePickerFragment.onDateSetListener {

    private EditText title;
    private ImageButton startDate;
    private ImageButton endDate;
    private EditText reason;
    private CheckBox uCheck, mCheck, tCheck, wCheck, rCheck, fCheck, sCheck;
    private ArrayList<String> daysSelected;
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
        endDate = (ImageButton) view.findViewById(R.id.endButton);

        uCheck = view.findViewById(R.id.sunday_check);
        mCheck = view.findViewById(R.id.monday_check);
        tCheck = view.findViewById(R.id.tuesday_check);
        wCheck = view.findViewById(R.id.wednesday_check);
        rCheck = view.findViewById(R.id.thursday_check);
        fCheck = view.findViewById(R.id.friday_check);
        sCheck = view.findViewById(R.id.saturday_check);
        daysSelected = new ArrayList<>();

        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();


        // Dealing with the Sunday-Saturday checkboxes:

        uCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uCheck.isChecked())
                    daysSelected.add("Sunday");
                else
                    daysSelected.remove("Sunday");
            }
        });

        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheck.isChecked())
                    daysSelected.add("Monday");
                else
                    daysSelected.remove("Monday");
            }
        });

        tCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tCheck.isChecked())
                    daysSelected.add("Tuesday");
                else
                    daysSelected.remove("Tuesday");
            }
        });

        wCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wCheck.isChecked())
                    daysSelected.add("Wednesday");
                else
                    daysSelected.remove("Wednesday");
            }
        });

        rCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rCheck.isChecked())
                    daysSelected.add("Thursday");
                else
                    daysSelected.remove("Thursday");
            }
        });

        fCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fCheck.isChecked())
                    daysSelected.add("Friday");
                else
                    daysSelected.remove("Friday");
            }
        });

        sCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sCheck.isChecked())
                    daysSelected.add("Saturday");
                else
                    daysSelected.remove("Saturday");
            }
        });



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
                        String newReason = reason.getText().toString();
                        listener.onSavePressedAdd(new Habit(newTitle, startDate, endDate, daysSelected, newReason, 0.0));
                    }
                }).create();
    }
}
