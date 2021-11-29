package com.example.habitup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.NonNull;

/**
 * AddPhotograph class by Vivian
 * This is an activity that handles adding photograph from camera or gallery, and display ths image on screen
 * Reference: https://newbedev.com/java-android-take-photo-and-display-in-imageview-code-example, https://www.py4u.net/discuss/624180
 */
public class AddPhotographActivity extends Activity
{
    private static final int CAMERA_REQUEST = 1888;
    public static final int GET_FROM_GALLERY = 3;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private Bitmap selectedPhoto;
    private HabitEventInstance habitEventInstance;

    /**
     * This initializes the creation of the AddPhotograph activity
     * It as well sets the OnClickListeners for all buttons within the activity
     * @param savedInstanceState
     * bundle that stores & passes data among activities
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photograph);

        this.imageView = (ImageView)this.findViewById(R.id.habit_event_photo);

        // If the image is not null, set preview image to current image
        habitEventInstance = HabitEventInstance.getInstance();
        if (habitEventInstance.getPhoto() != null) {
            selectedPhoto = habitEventInstance.getPhoto();
            this.imageView.setImageBitmap(selectedPhoto);
        }

        // OnClickListener for camera button
        Button photoButton = (Button) this.findViewById(R.id.camera_button);
        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        // OnClickListener for gallery button
        Button galleryButton = (Button) this.findViewById(R.id.album_button);
        galleryButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        Button addButton = (Button) this.findViewById(R.id.habit_event_back_button);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Refer to the habit event instance
                habitEventInstance.setPhoto(selectedPhoto);

                finish();
            }
        });
    }

    /**
     * This requests camera permission from the user.
     * If permission is granted, open the camera.
     * @param requestCode
     * request code
     * @param permissions
     * permission text
     * @param grantResults
     * result of requesting for permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * This handles the activity result from the camera or gallery activity
     * If an OK result is returned, get the selected image and display it on screen
     * @param requestCode
     * request code for camera permission
     * @param resultCode
     * request code for gallery permission
     * @param data
     * the current intent/activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            selectedPhoto = photo;
            imageView.setImageBitmap(photo);
        }

        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);

            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                selectedPhoto = bitmap;
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}