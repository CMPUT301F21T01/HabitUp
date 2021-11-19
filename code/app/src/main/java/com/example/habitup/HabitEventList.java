package com.example.habitup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * HabitEventList communicates with HabitEvent, HabitEventContent, and HabitEventActivity in order to display a list of
 * HabitEvents for a specific Habit.
 * Known Issues: None so far!
 */
class HabitEventList extends ArrayAdapter<HabitEvent> {
    private ArrayList<HabitEvent> habitEvents;
    private Context context;


    public HabitEventList(Context context, ArrayList<HabitEvent> habitEvents){
        super(context,0, habitEvents);
        this.habitEvents = habitEvents;
        this.context = context;
    }

    /**
     * the getView loads information into the display view content from each HabitEvent
     * and sets a listener for the deleteButton
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.habiteventcontent, parent,false);
        }

        HabitEvent habitEvent = habitEvents.get(position);

        TextView habitDate = view.findViewById(R.id.textViewDate);
        TextView habitLocation = view.findViewById(R.id.textViewLocation);
        TextView habitReflection = view.findViewById(R.id.textViewReflection);

        habitDate.setText(habitEvent.getDate());
        habitLocation.setText(habitEvent.getLocation());
        habitReflection.setText(habitEvent.getReflection());

        ImageView habitPhoto = view.findViewById(R.id.imageViewPhoto);

        // Reference to an image file in Cloud Storage
        // StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://habitup-d4738.appspot.com");
        if (habitEvent.getImage() != null) {
            habitPhoto.setImageBitmap(habitEvent.getImage());
        }
        else if (habitEvent.getURL() != "") {
            StorageReference imagesRef = storageRef.child(habitEvent.getURL());

            // Download directly from StorageReference using Glide
            // (See MyAppGlideModule for Loader registration)
            GlideApp.with(context)
                    .load(imagesRef)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(habitPhoto);
        }

        //delete HabitEvent stuff here
        //button action
        Button deleteButton = (Button) view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this assumes the list is 'alive' on the HabitEventActivity
                ((HabitEventActivity)context).onDeletePressed(position);
            }
        });

        // edit HabitEvent stuff here
        ImageButton editButton = (ImageButton) view.findViewById(R.id.edit_habit_event_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this assumes the list is 'alive' on the HabitEventActivity
                ((HabitEventActivity)context).onEditPressed(position);
            }
        });

        return view;

    }


}