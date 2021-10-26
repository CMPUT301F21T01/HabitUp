package com.example.habitup;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Variable declarations
    FloatingActionButton searchBtn;
    FloatingActionButton profileBtn;
    FloatingActionButton homeBtn;
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;

    Button addHabitButton;
    EditText addNameText;
    EditText addFrequencyText;
    FirebaseFirestore db;

    final String TAG = "DEBUG_LOG";

    // Initialize HabitList method
    private void initHabitList(CollectionReference collRef) {
        collRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            habitDataList.clear();

                            String name, freq;
                            if(!task.getResult().isEmpty()) {
                                for(QueryDocumentSnapshot doc : task.getResult()) {
                                    name = (String)doc.getData().get("name");
                                    freq = (String)doc.getData().get("frequency");

                                    // Add each habit from database
                                    habitDataList.add(new Habit(name, freq));
                                }
                            } else {
                                name = "No habits yet";
                                freq = "";

                                habitDataList.add(new Habit(name, freq));
                            }
                            habitAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting document(s): " + task.getException().toString());
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variable initializations
        habitList        = findViewById(R.id.habit_list);
        addHabitButton   = findViewById(R.id.add_habit_btn);
        addNameText      = findViewById(R.id.add_name_field);
        addFrequencyText = findViewById(R.id.add_freq_field);
        searchBtn        = findViewById(R.id.search_activity_btn);
        profileBtn       = findViewById(R.id.profile_activity_btn);
        homeBtn          = findViewById(R.id.home_activity_btn);

        habitDataList = new ArrayList<>();
        habitAdapter = new CustomList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

        db = FirebaseFirestore.getInstance();

        // Get username passed from intent that started this activity
        Intent intent = getIntent();
        String username = (String) intent.getStringExtra(Intent.EXTRA_TEXT);
        final CollectionReference habitsRef = db.collection(username+"/habits/habitList");

        // Initialize list
        initHabitList(habitsRef);

//        *************************************************************************************
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference newRef = db.collection("tester");
                HashMap<String, String> data = new HashMap();
                data.put("name", "Flappy");
                data.put("password", "buzz23");
                newRef.document("friends")
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) { ; }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                ;
                            }
                        });
            }
        });
//        *************************************************************************************

        // Add habit button listener
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String habitName   = addNameText.getText().toString();
                final String habitFrequency = addFrequencyText.getText().toString();

                HashMap<String, String> data = new HashMap<>();
                data.put("name", habitName);
                data.put("frequency", habitFrequency);

                habitsRef.document("habit" + String.valueOf(habitDataList.size()))
                        .set(data)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Document not added: " + e.toString());
                            }
                        });

                addNameText.setText("");
                addFrequencyText.setText("");
            }
        });



        // Collection event listener
        habitsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for(QueryDocumentSnapshot doc : documentSnapshots) {
                    String name = (String)doc.getData().get("name");
                    String freq = (String)doc.getData().get("frequency");

                    // Add each habit from database
                    habitDataList.add(new Habit(name, freq));
                }

                habitAdapter.notifyDataSetChanged();
            }
        });

        // Delete habit (on click) listener
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Delete habit?");
                final String selectedName = habitDataList.get(pos).getName();
                alert.setMessage("Do you want to delete '" + selectedName + "'?");
                alert.setNegativeButton("Cancel", null);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Delete from our database
                        habitsRef.document("habit" + String.valueOf(pos))
                                .delete()
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Document failed to be deleted: " + e.toString());
                                    }
                                });

                        // Notify adapter of change
                        habitAdapter.notifyDataSetChanged();
                    }
                });
                alert.show();
            }
        });

        // Switch to SearchActivity
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchIntent = new Intent(view.getContext(), SearchActivity.class);
                startActivity(switchIntent);
            }
        });

        // Switch to ProfileActivity
        profileBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent profileSwitchIntent = new Intent(view.getContext(), ProfileActivity.class);
                // Get and pass friends

                // Get and pass friend requests

                // Get and pass name


                // Switch activities
                startActivity(profileSwitchIntent);
            }

        });
    }

}
