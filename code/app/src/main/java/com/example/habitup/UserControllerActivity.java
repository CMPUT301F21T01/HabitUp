//handles interactions between the user and the database
//includes the login/signup situation

package com.example.habitup;

/**
 * *for now, we will just be entering a username, storing, and logging in
 * eventually, if a repeat username is attempted a toast will pop up asking to pick a new username
 * as well, potentially authentication via email
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserControllerActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState){

        final String[] name = new String[1];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_controller);

        Button enterButton = findViewById(R.id.enter_button);
        EditText userName = findViewById(R.id.username);

        FirebaseFirestore userDb;
        userDb = FirebaseFirestore.getInstance();
        String g_TAG = "TEST_LOG";
        CollectionReference collRef = userDb.collection("habitup-d4738");

        enterButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        name[0] = userName.getText().toString();
                        collRef.get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            boolean userFound = false;
                                            for(DocumentSnapshot doc : task.getResult()) {
                                                if(doc.getId().equals("auth")) {
                                                    if(!userFound) {
                                                        Log.d(g_TAG, "Found user!");
                                                        userFound = true;
                                                    }


                                                }
                                            }

                                            if(userFound) {
                                                ;
                                            }
                                            else if (!userFound ) {
                                                final CollectionReference userRef = userDb.collection(name[0]);
                                                //auth collection
                                                HashMap<String, String> data = new HashMap<>();
                                                data.put("name", "Harry S");
                                                data.put("password", "1b1d1d");
                                                userRef.document("auth")
                                                        .set(data)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d(g_TAG, "Document added.");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d(g_TAG, "Document not added: " + e.toString());
                                                            }
                                                        });
                                                //friends collection
                                                userRef.document("friends")
                                                        .set("")
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d(g_TAG, "Document added.");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d(g_TAG, "Document not added: " + e.toString());
                                                            }
                                                        });
                                                userRef.document("habits")
                                                        .set("")
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d(g_TAG, "Document added.");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d(g_TAG, "Document not added: " + e.toString());
                                                            }
                                                        });



                                            }
                                        } else {
                                            Log.d(g_TAG, "Error getting document: ", task.getException());
                                        }
                                        Intent nameIntent = new Intent(view.getContext(), MainActivity.class);
                                        nameIntent.putExtra(Intent.EXTRA_TEXT, name[0]);
                                        startActivity(nameIntent);


                                    }
                                });



                        //check if username exists and start next intent --> main activity
                        // if this test fails then create in db
                        //add to the XML, enter your real name
                        //take both of those and make a new collection where the fields are name, username
                        //go into the db, create a new user or go to an existing user
                        //db = FirebaseFirestore.getInstance();
                        //make a new Collection
                        //final CollectionReference collectionRef = db.collection("John");
                    }
                }
        );


        //creates in db, everything will be empty

        // firestore authentication: https://firebase.google.com/docs/auth/android/start


    }
}





