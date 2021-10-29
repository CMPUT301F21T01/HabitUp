package com.example.habitup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class ViewHabitFragment extends DialogFragment implements EditHabitFragment.OnFragmentInteractionListener {

    private TextView title;
    private TextView reason;
    private TextView startText;
    private TextView endText;
    private Button deleteButton, editButton;
    private CheckBox uCheck, mCheck, tCheck, wCheck, rCheck, fCheck, sCheck;
    private ArrayList<String> daysSelected;
    private ProgressBar progressBar;
    private ViewHabitFragment.OnFragmentInteractionListener listener;
    ArrayList<Habit> dataList;
    ArrayAdapter<Habit> adapter;

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

        dataList = HabitActivity.habitDataList;
        adapter = HabitActivity.habitAdapter;
        title = view.findViewById(R.id.view_habit_title);
        reason = view.findViewById(R.id.view_reason);
        startText = view.findViewById(R.id.start_date);
        endText = view.findViewById(R.id.end_date);
        progressBar = view.findViewById(R.id.view_progress_bar);

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
        progressBar.setProgress(habit.getProgress());

        checkBoxInit(daysSelected);

        // still trying t =o figure out how to do this properly as it doesn't work:
        editButton = view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditHabitFragment editFragment = EditHabitFragment.newInstance(habit);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.rel_layout, editFragment).commit();
            }
        });

        deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.remove(habit);
                adapter.notifyDataSetChanged();
                getFragmentManager().beginTransaction().remove(ViewHabitFragment.this).commit();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("")
                .setNegativeButton("Close", null)
                .create();
    }

    @Override
    public void onSavePressedEdit(Habit editHabit) {

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
