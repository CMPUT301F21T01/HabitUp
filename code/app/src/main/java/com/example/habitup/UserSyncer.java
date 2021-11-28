package com.example.habitup;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class is responsible for keeping data consistent between local User (instance) and
 * the remote Firestore database. As a result, it provides an interface for dealing with
 * anything database-related in regards to the User.
 * This class is a singleton as there should only be one instance of a UserSyncer.
 * @see User
 */
public class UserSyncer {

    private static final UserSyncer instance = new UserSyncer();

    private static FirebaseFirestore db;
    private static User user = null;

    private CollectionReference userReference;
    private CollectionReference friendsReference;
    private CollectionReference requestsReference;
    private CollectionReference habitsReference;

    private final String TAG = "DEBUG_LOG";

    /**
     * Constructor set as private so other classes are unable to create instance(s).
     */
    private UserSyncer() {}

    /**
     * This method can be called to get a handle to the instance.
     * @return instance
     */
    public static UserSyncer getInstance() {
        return instance;
    }

    /**
     * An interface with a callback method which is called after data is synced. This is
     * necessary as pulling data from Firestore is asynchronous. Without a callback code will
     * continue executing and any attributes set in a .get() may remain uninitialized.
     */
    public interface FirebaseCallback {
        void onCallback();
    }

    /**
     * Pulls habit data from Firestore db and pushes to User's habits. Keeps data persistent.
     * @param firebaseCallback Implementation of onCallback() method within FirebaseCallback interface.
     * @see FirebaseCallback
     */
    public void syncHabits(FirebaseCallback firebaseCallback) {

        instance.habitsReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            // Clear habits
                            instance.user.clearHabits();

                            // Re-populate with new habits
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                // Extract data i.e. name, reason, etc.
                                String name      = (String) document.getData().get("name");
                                String startDate = (String) document.getData().get("start date");
                                String endDate   = (String) document.getData().get("end date");
                                String reason    = (String) document.getData().get("reason");
                                String freq      = (String) document.getData().get("frequency");
                                String progress  = (String) document.getData().get("progress");
                                String sType     = (String) document.getData().get("type");
                                if(freq == null) continue;

                                ArrayList<String> frequency =
                                        new ArrayList<String>(Arrays.asList(freq.split(",")));

                                Boolean type = Boolean.parseBoolean(sType);
                                int progressInt = AddHabitFragment.setProgress(startDate, endDate);


                                // Add to our user's habits
                                instance.user.addHabit(new Habit(
                                        name, startDate, endDate,
                                        frequency, reason, progressInt, type));
                            }

