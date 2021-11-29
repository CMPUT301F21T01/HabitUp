package com.example.habitup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

/**
 * AddLocation class by Vivian
 * This is an activity that displays a Google map with a marker (pin) to indicate a particular location.
 * Reference: https://developers.google.com/maps/documentation/android-sdk/map-with-marker
 * Issues: None
 */
// [START maps_marker_on_map_ready]
public class AddLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLng location = new LatLng(53.5232, -113.5263);
    private String selected_location = "53.5232, -113.5263";

    GoogleMap myMap;
    HabitEventInstance habitEventInstance;

    /**
     * This initializes the creation of the AddLocation activity
     * @param savedInstanceState
     * bundle that stores & passes data among activities
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_add_location);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // If the location is not null, set preview image to current image
        habitEventInstance = HabitEventInstance.getInstance();
        if (habitEventInstance.getLocation() != "") {
            selected_location = habitEventInstance.getLocation();
        }

        Button confirmButton = (Button) this.findViewById(R.id.confirm_location_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Location: ", selected_location);

                // Refer to the habit event instance
                habitEventInstance.setLocation(selected_location);

                finish();
            }
        });
    }

    /**
     * This manipulates the map when it's available.
     * This is where we can add markers or lines, add listeners or move the camera.
     * @param googleMap
     * the googleMap instance
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker for University of Alberta
        myMap = googleMap;

        Marker marker = googleMap.addMarker(
                new MarkerOptions()
                        .position(location)
                        .title("Marker")
                        .draggable(true)
        );

        // Adjust zoom level
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        float size = googleMap.getMaxZoomLevel();
        googleMap.setMaxZoomPreference(size);

        // Set the text box to the latitude and longitude
        TextView locationText = (TextView) this.findViewById(R.id.location_text);
        double lat = marker.getPosition().latitude;
        double lng = marker.getPosition().longitude;

        DecimalFormat df = new DecimalFormat("#.0000");
        selected_location = df.format(lat) + ", " + df.format(lng);
        locationText.setText(selected_location);

        // Set up the drag marker listener
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener()
        {
            @Override
            public void onMarkerDragStart(Marker marker) { }

            @Override
            public void onMarkerDragEnd(Marker marker)
            {
                double lat = marker.getPosition().latitude;
                double lng = marker.getPosition().longitude;

                location = new LatLng(lat, lng);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

                DecimalFormat df = new DecimalFormat("#.0000");
                selected_location = df.format(lat) + ", " + df.format(lng);
                locationText.setText(selected_location);
            }

            @Override
            public void onMarkerDrag(Marker marker) { }
        });


    }

    /**
     * This handles the Zoom In and Zoom Out buttons
     * Reference: https://www.youtube.com/watch?v=B4OCSRBFjkM
     * @param view
     * view of the googleMap activity
     */
    public void onZoom(View view) {
        if (view.getId() == R.id.zoom_in) {
            myMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if (view.getId() == R.id.zoom_out) {
            myMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

}