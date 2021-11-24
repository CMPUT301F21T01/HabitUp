package com.example.habitup;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

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


    FirebaseFirestore db;

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

                                            // Get name of user and add to list
                                            String name = (String)doc.getData().get("name");
                                            nameDataList.add(name);
                                            nameAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    if(userFound && nameList.getVisibility() == View.GONE) {
                                        displayResultText.setVisibility(View.GONE);
                                        nameList.setVisibility(View.VISIBLE);
                                        nameList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                            @Override
                                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) { //clicks on the profile of the person that popped up
                                                String name = nameDataList.get(position);
                                                //conditional to check if the user has this person as a follower/requests
                                                //for now just starts the requests process
                                                Intent intent = new Intent(SearchActivity.this, RequestActivity.class);
                                                intent.putExtra(EXTRA_MESSAGE, name);
                                                startActivity(intent);

                                                return true;
                                            }
                                        });
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

        // Return to main activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}