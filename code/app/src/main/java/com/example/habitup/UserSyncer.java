package com.example.habitup;



import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

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
        instance.user.clearHabits();

        instance.habitsReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                // Extract data i.e. name, reason, etc.
                                String name      = (String) document.getData().get("name");
                                String startDate = (String) document.getData().get("start date");
                                String endDate   = (String) document.getData().get("end date");
                                String reason    = (String) document.getData().get("reason");
                                String freq      = (String) document.getData().get("frequency");
                                String progress  = (String) document.getData().get("progress");
                                if(freq == null) continue;

                                ArrayList<String> frequency =
                                        new ArrayList<String>(Arrays.asList(freq.split(",")));

                                int progressInt = Integer.parseInt(progress);

                                // Add to our user's habits
                                instance.user.addHabit(new Habit(
                                        name, startDate, endDate,
                                        frequency, reason, progressInt));
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
        if(user != null) return null;            // Only initialize user once.

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
                                instance.user.addFriend((String) document.getData().get("username"));
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
                                instance.user.addRequest((String) document.getData().get("username"));
                        }
                    }
                });

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
