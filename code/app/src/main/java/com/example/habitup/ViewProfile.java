package com.example.habitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

/**
 * this actvity allows the user to view the profile of a friend via search
 */
public class ViewProfile extends AppCompatActivity {


    ListView viewingHabitsListView;
    ArrayAdapter<Habit> viewingHabitAdapter;
    ArrayAdapter<String> viewingHabitNameListAdapter;
    ArrayList<String> viewingHabitNameList = new ArrayList<String>();
    TextView initial;
    User viewingUser = new User();
    String vName = null;
    String vUsername = null;
    String pCurrentUser = null;
    String pCurrentUserUsername = null;


    FirebaseFirestore db;


    @Override
    /**
     * This brings up the proper displays from the XML to be shown on the screen
     * @param savedInstanceState from the switching of the activities (from the search activity to here upon the name of the person being long clicked)
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        viewingHabitsListView = findViewById(R.id.profile_view_habits_list);
        initial = findViewById(R.id.userInitial);
        //unpacks the intent from the search activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            vName = extras.getString("name_of_user");
            vUsername = extras.getString("username_searched");
            pCurrentUser = extras.getString("name_of_main_user");
            pCurrentUserUsername = extras.getString("username_of_current_user");

        }
        char textForInsideBubble = String.valueOf(vName).charAt(0);
        initial.setText(String.valueOf(textForInsideBubble));

        db = FirebaseFirestore.getInstance();
        CollectionReference collRefSearchedUserHabits = db.collection(vUsername).document("habits").collection("habitList");

        viewingUser.setUsername(vUsername);
        viewingUser.setName(vName);


        //unpacks the searched user's habits from its collection in the database so the habits can be displayed
        collRefSearchedUserHabits.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            viewingUser.clearHabits();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Extract data i.e. name, reason, etc.
                                String name = (String) document.getData().get("name");
                                String startDate = (String) document.getData().get("start date");
                                String endDate = (String) document.getData().get("end date");
                                String reason = (String) document.getData().get("reason");
                                String freq = (String) document.getData().get("frequency");
                                String progress = (String) document.getData().get("progress");
                                String position = (String) document.getData().get("position");
                                String sType = (String) document.getData().get("type");
                                if (freq == null) continue;

                                ArrayList<String> frequency =
                                        new ArrayList<String>(Arrays.asList(freq.split(",")));
                                if (position == null) {
                                    position = "0";
                                }
                                int progressInt = Integer.parseInt(progress);
                                int positionInt = Integer.parseInt(position);
                                Boolean.parseBoolean(sType);
                                Boolean type;

                                // Add to our user's habits

                                if (type = true) {
                                    viewingUser.addHabit(new Habit(
                                            name, startDate, endDate,
                                            frequency, reason, progressInt, true, positionInt));
                                    viewingHabitNameList.add(name);
                                }
                                System.out.println(viewingHabitNameList);



                            }
                        }
                    }
                });

        System.out.println(viewingHabitNameList);


        viewingHabitNameListAdapter = new ArrayAdapter(this, R.layout.view_profile_content, viewingHabitNameList);
        viewingHabitsListView.setAdapter(viewingHabitNameListAdapter);
        viewingHabitsListView.setVisibility(View.VISIBLE);
        //viewingHabitAdapter = new HabitList(this, viewingUser.getHabits());
        //viewingHabitsList.setAdapter(viewingHabitAdapter);
    }
}
