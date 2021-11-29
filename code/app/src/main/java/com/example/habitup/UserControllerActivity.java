package com.example.habitup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.habitup.databinding.SignUpFragmentLayoutBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Objects;

/**
 * This activity provides an interface through which a user can sign in.
 */
public class UserControllerActivity extends AppCompatActivity implements SignUpFragment.OnFragmentInteractionListener {
    // Variable declarations
    Button enterButton;
    Button signUpButton;
    EditText usernameField;
    EditText passwordField;

    FirebaseFirestore db;
    final String TAG = "DEBUG_LOG";

    /**
     * Initializes view variables and saves the username which was entered. This username
     * is then passed through an intent to HabitActivity which pulls the appropriate user data
     * from Firestore.
     * @see HabitActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_controller);

        // Variable initializations
        final String[] signInfo = new String[2];
        enterButton   = findViewById(R.id.enter_button);
        signUpButton  = findViewById(R.id.signup_button);
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);

        db = FirebaseFirestore.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SignUpFragment().show(getSupportFragmentManager(), "SIGN_UP");
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInfo[0] = usernameField.getText().toString();
                signInfo[1] = passwordField.getText().toString();
                if(signInfo[1].length() == 0) {
                    Toast.makeText(getApplication().getBaseContext(),
                            "Password must be provided.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                DocumentReference authRef = db.collection(signInfo[0]).document("auth");
                authRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                if(signInfo[1].compareTo(document.getString("password")) == 0) {
                                    // Pass username to HabitActivity intent and start activity
                                    Intent intent = new Intent(view.getContext(), HabitActivity.class);
                                    intent.putExtra(Intent.EXTRA_TEXT, signInfo[0]);
                                    startActivity(intent);
                                } else {
                                    // Passwords do not match
                                    passwordField.setBackgroundResource(R.drawable.sign_in_border_error);
                                    Toast.makeText(getApplication().getBaseContext(),
                                            "Incorrect password. Please try again.",
                                            Toast.LENGTH_LONG).show();
                                    passwordField.setText("");
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            passwordField.setBackgroundResource(R.drawable.sign_in_border);
                                        }
                                    }, 3800);
                                }
                            }
                            else {
                                // Display that user does not exist
                                usernameField.setBackgroundResource(R.drawable.sign_in_border_error);
                                Toast.makeText(getApplication().getBaseContext(),
                                        "Username does not exist.",
                                        Toast.LENGTH_LONG).show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        usernameField.setBackgroundResource(R.drawable.sign_in_border);
                                    }
                                }, 3800);
                            }
                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());
                        }
                    }
                });
            }
        });
        // firestore authentication: https://firebase.google.com/docs/auth/android/start
    }

    /**
     * This method is called when a user attempts to create a new account from SignUpFragment.
     * It assures that a user with the provided username does not already exist, otherwise it
     * adds said user to the database with the provided password and name.
     * @param username of new user
     * @param password of new user
     * @param name     of new user
     */
    @Override
    public void onConfirmPressed(String username, String password, String name) {
        final String[] newUser = new String[3];
        newUser[0] = username;
        newUser[1] = password;
        newUser[2] = name;

        // Create new user (as long as username is not already taken)
        DocumentReference authRef   = db.collection(newUser[0]).document("auth");
        authRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Toast.makeText(getApplication().getBaseContext(),
                                "Username taken. Please pick another username.",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        // Create User
                        HashMap<String, String> data = new HashMap<>();
                        data.put("name", newUser[2]);
                        data.put("password", newUser[1]);
                        authRef.set(data)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplication().getBaseContext(),
                                                "Database may be down: could not create user." +
                                                        "Please try again later.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                        // Pass username to HabitActivity intent and start activity
                        Intent intent = new Intent(getApplicationContext(), HabitActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT, newUser[0]);
                        startActivity(intent);
                    }
                } else {
                    Log.d(TAG, "Error getting document: ", task.getException());
                }
            }
        });


    }
}