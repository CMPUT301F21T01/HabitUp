package com.example.habitup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * This class is a dialog fragment which handles both UI and information passing
 * for when a user wishes to sign up. (Displays sign_up_fragment.xml)
 */
public class SignUpFragment extends DialogFragment {

    // Declaration of Variables
    private EditText username;
    private EditText password;
    private EditText name;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onConfirmPressed(String username, String password, String name);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    "must implement SignUpFragment.OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Variable initializations
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.sign_up_fragment_layout, null);
        username = view.findViewById(R.id.usernameField);
        password = view.findViewById(R.id.passwordField);
        name     = view.findViewById(R.id.nameField);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newUsername = username.getText().toString();
                        String newPassword = password.getText().toString();
                        String newName     = name.getText().toString();
                        listener.onConfirmPressed(newUsername, newPassword, newName);
                    }
                }).create();
    }
}