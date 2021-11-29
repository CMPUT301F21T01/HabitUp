package com.example.habitup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;


/**
 * This activity is prompted by the Search Actvity if the username searched for is NOT yet a friend of the main user so they're able to send requests
 */

public class RequestToFollowActivity extends AppCompatActivity {
    Button sendRequestBtn;
    TextView nameText;
    String nameToRequest;
    String userNameToRequest;
    String currentUser;
    String currentUsersUsername;
    FirebaseFirestore db;
    String g_TAG = "TEST_LOG";

    @Override
    /**
     * sets the proper XML activities to so the requests activity is able to occur
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_to_follow);
        sendRequestBtn = findViewById(R.id.send_requests_button);
        db = FirebaseFirestore.getInstance();

        //unpack the intent from search activtity
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            nameToRequest = extras.getString("name_of_user");
            userNameToRequest = extras.getString("username_searched");
            currentUser = extras.getString("name_of_main_user");
            currentUsersUsername = extras.getString("username_of_current_user");

        }
        //dynamically changes the initial in the profile picture bubble based on that of the appropriate user
        nameText = (TextView) findViewById(R.id.userInitial);
        char textForInsideBubble = String.valueOf(nameToRequest).charAt(0);
        nameText.setText(String.valueOf(textForInsideBubble));

        CollectionReference collUserBeingRequested = db.collection(userNameToRequest);

        //when a request is sent, the user we are is added to the requests list for the searched user in the database
        sendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> data = new HashMap<>();
                data.put("username", currentUsersUsername);
                data.put("name", currentUser);
                collUserBeingRequested.document("friends").collection("requests").document(currentUsersUsername).set(data);
            }
        });




    }
}