package com.example.habitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewProfile extends AppCompatActivity {


    ListView viewingHabitsList;
    ArrayAdapter<Habit> viewingHabitAdapter;
    ArrayList<String> habitsDataList;
    TextView initial;
    public static UserSyncer viewingSyncer;
    public static User viewingUser;

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        viewingHabitsList = findViewById(R.id.habits_list);
        initial = findViewById(R.id.userInitial);

        String name = getIntent().getStringExtra("name_of_user");
        String username = getIntent().getStringExtra("username_searched");
        char textForInsideBubble = String.valueOf(name).charAt(0);
        initial.setText(String.valueOf(textForInsideBubble));


        CollectionReference collRefSearchedUserHabits = db.collection(username).document("habits").collection("habitList");

        viewingUser.setUsername(username);
        viewingUser.setName(name);

        collRefSearchedUserHabits.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            viewingUser.clearHabits();
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                // Extract data i.e. name, reason, etc.
                                String name      = (String) document.getData().get("name");
                                String startDate = (String) document.getData().get("start date");
                                String endDate   = (String) document.getData().get("end date");
                                String reason    = (String) document.getData().get("reason");
                                String freq      = (String) document.getData().get("frequency");
                                String progress  = (String) document.getData().get("progress");
                                String sType = (String) document.getData().get("type");
                                if(freq == null) continue;

                                ArrayList<String> frequency =
                                        new ArrayList<String>(Arrays.asList(freq.split(",")));

                                int progressInt = Integer.parseInt(progress);
                                Boolean type = Boolean.parseBoolean(sType);

                                // Add to our user's habits

                                if (type = true)
                                {
                                    viewingUser.addHabit(new Habit(
                                        name, startDate, endDate,
                                        frequency, reason, progressInt, type));
                                }


                            }
                        }
                    }
                });



        viewingHabitAdapter = new HabitList(this, viewingUser.getHabits());
        viewingHabitsList.setAdapter(viewingHabitAdapter);







    }
}