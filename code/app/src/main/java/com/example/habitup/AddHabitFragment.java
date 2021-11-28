// https://stackoverflow.com/questions/50909962/how-to-set-start-date-and-end-date-in-android
// https://stackoverflow.com/questions/8573250/android-how-can-i-convert-string-to-date
// https://stackoverflow.com/questions/67090316/progress-bar-between-two-given-dates-android-studio

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * This class is a a dialog fragment that handles the UI and information for when
 * a user wants to add a new habit.
 * It displays add_habit_fragment_layout.xml.
 * Issues: None so far...
 */
public class AddHabitFragment extends DialogFragment {
    // initialize variables:
    private EditText title;
    private EditText reason;
    private TextView startText;
    private TextView endText;
    private Boolean type;
    private CheckBox uCheck, mCheck, tCheck, wCheck, rCheck, fCheck, sCheck, typeCheck;
    private ArrayList<String> daysSelected;
    private int newProgress;
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
        typeCheck = view.findViewById(R.id.type_check);
        daysSelected = new ArrayList<>();

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
        // Checking to see if user clicked the habit type checkbox to set the habit private:
        type = false;
        typeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = typeCheck.isChecked();
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
                        newProgress = setProgress(startDate, endDate);
                        listener.onSavePressedAdd(new Habit(newTitle, startDate, endDate, daysSelected, newReason, newProgress, type));
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
     * This method calculates and sets the progress for a newly added habit.
     * @param startString the starting date for a habit
     * @param endString the ending date for a habit
     */
    public static int setProgress(String startString, String endString){
        Date sDate = new Date();
        Date eDate = new Date();
        Date currentDate = new Date();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        try{
            sDate = format.parse(startString);
            eDate = format.parse(endString);
        } catch (ParseException e){
            e.printStackTrace();
        }

        long totalDifference = eDate.getTime() - sDate.getTime();
        long days = currentDate.getTime() - sDate.getTime();

        TimeUnit time = TimeUnit.DAYS;
        long difference = time.convert(totalDifference, TimeUnit.MILLISECONDS);
        long daysPassed = time.convert(days, TimeUnit.MILLISECONDS);


        double rate = 1.0/difference;
        double dailyPercentage = daysPassed * rate;

        int progress = 1;
        if (currentDate.getTime() == sDate.getTime())
            progress = 0;
        else if (currentDate.getTime() > eDate.getTime())
            progress = 100;
        else if (difference != 0)
            progress = (int) Math.round(dailyPercentage * 100);

        return progress;
    }
}
