package com.example.habitup;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This class is a a dialog fragment that handles the UI and information for when
 * a user wants to edit a habit.
 * It displays add_habit_fragment_layout.xml.
 * Issues: None so far...
 */
public class EditHabitFragment extends DialogFragment {
    // Initialize variables:
    private EditText title;
    private EditText reason;
    private TextView startText;
    private TextView endText;
    private CheckBox uCheck, mCheck, tCheck, wCheck, rCheck, fCheck, sCheck;
    private ArrayList<String> daysSelected;
    private int newProgress;
    private EditHabitFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onSavePressedEdit(Habit editHabit, int position);
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (context instanceof EditHabitFragment.OnFragmentInteractionListener){
            listener = (EditHabitFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    // Get information passed from ViewHabitActivity:
    public static EditHabitFragment newInstance(Habit habit, int position) {
        EditHabitFragment fragment = new EditHabitFragment();
        Bundle args = new Bundle();
        args.putSerializable("habits", habit);
        args.putSerializable("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Habit habit = (Habit) getArguments().getSerializable("habits");
        int pos = getArguments().getInt("position");

        // Assigning and initializing variables:
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_habit_fragment_layout, null);
        title = view.findViewById(R.id.habitEdit);
        reason = view.findViewById(R.id.reasonEdit);
        ImageButton startButton = view.findViewById(R.id.startButton);
        ImageButton endButton = view.findViewById(R.id.endButton);
        startText = view.findViewById(R.id.start_text);
        endText = view.findViewById(R.id.end_text);
        // Assigning checkbox variables:
        uCheck = view.findViewById(R.id.sunday_check);
        mCheck = view.findViewById(R.id.monday_check);
        tCheck = view.findViewById(R.id.tuesday_check);
        wCheck = view.findViewById(R.id.wednesday_check);
        rCheck = view.findViewById(R.id.thursday_check);
        fCheck = view.findViewById(R.id.friday_check);
        sCheck = view.findViewById(R.id.saturday_check);
        // Setting habit's current information variables:
        title.setText(habit.getTitle());
        reason.setText(habit.getReason());
        startText.setText(habit.getStartDate());
        endText.setText(habit.getEndDate());
        daysSelected = habit.getFrequency();

        checkBoxInit(daysSelected);

        // Checking all weekday checkboxes to see if user pressed them
        // and adds them to a daysSelected list if pressed:
        uCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uCheck.isChecked())
                    daysSelected.add("U");
                else
                    daysSelected.remove("U");
            }
        });
        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheck.isChecked())
                    daysSelected.add("M");
                else
                    daysSelected.remove("M");
            }
        });
        tCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tCheck.isChecked())
                    daysSelected.add("T");
                else
                    daysSelected.remove("T");
            }
        });
        wCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wCheck.isChecked())
                    daysSelected.add("W");
                else
                    daysSelected.remove("W");
            }
        });
        rCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rCheck.isChecked())
                    daysSelected.add("R");
                else
                    daysSelected.remove("R");
            }
        });
        fCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fCheck.isChecked())
                    daysSelected.add("F");
                else
                    daysSelected.remove("F");
            }
        });
        sCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sCheck.isChecked())
                    daysSelected.add("S");
                else
                    daysSelected.remove("S");
            }
        });

        // Get user's selected date if StartDate button is clicked:
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentDate(startText);
            }
        });

        // Get user's selected date if EndDate button is clicked:
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentDate(endText);
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
                        String startDate = startText.getText().toString();
                        String endDate = endText.getText().toString();
                        setProgress(startDate, endDate);
                        listener.onSavePressedEdit(new Habit(newTitle, startDate, endDate, daysSelected, newReason, newProgress), pos);
                    }
                }).create();
    }

    /**
     * This method displays a DatepickerDialog and gets the date selected by user.
     * @param setDate the selected date from user
     */
    public void getCurrentDate(TextView setDate) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        SimpleDateFormat selectedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        setDate.setText(selectedDate.format(calendar.getTime()));
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * This method checks every weekday checkbox to see if they have been clicked and
     * sets eat checkbox to true (adds checkmark on each box) if they have been clicked.
     * @param check the list of selected days
     */
    public void checkBoxInit(ArrayList<String> check){
        if(check.isEmpty()){}
        else {
            for (int i = 0; i < check.size(); i++) {
                if (check.get(i).equals("U")) {
                    uCheck.setChecked(true);
                }
                if (check.get(i).equals("M")) {
                    mCheck.setChecked(true);
                }
                if (check.get(i).equals("T")) {
                    tCheck.setChecked(true);
                }
                if (check.get(i).equals("W")) {
                    wCheck.setChecked(true);
                }
                if (check.get(i).equals("R")) {
                    rCheck.setChecked(true);
                }
                if (check.get(i).equals("F")) {
                    fCheck.setChecked(true);
                }
                if (check.get(i).equals("S")) {
                    sCheck.setChecked(true);
                }
            }
        }
    }

    /**
     * This method calculates and sets the progress for a habit.
     * @param startString the starting date for a habit
     * @param endString the ending date for a habit
     */
    public void setProgress(String startString, String endString){
        Date sDate = new Date();
        Date eDate = new Date();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try{
            sDate = format.parse(startString);
            eDate = format.parse(endString);
        } catch (ParseException e){
            e.printStackTrace();
        }

        long current = calendar.getTimeInMillis();
        long difference = eDate.getTime() - sDate.getTime();
        long currentDifference = current - sDate.getTime();

        float progress;
        if(difference == 0) {
            progress = 100;
        } else {
            progress = (float) currentDifference / difference * 100;
            if (progress > 100) {
                progress = 100;
            }
            if (progress < 0) {
                progress = 0;
            }
        }
        newProgress = ((int) progress);
    }
}