                            firebaseCallback.onCallback();
                        }
                    }
                });
    }

    /**
     * Initializes collection reference(s), sets and syncs User, and returns said User.
     * @param username String representation of the username (used to login).
     * @param firebase A Firebase firestore instance (reference to database)
     * @return User on success or
     *         null on failure (in the case that user is already initialized)
     */
    public User initialize(String username, FirebaseFirestore firebase){
        if(user != null) return user;            // Only initialize user once.

        // Attribute initialization
        instance.db = firebase;
        instance.userReference      = instance.db.collection(username);
        instance.friendsReference   = instance.db.collection(username + "/friends/current");
        instance.requestsReference  = instance.db.collection(username + "/friends/requests");
        instance.habitsReference    = instance.db.collection(username + "/habits/habitList");

        instance.user = new User();
        instance.user.setUsername(username);

        // Get and set name
        instance.userReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals("auth"))
                                    instance.user.setName((String)document.getData().get("name"));
                            }
                        }
                    }
                });

        // Get and set friends
        instance.friendsReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult())
                                instance.user.addFriend((String)document.getId());
                        }
                    }
                });

        // Get and set requests
        instance.requestsReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult())
                                instance.user.addRequest((String) document.getId());
                        }
                    }
                });

        return instance.user;
    }

    /**
     * Adds a habit to the user in Firestore. Also handles formatting the data appropriately
     * so that it is stored properly in the database.
     * @param habit to be added
     */
    public void addHabit(Habit habit) {

        // Format data into a hash map
        String frequencyString = String.join(",", habit.getFrequency());
        HashMap<String, String> data = new HashMap<>();
        data.put("name", habit.getTitle());
        data.put("start date", habit.getStartDate());
        data.put("end date", habit.getEndDate());
        data.put("frequency", frequencyString);
        data.put("reason", habit.getReason());
        data.put("progress", habit.getProgress().toString());

        data.put("type", habit.getType().toString());

        // Add habit to User's habitList collection in Firestore
        instance.habitsReference.document(habit.getTitle())
                .set(data)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Document not added: " + e.toString());
                    }
                });
    }

    /**
     * Deletes a habit of the user from Firestore.
     * @param habit to be deleted
     */
    public void deleteHabit(Habit habit) {
        instance.habitsReference.document(habit.getTitle())
                .delete()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Document failed to be deleted: " + e.toString());
                    }
                });
    }

    /**
     * Edits a habit of the user in Firestore.
     * @param oldName previous name of the habit (prior to editing)
     * @param habit to be edited / re-added
     */
    public void editHabit(String oldName, Habit habit) {

        // Format data into a hash map
        String frequencyString = String.join(",", habit.getFrequency());
        HashMap<String, Object> updateData = new HashMap<>();
        updateData.put("name", habit.getTitle());
        updateData.put("start date", habit.getStartDate());
        updateData.put("end date", habit.getEndDate());
        updateData.put("frequency", frequencyString);
        updateData.put("reason", habit.getReason());
        updateData.put("progress", habit.getProgress().toString());
        
        updateData.put("type", habit.getType().toString());

        if(oldName == habit.getTitle()) {
            // Update the document
            instance.habitsReference.document(oldName)
                    .update(updateData)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Document not added: " + e.toString());
                        }
                    });
        } else {
            // Otherwise the name was edited meaning we must delete and re-add the document

            // Delete
            instance.habitsReference.document(oldName)
                    .delete()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Document failed to be deleted: " + e.toString());
                        }
                    });

            // Re-create
            instance.habitsReference.document(habit.getTitle())
                    .set(updateData)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Document not added: " + e.toString());
                        }
                    });
        }

    }

    /**
     * Accepts or rejects a friend request in Firestore.
     * @param username to be accepted/rejected
     * @param decision true if user is to be accepted or
     *                 false otherwise
     */
    public void handleRequest(String username, boolean decision) {
        // Delete from requests
        instance.requestsReference.document(username)
                .delete()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Document failed to be deleted: " + e.toString());
                    }
                });

        if(decision) {
            // Add to current (friends)
            HashMap<String, Object> filler = new HashMap<>();
            instance.friendsReference.document(username)
                    .set(filler)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Document failed to be created: " + e.toString());
                        }
                    });
        }
    }

    /**
     * Sends a follower request to the username provided (in Firestore).
     * @param username to request to follow
     */
    public void sendRequest(String username) {
        CollectionReference foreignRequests = instance.db
                .collection(username + "/friends/requests");

        // Send request
        HashMap<String, Object> filler = new HashMap<>();
        foreignRequests.document(instance.user.getUsername())
                .set(filler)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Document failed to be created: " + e.toString());
                    }
                });
    }

    /**
     * Removes provided username from user's friend/follower list in Firestore.
     * @param username to be removed
     */
    public void removeFriend(String username) {
        instance.friendsReference.document(username)
                .delete()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Document failed to be deleted: " + e.toString());
                    }
                });
    }

    /**
     * Returns a handle to the instance of User class.
     * @return user Instance of user
     */
    public User getUser() {
        return instance.user;
    }

    /**
     * Returns a handle to User's habits.
     * @return habitsReference A collection reference to user's habits within the database.
     */
    public CollectionReference getHabitsRef() {
        return instance.habitsReference;
    }

    /**
     * Returns a handle to User.
     * @return userReference A collection reference to user withtin the database.
     */
    public CollectionReference getUserRef() { return instance.userReference; }

    /**
     * Returns a handle to User's friends.
     * @return friendsReference A collection reference to user's friends within the database.
     */
    public CollectionReference getFriendsRef() { return instance.friendsReference; }

    /**
     * Returns a handle to User's friend requests.
     * @return requestsReference A collection reference to user's friend requests within the database.
     */
    public CollectionReference getRequestsRef() { return instance.requestsReference; }
}
