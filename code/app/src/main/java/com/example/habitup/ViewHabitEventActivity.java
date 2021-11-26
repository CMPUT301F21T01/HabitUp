package com.example.habitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * ViewHabitEventActivity class by Vivian
 * This is an activity that allows the user to view a habit event
 * Issues: None so far
 */

public class ViewHabitEventActivity extends AppCompatActivity implements EditHabitEventFragment.OnFragmentInteractionListener {

    private HabitEventInstance habitEventInstance = HabitEventInstance.getInstance();
    private String date = "Date: ";
    private String location = "Location: ";
    private String reflection = "Comment: ";
    private Bitmap photo = null;

    /**
     * This initializes the creation of the ViewHabitEventActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit_event);

        // Get current HabitEvent data from the HabitEventInstance
        HabitEventInstance.getInstance();
        habitEventInstance = HabitEventInstance.getInstance();

        date += habitEventInstance.getDate();
        location += habitEventInstance.getLocation();
        reflection += habitEventInstance.getReflection();
        // photo = habitEventInstance.getPhoto();

        // Set TextView and ImageView for current HabitEvent
        TextView habitEventDate = findViewById(R.id.habit_event_date);
        habitEventDate.setText(date);

        TextView habitEventLocation = findViewById(R.id.habit_event_location);
        habitEventLocation.setText(location);

        TextView habitEventReflection = findViewById(R.id.habit_event_reflection);
        habitEventReflection.setText(reflection);

        ImageView habitEventPhoto = findViewById(R.id.habit_event_photo);
        habitEventPhoto.setImageBitmap(null);
        // habitEventPhoto.setImageBitmap(photo);

        // Reference to an image file in Cloud Storage
        // StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://habitup-d4738.appspot.com");

        if (habitEventInstance.getPhoto() != null) {
            habitEventPhoto.setImageBitmap(habitEventInstance.getPhoto());
        }
        else if (habitEventInstance.getURL() != "") {
            StorageReference imagesRef = storageRef.child(habitEventInstance.getURL());

            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            GlideApp.with(this)
                    .load(imagesRef)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(habitEventPhoto);
        }

        // OnClickListener for the edit button
        final Button editHabitEventButton = findViewById(R.id.habit_event_edit_button);
        editHabitEventButton.setOnClickListener((v) -> {
            // Open up the EditHabitEventFragment
            new EditHabitEventFragment().show(getSupportFragmentManager(), "EDIT_HABIT_EVENT");
        });

        // OnClickListener for the back button
        final Button backHabitEventButton = findViewById(R.id.habit_event_back_button);
        backHabitEventButton.setOnClickListener((v) -> {
            finish();
        });

    }

    /**
     * This handles when ok of the EditHabitEventFragment is pressed
     * @param habitEvent
     *    This is the habit event that has been modified
     */
    public void onEditOkPressed(HabitEvent habitEvent) {

        // Get passed values
        Intent intent = getIntent();
        String username = (String) intent.getStringExtra("username");
        String habitName = (String) intent.getStringExtra("habitName");

        // Firestore stuff
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String TAG = "DEBUG_LOG"; //for debugging
        CollectionReference habitsRef = db.collection(username + "/habits/habitList/" + habitName +"/habitEventList");

        // Update location
        habitsRef.document(habitEvent.getDate())
                .update("location", habitEvent.getLocation())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Document not added: " + e.toString());
                    }
                });

        // Update reflection
        habitsRef.document(habitEvent.getDate())
                .update("reflections", habitEvent.getReflection())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Document not added: " + e.toString());
                    }
                });

        // TODO: Update photograph
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

        // Store Image to Firebase File Storage
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = habitEvent.getImage();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        // Upload the image
        UploadTask uploadTask = imagesRef.putBytes(imageData);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "Document not added: " + exception.toString());
            }
        });

        // refresh current activity
        finish();
        startActivity(intent);
    }

}