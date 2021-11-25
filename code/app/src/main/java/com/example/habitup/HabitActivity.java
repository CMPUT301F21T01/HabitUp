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
import android.widget.TextView;

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

/**
 * This class manages the HabitActivity.xml, or the screen where you see the list of habits.
 * This class establishes connection with the fireStore database, and communicates with it to update, store, add, edit, and delete habits.
 * Issues: None so far...
 */
public class HabitActivity extends AppCompatActivity implements AddHabitFragment.OnFragmentInteractionListener {
    // Variable declarations
    FloatingActionButton searchBtn;
    FloatingActionButton profileBtn;
    FloatingActionButton homeBtn;
    FloatingActionButton addHabitBtn;
    TextView userTitle;
    ListView habitList;
    public static ArrayAdapter<Habit> habitAdapter;
    public static ArrayList<Habit> habitDataList;
    public static CollectionReference habitsRef;

    public static UserSyncer syncer;
    public static User mainUser;

    FirebaseFirestore db;
    final String TAG = "DEBUG_LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_activity);

        // Variable initializations
        habitList = findViewById(R.id.habit_list);
        searchBtn = findViewById(R.id.search_activity_btn);
        profileBtn = findViewById(R.id.profile_activity_btn);
        homeBtn = findViewById(R.id.home_activity_btn);
        addHabitBtn = findViewById(R.id.add_fab);
        userTitle = findViewById(R.id.user_name);
        // Setting adapter:
        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

        db = FirebaseFirestore.getInstance();

        // Get username passed from intent that started this activity
        Intent intent = getIntent();
        String username = (String) intent.getStringExtra(Intent.EXTRA_TEXT);
        habitsRef = db.collection(username + "/habits/habitList");
        userTitle.setText(username + "'s Habits");

        // UserSyncer implementation testing
        syncer = UserSyncer.getInstance();
        mainUser = syncer.initialize(username, db);

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

                // Get friends, requests and name
                ArrayList<String> friends  = mainUser.getFriends();
                ArrayList<String> requests = mainUser.getRequests();
                String name                = mainUser.getName();

                // Put into intent
                profileSwitchIntent.putExtra("friends", friends);
                profileSwitchIntent.putExtra("requests", requests);
                profileSwitchIntent.putExtra("name", name);

                // Switch activities
                startActivity(profileSwitchIntent);
            }

        });

        // Add habit button listener:
        addHabitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddHabitFragment().show(getSupportFragmentManager(), "ADD_HABIT");
            }
        });

        // Listener for clicking on a habit in the listview:
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // If habit is clicked then go to ViewHabitActivity;
                // Getting current selected habit's information and sending it to ViewHabitActivity:
                Habit selectedHabit = habitDataList.get(position);
                Intent intent = new Intent(HabitActivity.this, ViewHabitActivity.class);
                intent.putExtra("habit", selectedHabit);
                intent.putExtra("position", position);

                //need the username as well, for the habitEvent later:
                intent.putExtra("username", username);

                startActivityForResult(intent, 1);
            }
        });

        habitList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setOnDragListener();
            }
        });

        // Collection event listener:
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

    /**
     * This method is accessed when user returns from AddHabitFragment, it adds the given new habit
     * and stores it into firebase.
     * @param newHabit the given newly added habit
     */
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

    /**
     * This method overrides onStart() to update the UI info whenever user goes back to HabitActivity
     * from a different Activity.
     */
    // used this image ot help me understand how onStart() works: https://developer.android.com/images/activity_lifecycle.png
    @Override
    protected void onStart() {
        super.onStart();

        // update the habitList and notify adapter of change
        habitAdapter = new HabitList(this, habitDataList);
        habitList.setAdapter(habitAdapter);
        syncer = UserSyncer.getInstance();
        mainUser = syncer.getUser();
    }

    /**
     * This method controls the information that ViewHabitActivity returns, which is either information
     * to edit or delete a habit. The method also edits or deletes a Habit while communicating with firebase.
     * @param requestCode 1 if user deletes a habit, 2 if user edits a habit
     * @param resultCode RESULT_OK if transition from previous activity was successful
     * @param data the passed information which is the position of given habit and the edited habit's information
     */
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