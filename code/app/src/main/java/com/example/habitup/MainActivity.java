package com.example.habitup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Declaration of variables
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;

    Button addHabitButton;
    EditText addNameText;
    EditText addFrequencyText;
    FirebaseFirestore db;

    final String TAG = "TEST_LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize variables
        habitList        = findViewById(R.id.habit_list);
        addHabitButton   = findViewById(R.id.add_habit_btn);
        addNameText      = findViewById(R.id.add_name_field);
        addFrequencyText = findViewById(R.id.add_freq_field);

        habitDataList = new ArrayList<>();
        habitAdapter = new CustomList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionRef = db.collection("John");

        // Add habit button listener
        addHabitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String habitName   = addNameText.getText().toString();
                final String habitFrequency = addFrequencyText.getText().toString();
                /*  ^^ Could fail to parse, implement try catch exception clause later ^^  */

                HashMap<String, String> data = new HashMap<>();
                data.put("Name", habitName);
                data.put("Frequency", habitFrequency);

                db.collection("John")
                        .document(String.valueOf(habitDataList.size()))
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Document added.");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Document not added: " + e.toString());
                            }
                        });

                addNameText.setText("");
                addFrequencyText.setText("");
            }
        });

        // Collection event listener
        collectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();

                for(QueryDocumentSnapshot doc : documentSnapshots) {
                    String name = (String)doc.getData().get("Name");
                    String freq = (String)doc.getData().get("Frequency");

                    // Add each habit from database
                    habitDataList.add(new Habit(name, freq));
                }

                habitAdapter.notifyDataSetChanged();
            }
        });

        // Delete habit (on click) listener
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Delete habit?");
                final String selectedName = habitDataList.get(pos).getName();
                alert.setMessage("Do you want to delete '" + selectedName + "'?");
                alert.setNegativeButton("Cancel", null);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Delete from our database
                        collectionRef.document(String.valueOf(pos))
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "Document deleted.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Document failed to be deleted: " + e.toString());
                                    }
                                });

                        // Notify adapter of change
                        habitAdapter.notifyDataSetChanged();
                    }
                });

                alert.show();
            }
        });

    }

}
