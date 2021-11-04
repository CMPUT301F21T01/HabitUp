package com.example.habitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity{

    ListView friendsList;
    ArrayAdapter<String> friendsAdapter;
    ArrayList<String> friendsDataList;
    Button backBtn;
    Button viewRequestsBtn;
    TextView initial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        backBtn = findViewById(R.id.switch_activity_btn);
        viewRequestsBtn = findViewById(R.id.view_requests_button);
        friendsList = findViewById(R.id.friend_list);
        initial = findViewById(R.id.userInitial);

        // Unpack intent
        ArrayList<String> friendsDataList = (ArrayList<String>) getIntent().getSerializableExtra("friends");
        ArrayList<String> requestsDataList = (ArrayList<String>) getIntent().getSerializableExtra("requests");
        String name = getIntent().getStringExtra("name");

        // Set user initial
        //initial.setText(name.charAt(0).toString());

        friendsAdapter = new ArrayAdapter<>(this, R.layout.profile_content, friendsDataList);
        friendsList.setAdapter(friendsAdapter);

        viewRequestsBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent requestsSwitchIntent = new Intent(v.getContext(), RequestsActivity.class);
                requestsSwitchIntent.putExtra("requests", requestsDataList);
                startActivity(requestsSwitchIntent);
            }
        });


        // Return to main activity
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}