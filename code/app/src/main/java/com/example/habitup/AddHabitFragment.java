// https://stackoverflow.com/questions/50909962/how-to-set-start-date-and-end-date-in-android

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddHabitFragment extends DialogFragment {

    private EditText title;
    private EditText reason;
    private TextView startText;
    private TextView endText;
    private CheckBox uCheck, mCheck, tCheck, wCheck, rCheck, fCheck, sCheck;
    private ArrayList<String> daysSelected;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onSavePressedAdd(Habit newHabit);
    }

    @Override
    public void onAttach(@NonNull Context context){
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
        ImageButton startButton = view.findViewById(R.id.startButton);
        ImageButton endButton = view.findViewById(R.id.endButton);
        startText = view.findViewById(R.id.start_text);
        endText = view.findViewById(R.id.end_text);

        uCheck = view.findViewById(R.id.sunday_check);
        mCheck = view.findViewById(R.id.monday_check);
        tCheck = view.findViewById(R.id.tuesday_check);
        wCheck = view.findViewById(R.id.wednesday_check);
        rCheck = view.findViewById(R.id.thursday_check);
        fCheck = view.findViewById(R.id.friday_check);
        sCheck = view.findViewById(R.id.saturday_check);
        daysSelected = new ArrayList<>();

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


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentDate(startText);
                    }
            });

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
                        listener.onSavePressedAdd(new Habit(newTitle, startDate, endDate, daysSelected, newReason, 0.0));
                    }
                }).create();
    }

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
}
