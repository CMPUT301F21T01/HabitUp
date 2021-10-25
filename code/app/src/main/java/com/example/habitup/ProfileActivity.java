package com.example.habitup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity{

    ListView friendsList;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        friendsList = findViewById(R.id.friend_list);
        ArrayList friendsListDataList = new ArrayList<>();
        ArrayAdapter friendAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendsListDataList);
        friendsList.setAdapter(friendAdapter);

        backBtn = findViewById(R.id.switch_activity_btn);

        // Return to main activity
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}