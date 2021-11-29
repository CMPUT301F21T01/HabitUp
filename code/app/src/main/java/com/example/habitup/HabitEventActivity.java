package com.example.habitup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashMap;

/**
 * This class manages the HabitEventActivity, or the screen where you see the HabitEvents for each Habit
 * It establishes connection with the fireStore database, and communicates with it to update,store, and add habitEvents
 */


public class HabitEventActivity extends AppCompatActivity implements AddHabitEventFragment.OnFragmentInteractionListener,GenerateQRHabitEventFragment.OnFragmentInteractionListener {

    HabitEventInstance habitEventInstance = HabitEventInstance.getInstance("", "", null);

    ListView habitEventList;
    ArrayAdapter<HabitEvent> habitEventAdapter;
    ArrayList<HabitEvent> habitEventDataList;

    String username;
    String habitName;

    //this is to communicate with firebase
    public static CollectionReference habitsRef;
    FirebaseFirestore db;
    final String TAG = "DEBUG_LOG"; //for debugging


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event);

        // initialization
        habitEventList = findViewById(R.id.habitEvent_list);
        habitEventDataList = new ArrayList<>();
        habitEventAdapter = new HabitEventList(this, habitEventDataList);
        habitEventList.setAdapter(habitEventAdapter);

        //firebase setup
        db = FirebaseFirestore.getInstance();

        // We are loading the users habitEventList for a particular habit
        // so we need the username+ habitName : (username/habits/habitList/habitName/habitEventList)
        Intent intent = getIntent();
        username = (String) intent.getStringExtra("username");
        habitName = (String) intent.getStringExtra("habitName");
        habitsRef = db.collection(username + "/habits/habitList/" + habitName +"/habitEventList");
        //Log.d("DEBUG_LOG", username + "/habits/habitList/" + habitName +"/habitEventList");
        //we then read from firebase and fill up our HabitEventList from the SnapshotListener! (it also updates whenever there is a change in firebase too)

        ContentResolver result = (ContentResolver) this.getContentResolver();

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

                    String imageRef = username + "/habits/habitList/" + habitName +"/habitEventList/" + Date + "/photo.jpg";
                    Bitmap Image = null;

                    // Add each habitEvent from database
                    HabitEvent newHabitEvent = new HabitEvent(Reflection,Location,Image);
                    newHabitEvent.setDate(Date);
                    newHabitEvent.setURL(imageRef);
                    habitEventDataList.add(newHabitEvent);
                }
                habitEventAdapter.notifyDataSetChanged();
            }
        });

        // add HabitEvent
        final FloatingActionButton addHabitEventButton = findViewById(R.id.add_habit_event_button);
        addHabitEventButton.setOnClickListener((v) -> {
            new AddHabitEventFragment().show(getSupportFragmentManager(), "ADD_HABIT_EVENT");
        });

        // back to previous activity
        final ImageButton backHabitEventButton = findViewById(R.id.back_habit_event_button);
        backHabitEventButton.setOnClickListener((v) -> {
            finish();
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
     * This is the new habit event that is created
     */
    public void onOkPressed(HabitEvent newHabitEvent) {
        // add habitEvent to firebase:

        HashMap<String, String> data = new HashMap<>();
        data.put("location", newHabitEvent.getLocation());
        data.put("reflections", newHabitEvent.getReflection());
        data.put("date", newHabitEvent.getDate());

        habitsRef.document(newHabitEvent.getDate())

                .set(data)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Document not added: " + e.toString());
                    }
                });

        // Store Image to Firebase File Storage
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = newHabitEvent.getImage();

        // Upload image if it is not null
        if (bitmap != null) {

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://habitup-d4738.appspot.com");

            String URL = username + "/habits/habitList/" + habitName +"/habitEventList/" + newHabitEvent.getDate() + "/photo.jpg";
            newHabitEvent.setURL(URL);
            StorageReference imagesRef = storageRef.child(URL);

            // Upload the image
            UploadTask uploadTask = imagesRef.putBytes(imageData);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(TAG, "Document not added: " + exception.toString());
                }
            });


        }

        habitEventAdapter.notifyDataSetChanged();

        // Refresh current activity
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    /**
     * this function deletes a HabitEvent from fireStore when the user presses the delete button on that HabitEvent
     * @param position
     * This is the position of the habit event inside the list
     */
    //delete HabitEvent code
    public void onDeletePressed(int position){
        //get the habitEvent relating to the position of the habit
        HabitEvent habitEvent = habitEventDataList.get(position);

        //this will look for the habitEvent with the same Reflection and delete it
        //the list should auto-update thanks to habitsRef.addSnapshotListener
        habitsRef.document(habitEvent.getDate())//TODO: make this delete of a "title", not their date
                .delete()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Document failed to be deleted: " + e.toString());
                    }
                });

        // delete image from firestore
        String URL = username + "/habits/habitList/" + habitName +"/habitEventList/" + habitEvent.getDate() + "/photo.jpg";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://habitup-d4738.appspot.com");
        StorageReference imagesRef = storageRef.child(habitEvent.getURL());

        imagesRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d(TAG, "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d(TAG, "onFailure: did not delete file");
            }
        });
    }

    /**
     * This opens the ViewHabitEventActivity and displays details of current habit event
     * @param position
     *   This is the position of the habit event inside the list
     */
    // edit HabitEvent code
    public void onEditPressed(int position){
        //get the habitEvent relating to the position of the habit
        HabitEvent currentHabitEvent = habitEventDataList.get(position);

        // Update habit event instance with current habit event
        HabitEventInstance habitEventInstance = HabitEventInstance.getInstance();

        habitEventInstance.setHabitEvent(currentHabitEvent);
        habitEventInstance.setDate(currentHabitEvent.getDate());
        habitEventInstance.setLocation(currentHabitEvent.getLocation());
        habitEventInstance.setReflection(currentHabitEvent.getReflection());
        habitEventInstance.setPhoto(currentHabitEvent.getImage());
        habitEventInstance.setURL(username + "/habits/habitList/" + habitName +"/habitEventList/" + currentHabitEvent.getDate() + "/photo.jpg");

        // Pass username to the ViewHabitEventActivity
        Intent intent = getIntent();
        String username = (String) intent.getStringExtra("username");
        String habitName = (String) intent.getStringExtra("habitName");

        // Start ViewHabitEventActivity
        Intent i = new Intent(HabitEventActivity.this, ViewHabitEventActivity.class);
        i.putExtra("username", username);
        i.putExtra("habitName", habitName);
        startActivity(i);
    }

    /**
     * This opens the GenerateQRHabitEventFragment and displays a QR code with the Reflection+Location text
     * @param position
     *   This is the position of the habit event inside the list
     */
    //create and show QR code
    public void onQRPressed(int position){

        //get the habitEvent relating to the position of the habit
        HabitEvent currentHabitEvent = habitEventDataList.get(position);

        // Update habit event instance with current habit event
        HabitEventInstance habitEventInstance = HabitEventInstance.getInstance();

        habitEventInstance.setHabitEvent(currentHabitEvent);
        habitEventInstance.setDate(currentHabitEvent.getDate());
        habitEventInstance.setLocation(currentHabitEvent.getLocation());
        habitEventInstance.setReflection(currentHabitEvent.getReflection());
        habitEventInstance.setPhoto(currentHabitEvent.getImage());
        habitEventInstance.setURL(username + "/habits/habitList/" + habitName +"/habitEventList/" + currentHabitEvent.getDate() + "/photo.jpg");

        //we launch our new QR fragment!
        new GenerateQRHabitEventFragment().show(getSupportFragmentManager(), "GENERATE_QR_CODE");
    }

}