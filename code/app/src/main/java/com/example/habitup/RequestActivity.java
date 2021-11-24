package com.example.habitup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

//this activity includes the idea that the

public class RequestActivity extends AppCompatActivity {
    Button sendRequestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        sendRequestBtn = findViewById(R.id.send_requests_button);

        //unpack the intent from search activtity
        String name = getIntent().getStringExtra("name");
        User.addRequest(name);
        //then go back to the last activity (search or profile)


    }
}