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
import java.util.List;

public class HabitActivity extends AppCompatActivity implements AddHabitFragment.OnFragmentInteractionListener {
    // comment
    // Variable declarations
    FloatingActionButton searchBtn;
    FloatingActionButton profileBtn;
    FloatingActionButton homeBtn;
    FloatingActionButton addHabitBtn;
    FloatingActionButton realAddButton;
    ListView habitList;
    public static ArrayAdapter<Habit> habitAdapter;
    public static ArrayList<Habit> habitDataList;
    public static CollectionReference habitsRef;

    FirebaseFirestore db;
    final String TAG = "DEBUG_LOG";

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
        habitsRef = db.collection(username + "/habits/habitList");

        // Switch to SearchActivity
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchIntent = new Intent(view.getContext(), SearchActivity.class);
                startActivity(switchIntent);
            }
        });

        // Switch to ProfileActivity
        profileBtn.setOnClickListener(new View.OnClickListener() {
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

        // view habit
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Habit selectedHabit = habitDataList.get(position);
                Intent intent = new Intent(HabitActivity.this, ViewHabitActivity.class);
                intent.putExtra("habit", selectedHabit);
                intent.putExtra("position", position);
                //need the username as well, for habitEvent later
                intent.putExtra("username", username);

                //i don't know what the below line is but its depreciated?
                //startActivityForResult(intent, 1);
                startActivity(intent);
            }
        });

        // Collection event listener
        habitsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for (QueryDocumentSnapshot doc : documentSnapshots) {


                    String name = (String) doc.getData().get("name");
                    String startDate = (String) doc.getData().get("start date");
                    String endDate = (String) doc.getData().get("end date");
                    String reason = (String) doc.getData().get("reason");
                    String freq = (String) doc.getData().get("frequency");
                    if(freq == null) continue;
                    String prog = (String) doc.getData().get("progress");
                    ArrayList<String> frequency = new ArrayList<String>(Arrays.asList(freq.split(",")));
                    int progress = Integer.parseInt(prog);


                    // Add each habit from database
                    habitDataList.add(new Habit(name, startDate, endDate, frequency, reason, progress));
                }
                habitAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSavePressedAdd(Habit newHabit) {
        // add habit to firebase:

        String frequencyString = String.join(",", newHabit.getFrequency());

        HashMap<String, String> data = new HashMap<>();
        data.put("name", newHabit.getTitle());
        data.put("start date", newHabit.getStartDate());
        data.put("end date", newHabit.getEndDate());
        data.put("frequency", frequencyString);
        data.put("reason", newHabit.getReason());
        data.put("progress", newHabit.getProgress().toString());

        habitsRef.document(newHabit.getTitle())  // CHANGED THIS ******
                //"habit" + String.valueOf(habitDataList.size())
                .set(data)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Document not added: " + e.toString());
                    }
                });
    }

    // overriding onStart() for updating info whenever user goes back to HabitActivity:
    // used this image ot help me understand how onStart() works: https://developer.android.com/images/activity_lifecycle.png
    @Override
    protected void onStart() {
        super.onStart();

        // update the medicine list and the adapter:
        habitAdapter = new HabitList(this, habitDataList);
        habitList.setAdapter(habitAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                if (data.getExtras().containsKey("position") && !data.getExtras().containsKey("editedHabit")) {
                    int pos = data.getIntExtra("position", -1);
                    String selectedName = habitDataList.get(pos).getTitle();
                    habitsRef.document(selectedName)
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
                if (data.getExtras().containsKey("editedHabit")) {
                    Habit editedHabit = (Habit) data.getSerializableExtra("editedHabit");
                    int pos = data.getIntExtra("position", -1);
                    String selectedName = habitDataList.get(pos).getTitle();

                    habitsRef.document(selectedName)
                            .delete()
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Document failed to be deleted: " + e.toString());
                                }
                            });
                    habitAdapter.notifyDataSetChanged();

                    String frequencyString = String.join(",", editedHabit.getFrequency());

                    HashMap<String, String> updateData = new HashMap<>();
                    updateData.put("name", editedHabit.getTitle());
                    updateData.put("start date", editedHabit.getStartDate());
                    updateData.put("end date", editedHabit.getEndDate());
                    updateData.put("frequency", frequencyString);
                    updateData.put("reason", editedHabit.getReason());
                    updateData.put("progress", editedHabit.getProgress().toString());

                    habitsRef.document(editedHabit.getTitle())
                            .set(updateData)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Document not added: " + e.toString());
                                }
                            });
                    habitAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}