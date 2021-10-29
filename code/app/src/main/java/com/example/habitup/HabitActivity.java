package com.example.habitup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HabitActivity extends AppCompatActivity implements AddHabitFragment.OnFragmentInteractionListener, ViewHabitFragment.OnFragmentInteractionListener, EditHabitFragment.OnFragmentInteractionListener {

    // Variable declarations
    FloatingActionButton searchBtn;
    FloatingActionButton profileBtn;
    FloatingActionButton homeBtn;
    FloatingActionButton addHabitBtn;
    FloatingActionButton realAddButton;
    ListView habitList;
    public static ArrayAdapter<Habit> habitAdapter;
    public static ArrayList<Habit> habitDataList;

    FirebaseFirestore db;
    final String TAG = "DEBUG_LOG";

    // Temporary until we fully implement the addHabit button *****
    Button tempAddHabitBtn;
    EditText tempAddNameText;
    //                                                        *****

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variable initializations
        habitList = findViewById(R.id.habit_list);
        searchBtn = findViewById(R.id.search_activity_btn);
        profileBtn = findViewById(R.id.profile_activity_btn);
        homeBtn = findViewById(R.id.home_activity_btn);
        addHabitBtn = findViewById(R.id.add_fab);

        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

        db = FirebaseFirestore.getInstance();

        // Get username passed from intent that started this activity
        Intent intent = getIntent();
        String username = (String) intent.getStringExtra(Intent.EXTRA_TEXT);
        final CollectionReference habitsRef = db.collection(username+"/habits/habitList");

        // Temporary until we fully implement addHabit button *****
        tempAddNameText = findViewById(R.id.tempAddNameText);
        tempAddHabitBtn = findViewById(R.id.tempAddHabitBtn);
        //                                                    *****

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

        // Add habit listener
        addHabitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddHabitFragment().show(getSupportFragmentManager(), "ADD_HABIT");
            }
        });

        // Temporary until we fully implement addHabit button                                   *****

        // Add habit button listener
        tempAddHabitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String habitName   = tempAddNameText.getText().toString();

                HashMap<String, String> data = new HashMap<>();
                data.put("name", habitName);

                habitsRef.document("habit" + String.valueOf(habitDataList.size()))
                        .set(data)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Document not added: " + e.toString());
                            }
                        });

                // Reset EditText view to blank
                tempAddNameText.setText("");
            }
        });

        // view habit:
        habitList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Habit selectedHabit = habitDataList.get(position);
                ViewHabitFragment viewFrag = ViewHabitFragment.newInstance(selectedHabit);
                viewFrag.show(getSupportFragmentManager(), "VIEW_HABIT");
                return true;
            }
        });


        // Delete habit (on click) listener
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(HabitActivity.this);
                alert.setTitle("Delete habit?");
                final String selectedName = habitDataList.get(pos).getTitle();
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

        // Collection event listener
        habitsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for(QueryDocumentSnapshot doc : documentSnapshots) {
                    String name = (String)doc.getData().get("name");

                    // Add each habit from database
                    habitDataList.add(new Habit(name));
                }

                habitAdapter.notifyDataSetChanged();
            }
        });

        //                                                                                      *****
    }

    @Override
    public void onSavePressedAdd(Habit newHabit) {
        //firebase stuff goes here
        habitAdapter.add(newHabit);
        habitAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSavePressedEdit(Habit editHabit) {
        // firebase stuff goes here
    }
}
