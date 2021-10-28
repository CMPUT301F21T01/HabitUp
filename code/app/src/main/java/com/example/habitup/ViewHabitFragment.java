package com.example.habitup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class ViewHabitFragment extends DialogFragment {

    private TextView title;
    private TextView reason;
    private TextView startText;
    private TextView endText;
    private CheckBox uCheck, mCheck, tCheck, wCheck, rCheck, fCheck, sCheck;
    private ArrayList<String> daysSelected;
    private ViewHabitFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (context instanceof ViewHabitFragment.OnFragmentInteractionListener){
            listener = (ViewHabitFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    public static ViewHabitFragment newInstance(Habit habit) {
        ViewHabitFragment fragment = new ViewHabitFragment();
        Bundle args = new Bundle();
        args.putSerializable("habits", habit);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Habit habit = (Habit) getArguments().getSerializable("habits");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_habit, null);

        title = view.findViewById(R.id.view_habit_title);
        reason = view.findViewById(R.id.view_reason);
        startText = view.findViewById(R.id.start_date);
        endText = view.findViewById(R.id.end_date);

        uCheck = view.findViewById(R.id.view_sunday_check);
        mCheck = view.findViewById(R.id.view_monday_check);
        tCheck = view.findViewById(R.id.view_tuesday_check);
        wCheck = view.findViewById(R.id.view_wednesday_check);
        rCheck = view.findViewById(R.id.view_thursday_check);
        fCheck = view.findViewById(R.id.view_friday_check);
        sCheck = view.findViewById(R.id.view_saturday_check);

        title.setText(habit.getTitle());
        reason.setText(habit.getReason());
        startText.setText(habit.getStartDate());
        endText.setText(habit.getEndDate());
        daysSelected = habit.getFrequency();

        checkBoxInit(daysSelected);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("")
                .setNegativeButton("Close", null)
                .create();
    }

    public void checkBoxInit(@NonNull ArrayList<String> check){
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


