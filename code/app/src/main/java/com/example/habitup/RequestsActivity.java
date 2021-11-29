package com.example.habitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This activity allows the user to view their friend requests
 */
public class RequestsActivity extends AppCompatActivity {

    ListView requestList;
    //ArrayAdapter<String> requestsAdapter;
    ArrayList<String> requestsDataList;
    Button backBtn;
    Button approveBtn;
    Button deleteBtn;
    RequestsCustomAdapter requestsAdapter;
    public static UserSyncer syncer;
    public static User mainUser;

    FirebaseFirestore db;
    /**
     * This brings up the proper displays from the XML to be shown on the screen
     * @param savedInstanceState from the switching of the activities (from the profile activity to here upon the profile button being clicked)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        approveBtn = findViewById(R.id.approve_btn);
        deleteBtn = findViewById(R.id.decline_btn);
        requestList = findViewById(R.id.requests_list);
        db = FirebaseFirestore.getInstance();
        syncer   = UserSyncer.getInstance();


        // Unpack intents
        ArrayList<String> requestsDataList = (ArrayList<String>) getIntent().getSerializableExtra("requests");
        String usernameOfCurrentUser = getIntent().getStringExtra("usernameOfCurrentUser");
        String nameOfCurrentUser = getIntent().getStringExtra("nameOfCurrentUser");

        mainUser = syncer.initialize(usernameOfCurrentUser, db);
        requestsAdapter = new RequestsCustomAdapter(requestsDataList, this);

        requestList.setAdapter(requestsAdapter);




    }
}

