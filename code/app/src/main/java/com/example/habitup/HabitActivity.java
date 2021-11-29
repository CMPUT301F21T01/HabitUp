package com.example.habitup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

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
    public static UserSyncer syncer;
    public static User mainUser;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_activity);

        // Variable initializations
        habitList   = findViewById(R.id.habit_listView);
        searchBtn   = findViewById(R.id.search_activity_btn);
        profileBtn  = findViewById(R.id.profile_activity_btn);
        homeBtn     = findViewById(R.id.home_activity_btn);
        addHabitBtn = findViewById(R.id.add_fab);
        userTitle   = findViewById(R.id.user_name);

        db = FirebaseFirestore.getInstance();

        // Get username
        Intent intent   = getIntent();
        String username = intent.getStringExtra(Intent.EXTRA_TEXT);

        // Initialize UserSyncer and HabitLists
        syncer   = UserSyncer.getInstance();
        mainUser = syncer.initialize(username, db);
        habitAdapter = new HabitList(this, mainUser.getHabits());
        habitList.setAdapter(habitAdapter);

        // Set user's name
        userTitle.setText(username + "'s Habits");

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
                Habit selectedHabit = mainUser.getHabits().get(position);

                Intent intent = new Intent(HabitActivity.this, ViewHabitActivity.class);
                intent.putExtra("habit", selectedHabit);
                intent.putExtra("position", position);

                //need the username as well, for the habitEvent later:
                intent.putExtra("username", username);

                startActivityForResult(intent, 1);
            }
        });

        // Collection event listener:
        syncer.getHabitsRef().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                syncer.syncHabits(new UserSyncer.FirebaseCallback() {
                    @Override
                    public void onCallback() {
                        habitlistSort();
                    }
                });
            }
        });
    }

    /**
     * This method is accessed when user returns from AddHabitFragment. It calls for the instance
     * of UserSyncer to add the new habit to Firestore.
     * @param newHabit habit to be added
     */
    @Override
    public void onSavePressedAdd(Habit newHabit) {
        syncer.addHabit(newHabit);
    }

    /**
     * This method overrides onStart() to update the UI info whenever user goes back to HabitActivity
     * from a different Activity.
     */
    // used this image ot help me understand how onStart() works: https://developer.android.com/images/activity_lifecycle.png
    @Override
    protected void onStart() {
        super.onStart();

        // Update adapter and list
        habitAdapter = new HabitList(this, mainUser.getHabits());
        habitList.setAdapter(habitAdapter);
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
                // Get position
                int pos = data.getIntExtra("position", -1);

                // Edit or Delete habit depending on key
                if (!data.getExtras().containsKey("editedHabit")) {
                    syncer.deleteHabit(mainUser.getHabits().get(pos));
                    habitAdapter.remove(habitAdapter.getItem(pos));
                    updateHabitsPosition();
                    habitlistSort();
                } else {
                    String selectedName = mainUser.getHabits().get(pos).getTitle();
                    Habit editedHabit = (Habit) data.getSerializableExtra("editedHabit");
                    syncer.editHabit(selectedName, editedHabit);
                }
            }
        }
    }

    public static void OnUpButtonClick(int position, Habit habit){
        if (position != 0) {
            habitAdapter.getItem(position).setPosition(position-1);
            habitAdapter.getItem(position-1).setPosition(position);
            habitlistSort();
            syncer.editHabit(habit.getTitle(), habit);
            syncer.editHabit(habitAdapter.getItem(position).getTitle(), habitAdapter.getItem(position));
        }
    }

    public static void OnDownButtonClick(int position, Habit habit){
        if (position != habitAdapter.getCount() - 1) {
            habitAdapter.getItem(position).setPosition(position+1);
            habitAdapter.getItem(position+1).setPosition(position);
            habitlistSort();
            syncer.editHabit(habit.getTitle(), habit);
            syncer.editHabit(habitAdapter.getItem(position).getTitle(), habitAdapter.getItem(position));
        }
    }

    public static void updateHabitsPosition(){
        for(int i = 0; i < habitAdapter.getCount(); i++) {
            habitAdapter.getItem(i).setPosition(i);
            syncer.editHabit(habitAdapter.getItem(i).getTitle(), habitAdapter.getItem(i));
        }
    }

    public static void habitlistSort(){
        habitAdapter.sort(new Comparator<Habit>() {
            @Override
            public int compare(Habit h1, Habit h2) {
                return h1.getPosition().compareTo((h2.getPosition()));
            }
        });
        habitAdapter.notifyDataSetChanged();
    }
}