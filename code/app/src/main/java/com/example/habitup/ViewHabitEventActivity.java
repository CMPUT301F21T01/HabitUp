package com.example.habitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewHabitEventActivity extends AppCompatActivity implements EditHabitEventFragment.OnFragmentInterationListener {

    private HabitEventInstance habitEventInstance = HabitEventInstance.getInstance();
    private String date = "Date: ";
    private String location = "Location: ";
    private String reflection = "Comment: ";
    private Bitmap photo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit_event);

        HabitEventInstance.getInstance();
        habitEventInstance = HabitEventInstance.getInstance();

        date += habitEventInstance.getDate();
        location += habitEventInstance.getLocation();
        reflection += habitEventInstance.getReflection();
        photo = habitEventInstance.getPhoto();

        TextView habitEventDate = findViewById(R.id.habit_event_date);
        habitEventDate.setText(date);

        TextView habitEventLocation = findViewById(R.id.habit_event_location);
        habitEventLocation.setText(location);

        TextView habitEventReflection = findViewById(R.id.habit_event_reflection);
        habitEventReflection.setText(reflection);

        ImageView habitEventPhoto = findViewById(R.id.habit_event_photo);
        habitEventPhoto.setImageBitmap(photo);

        final Button editHabitEventButton = findViewById(R.id.habit_event_edit_button);
        editHabitEventButton.setOnClickListener((v) -> {
            new EditHabitEventFragment().show(getSupportFragmentManager(), "EDIT_HABIT_EVENT");
        });

        final Button backHabitEventButton = findViewById(R.id.habit_event_back_button);
        backHabitEventButton.setOnClickListener((v) -> {
            finish();
        });

    }

    public void onEditOkPressed(HabitEvent habitEvent) {
        Bundle extras = getIntent().getExtras();
        int pos = extras.getInt("pos");

        // UPDATE TO DATABASE HERE!
        // habitEvent is the modified habit event object
        // pos is the position of edited habit event inside the list


        // refresh current activity
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}