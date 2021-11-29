package com.example.habitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This is an activity that provides an interface to search for other users of the application.
 */
public class SearchActivity extends AppCompatActivity {


    // Global variables
    String g_TAG;

    // Declare variables
    Button backButton;
    Button searchButton;
    EditText searchNameText;
    TextView displayResultText;
    ListView nameList;
    ArrayAdapter<String> nameAdapter;
    ArrayList<String> nameDataList;
    String usernameSearched;

    FirebaseFirestore db;
    String currentUserName = null;
    String currentUsersUsername = null;

    /**
     * Initializes view variables and sets a listener for the search button. When it (the search
     * button) is clicked search through the database and display results as such:
     *      1. If user is found -> display their name
     *      2. If user is not found -> display "No user found"
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize variables
        nameList = findViewById(R.id.name_list);
        nameDataList = new ArrayList<>();
        nameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameDataList);
        nameList.setAdapter(nameAdapter);

        displayResultText = findViewById(R.id.result_text);
        backButton = findViewById(R.id.swap_activity_btn);
        searchButton = findViewById(R.id.search_btn);
        searchNameText = findViewById(R.id.search_field);

        db = FirebaseFirestore.getInstance();
        g_TAG = "TEST_LOG";




        // Search for User
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Reset names list and update adapter
                if(nameDataList.size() > 0) {
                    nameDataList.clear();
                    nameAdapter.notifyDataSetChanged();
                }
                // Get name to search for
                String name = searchNameText.getText().toString();
                usernameSearched = name;
                // Get reference to name
                CollectionReference collRef = db.collection(name);

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

                                            // Get name of user and add to list and get username for use with the syncer
                                            String name = (String)doc.getData().get("name");
                                            nameDataList.add(name);
                                            nameAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    if(userFound && nameList.getVisibility() == View.GONE) {
                                        displayResultText.setVisibility(View.GONE);
                                        nameList.setVisibility(View.VISIBLE);
                                    }
                                    else if (!userFound && nameList.getVisibility() == View.VISIBLE) {
                                        nameList.setVisibility(View.GONE);
                                        displayResultText.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Log.d(g_TAG, "Error getting document: ", task.getException());
                                }
                            }
                        });

                // Hide "no users found" prompt, display ListView
                displayResultText.setVisibility(View.GONE);
                nameList.setVisibility(View.VISIBLE);

            }
        });
        //getting to
        nameList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) { //clicks on the profile of the person that popped up
                String usersName = (String) nameAdapter.getItem(position);
                Log.d(g_TAG, "this is the name" + usersName);
                /*
                check if searched user is in friends
                if so, open the viewprofile activity
                ekb_todo: see public habits
                if not, request to follow activity which means adding this user to the other user's requests

                */
                //unpack the intent from the habit activty so the current user is known in this context
                Bundle extras = getIntent().getExtras();
                if (extras != null)
                {
                    currentUserName = extras.getString("name_of_main_user");
                    currentUsersUsername = extras.getString("username_of_main_user");

                }
                //search for the searched user in friends
                CollectionReference collRefMainUser = db.collection(currentUsersUsername);
                //if the searched isn't one of the friends of current user go to the send requests activity and set current user in searached user's requests
                collRefMainUser.document("friends").collection("current").get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {
                                Intent intent;
                                if(task.isSuccessful())
                                {
                                    boolean userFound = true;
                                    for(DocumentSnapshot doc : task.getResult())
                                    {
                                        if (doc.getId().equals(usernameSearched))
                                        {
                                            userFound = true;
                                        }
                                        else
                                        {
                                            userFound = false;
                                        }
                                    }

                                    if(userFound == true)
                                    {


                                        intent = new Intent(SearchActivity.this, ViewProfile.class);
                                    }
                                    else
                                    {
                                        intent = new Intent(SearchActivity.this, RequestToFollowActivity.class);
                                    }
                                    intent.putExtra("name_of_user", usersName);
                                    intent.putExtra("username_searched", usernameSearched);
                                    intent.putExtra("name_of_main_user", currentUserName);
                                    intent.putExtra("username_of_current_user", currentUsersUsername);
                                    startActivity(intent);
                                }
                            }
                        });


                return true;
            }
        });

        // Return to main activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}