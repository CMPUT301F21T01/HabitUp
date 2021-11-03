package com.example.habitup;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;

/**
 * This class is responsible for keeping data consistent between local User (class) and
 * the remote Firestore database.
 */
public class UserSyncer {

    private static final UserSyncer instance = new UserSyncer();

    private FirebaseFirestore db;
    private User user = null;

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
     * Initializes collection reference(s), sets and syncs User, and returns said User.
     * @param username String representation of the username (used to login).
     * @return User on success or
     *         null on failure (user already initialized)
     */
    public User initialize(String username, FirebaseFirestore firebase){
        if(user != null) return null;            // Only initialize user once.

        // Attribute initialization
        instance.db = firebase;
        instance.userReference      = instance.db.collection(username);
        instance.friendsReference   = instance.db.collection(username + "/friends/current");
        instance.requestsReference  = instance.db.collection(username + "/friends/requests");
        instance.habitsReference    = instance.db.collection(username + "/habits/habitList");

        String[] name = new String[1];
        ArrayList<String> friends  = new ArrayList<>();
        ArrayList<String> requests = new ArrayList<>();

        // Get name
        instance.userReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals("auth")) {
                                    name[0] = (String)document.getData().get("name");
                                }
                            }
                        }
                    }
                });

        // Get friends
        instance.friendsReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                friends.add((String) document.getData().get("username"));
                            }
                        }
                    }
                });

        // Get requests
        instance.requestsReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                requests.add((String) document.getData().get("username"));
                            }
                        }
                    }
                });

        // Create user and return
        instance.user = new User(username, name[0], friends, requests);
        return instance.user;
    }


}
