package com.example.habitup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashMap;

/**
 * This class manages the HabitEventActivity, or the screen where you see the HabitEvents for each Habit
 * It establishes connection with the fireStore database, and communicates with it to update,store, and add habitEvents
 * Outstanding issues: read the "to.do" comments below (should be yellow)
 * outsanding issues cont:
 *          need to implement the ability to store, download, and upload photos (Bitmaps) from the fireStore database, as of now we ignore the photo
 *          need to give each HabitEvent a 'title' that can assure they can be unique and so we can order then in Firestore by recently created
 *          as of now we use 'Reflection' as a title but the issue with that is Reflection must be unique for each HabitEvent and each HabitEvent Reflection now has to be unique
 */


public class HabitEventActivity extends AppCompatActivity implements AddHabitEventFragment.OnFragmentInterationListener, EditHabitEventFragment.OnFragmentInterationListener {



    ListView habitEventList;
    ArrayAdapter<HabitEvent> habitEventAdapter;
    ArrayList<HabitEvent> habitEventDataList;


    //this is to communicate with firebase
    public static CollectionReference habitsRef;
    FirebaseFirestore db;
    final String TAG = "DEBUG_LOG"; //for debugging


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event);

        habitEventList = findViewById(R.id.habitEvent_list);
        //buttons

//        String []Locations ={"Take a daily nap", "Eat salads", "Smile", "Read a book","Make your bed","Do your homework","Workout"};
//        String []Reflections = {"AB", "BC", "ON", "ON","DS","SD","DS"};
//

        habitEventDataList = new ArrayList<>();

//        for(int i=0;i<Locations.length;i++){
//            habitEventDataList.add((new HabitEvent(Reflections[i], Locations[i])));
//        }

        habitEventAdapter = new HabitEventList(this, habitEventDataList);

        habitEventList.setAdapter(habitEventAdapter);

        //firebase setup
        db = FirebaseFirestore.getInstance();

        // We are loading the users habitEventList for a particular habit
        // so we need the username+ habitName : (username/habits/habitList/habitName/habitEventList)
        Intent intent = getIntent();
        String username = (String) intent.getStringExtra("username");
        String habitName = (String) intent.getStringExtra("habitName");
        habitsRef = db.collection(username + "/habits/habitList/" + habitName +"/habitEventList");
        //Log.d("DEBUG_LOG", username + "/habits/habitList/" + habitName +"/habitEventList");
        //we then read from firebase and fill up our HabitEventList from the SnapshotListener! (it also updates whenever there is a change in firebase too)

        // Collection event listener
        habitsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitEventDataList.clear();
                for (QueryDocumentSnapshot doc : documentSnapshots) {



                    String Date = (String) doc.getData().get("date"); //TODO: change HabitEvent constructor to accept Date
                    //Bitmap Image = (Bitmap) doc.getData().get("photo"); //TODO: learn how to upload/download Bitmaps!
                    String Reflection = (String) doc.getData().get("reflections");
                    String Location = (String) doc.getData().get("location");
                    //Log.d("DEBUG_LOG",Date + "DATE WE GET");
                    // Add each habitEvent from database
                    habitEventDataList.add(new HabitEvent(Reflection,Location,Date));
                }
                habitEventAdapter.notifyDataSetChanged();
            }
        });

        final FloatingActionButton addCityButton = findViewById(R.id.add_habit_event_button);
        addCityButton.setOnClickListener((v) -> {
            new AddHabitEventFragment().show(getSupportFragmentManager(), "ADD_HABIT_EVENT");
        });

        
        // Handle edit button
        habitEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                HabitEventInstance habitEventInstance = HabitEventInstance.getInstance();
                HabitEvent currentHabitEvent = habitEventDataList.get(pos);

                habitEventInstance.setHabitEvent(currentHabitEvent);
                habitEventInstance.setLocation(currentHabitEvent.getLocation());
                habitEventInstance.setReflection(currentHabitEvent.getReflection());
                habitEventInstance.setPhoto(currentHabitEvent.getImage());

//                ImageButton editHabitEventButton = findViewById(R.id.edit_habit_event_button);
//                editHabitEventButton.setOnClickListener((u) -> {
                new EditHabitEventFragment().show(getSupportFragmentManager(), "EDIT_HABIT_EVENT");
//                });

            }
        });
    }


    /**
     * the onStart function ensures that when a user returns back to this Activity from anywhere, the HabitEventList gets updated accordingly
     */
    //this is when they go back to this activity from somewhere else??? (like a fragment or other activity)????
    @Override
    protected void onStart() {
        super.onStart();

        // update the habitEvent list and the adapter:
        habitEventAdapter = new HabitEventList(this, habitEventDataList);
        habitEventList.setAdapter(habitEventAdapter);
    }

    /**
     * this function updates the Firestore database whenever the user presses the confirm button when adding a HabitEvent
     * @param newHabitEvent
     */
    public void onOkPressed(HabitEvent newHabitEvent) {
        // add habitEvent to firebase:

        HashMap<String, String> data = new HashMap<>();
        data.put("date", newHabitEvent.getDate());
        data.put("location", newHabitEvent.getLocation());
        data.put("photo", "TODO: MAKE THIS PART WORK lol lmao");
        data.put("reflections", newHabitEvent.getReflection());

        //TODO: we currently find and store habitEvents by reflection, we need to add a custom "title' to ensure no duplicates
        //TODO: this also means that new HabitEvents added are not in the order they are added right now, but alphabetical..
        habitsRef.document(newHabitEvent.getReflection())

                .set(data)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Document not added: " + e.toString());
                    }
                });
    }

    /**
     * this function deletes a HabitEvent from fireStore when the user presses the delete button on that HabitEvent
     * @param position
     */
    //delete HabitEvent code
    public void onDeletePressed(int position){
        //get the habitEvent relating to the position of the habit
        HabitEvent habitEvent = habitEventDataList.get(position);

        //this will look for the habitEvent with the same Reflection and delete it
        //the list should auto-update thanks to habitsRef.addSnapshotListener
        habitsRef.document(habitEvent.getReflection())//TODO: make this delete of a "title", not their reflection
                .delete()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Document failed to be deleted: " + e.toString());
                    }
                });



    }



    public void onEditOkPressed(HabitEvent habitEvent) {
        habitEventAdapter.notifyDataSetChanged();

    }

}