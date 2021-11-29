package com.example.habitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This activity allows the user to view their friend requests
 */
public class RequestsActivity extends AppCompatActivity {

    ListView requestList;
    ArrayAdapter<String> requestsAdapter;
    ArrayList<String> requestsDataList;
    Button backBtn;
    Button approveBtn;
    Button deleteBtn;

    /**
     * This brings up the proper displays from the XML to be shown on the screen
     * @param savedInstanceState from the switching of the activities (from the profile activity to here upon the profile button being clicked)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        backBtn = findViewById(R.id.go_back_btn);
        approveBtn = findViewById(R.id.approve_btn);
        deleteBtn = findViewById(R.id.delete_btn);
        requestList = findViewById(R.id.requests_list);

        // Unpack intent
        ArrayList<String> requestsDataList = (ArrayList<String>) getIntent().getSerializableExtra("requests");

        requestsAdapter = new ArrayAdapter<>(this, R.layout.requests_content, requestsDataList);

        requestList.setAdapter(requestsAdapter);

        /**
         * uses the back button to go to the main activity, habit list screen
         * @param view
         */
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });




    }
}

