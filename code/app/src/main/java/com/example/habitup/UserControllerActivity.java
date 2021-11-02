package com.example.habitup;

/**
 * *for now, we will just be entering a username, storing, and logging in
 * eventually, if a repeat username is attempted a toast will pop up asking to pick a new username
 * as well, potentially authentication via email
 */


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class UserControllerActivity extends AppCompatActivity
{
    // Variable declarations
    Button enterButton;
    EditText usernameField;
    FirebaseFirestore db;
    final String TAG = "DEBUG_LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_controller);

        // Variable initializations
        final String[] name = new String[1];
        enterButton = findViewById(R.id.enter_button);
        usernameField = findViewById(R.id.username);

        db = FirebaseFirestore.getInstance();

        enterButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               name[0] = usernameField.getText().toString();
                                               CollectionReference userRef = db.collection(name[0]);
                                               userRef.get()
                                                       .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                               if(task.isSuccessful()) {

                                                                   // Check if user exists
                                                                   boolean userFound = false;
                                                                   if(!task.getResult().isEmpty())
                                                                       userFound = true;

                                                                   if(userFound) {
                                                                       ;
                                                                   }
                                                                   else if (!userFound ) {
                                                                       // Create auth document
                                                                       HashMap<String, String> data = new HashMap<>();
                                                                       data.put("name", "Harry S.");
                                                                       data.put("password", "1b1ddd");
                                                                       userRef.document("auth")
                                                                               .set(data)
                                                                               .addOnFailureListener(new OnFailureListener() {
                                                                                   @Override
                                                                                   public void onFailure(@NonNull Exception e) {
                                                                                       Log.d(TAG, "Document not added: " + e.toString());
                                                                                   }
                                                                               });
                                                                   }

                                                                   // Pass username to HabitActivity intent and start activity
                                                                   Intent intent = new Intent(view.getContext(), HabitActivity.class);
                                                                   intent.putExtra(Intent.EXTRA_TEXT, name[0]);
                                                                   startActivity(intent);
                                                               } else {
                                                                   Log.d(TAG, "Error getting document: ", task.getException());
                                                               }
                                                           }
                                                       });
                                           }
                                       }
        );
        // firestore authentication: https://firebase.google.com/docs/auth/android/start
    }
}
