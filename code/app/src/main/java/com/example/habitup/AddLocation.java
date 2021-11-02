
// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.example.habitup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
// [START maps_marker_on_map_ready]
public class AddLocation extends AppCompatActivity implements OnMapReadyCallback {

    // https://developer.android.com/training/location/retrieve-current
    private FusedLocationProviderClient fusedLocationClient;

    private LatLng location = new LatLng(53.5232, -113.5263);
    private String selected_location = "53.5232, -113.5263";

    GoogleMap myMap;
    HabitEventInstance habitEventInstance;

    // [START_EXCLUDE]
    // [START maps_marker_get_map_async]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_add_location);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        habitEventInstance = HabitEventInstance.getInstance();
        habitEventInstance.setLocation(selected_location);

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
    // [END maps_marker_get_map_async]
    // [END_EXCLUDE]

    // [START_EXCLUDE silent]
    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    // [END_EXCLUDE]
    // [START maps_marker_on_map_ready_add_marker]
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // [START_EXCLUDE silent]
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        // [END_EXCLUDE]
        myMap = googleMap;

        Marker marker = googleMap.addMarker(
                new MarkerOptions()
                        .position(location)
                        .title("Marker")
                        .draggable(true)
        );
        // [START_EXCLUDE silent]
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        float size = googleMap.getMaxZoomLevel();
        googleMap.setMaxZoomPreference(size);
        // [END_EXCLUDE]

        TextView locationText = (TextView) this.findViewById(R.id.location_text);
        double lat = marker.getPosition().latitude;
        double lng = marker.getPosition().longitude;

        DecimalFormat df = new DecimalFormat("#.0000");
        selected_location = df.format(lat) + ", " + df.format(lng);
        locationText.setText(selected_location);

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener()
        {
            @Override
            public void onMarkerDragStart(Marker marker)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void onMarkerDragEnd(Marker marker)
            {
                // TODO Auto-generated method stub
                double lat = marker.getPosition().latitude;
                double lng = marker.getPosition().longitude;

                location = new LatLng(lat, lng);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

                DecimalFormat df = new DecimalFormat("#.0000");
                selected_location = df.format(lat) + ", " + df.format(lng);
                locationText.setText(selected_location);
            }

            @Override
            public void onMarkerDrag(Marker marker)
            {
                // TODO Auto-generated method stub
            }
        });


    }
    // [END maps_marker_on_map_ready_add_marker]

    // Zoom in and zoom out buttons
    // https://www.youtube.com/watch?v=B4OCSRBFjkM
    public void onZoom(View view) {
        if (view.getId() == R.id.zoom_in) {
            myMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if (view.getId() == R.id.zoom_out) {
            myMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

}
// [END maps_marker_on_map_ready]