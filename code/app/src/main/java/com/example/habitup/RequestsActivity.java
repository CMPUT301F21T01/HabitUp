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
        requestList = findViewById(R.id.requests_list);

        String[] requests = {"static_harrys", "static_niall_h72", "static_bey_kc"};

        requestsDataList = new ArrayList<>();
        requestsDataList.addAll(Arrays.asList(requests));

        requestsAdapter = new ArrayAdapter<>(this, R.layout.requests_content, requestsDataList);

        requestList.setAdapter(requestsAdapter);


        //return to main activity
    /**
     * uses the back button to return to the main acitivty showing the user's list of habits
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
