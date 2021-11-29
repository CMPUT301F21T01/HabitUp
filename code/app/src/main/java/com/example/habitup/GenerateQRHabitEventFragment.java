package com.example.habitup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


/**
 * GenerateQRHabitEventFragment class
 * This is a fragment that generates a QR for a habitEvent
 */
public class GenerateQRHabitEventFragment extends DialogFragment {

    //stuff for the QR code
    private ImageView QRImage;
    Bitmap bitmap;
    QRGEncoder QREncoder;

    private GenerateQRHabitEventFragment.OnFragmentInteractionListener listner;

    private HabitEventInstance habitEventInstance = HabitEventInstance.getInstance();
    private String location = "";
    private String reflection = "";

    /**
     * This is the fragment interaction listener
     */
    public interface OnFragmentInteractionListener {
        //nothing is needed here for this fragment
    }

    /**
     * This is called when a fragment is first attached to its context.
     * @param context
     * context is needed to attach the fragment to the previous screen
     */
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof GenerateQRHabitEventFragment.OnFragmentInteractionListener) {
            listner = (GenerateQRHabitEventFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }

    }

    /**
     * This initializes the creation of the GenerateQR fragment
     * @param savedInstanceState bundle that stores & passes data among activities
     */
    @NonNull
    @Override
    public Dialog onCreateDialog (@Nullable Bundle savedInstanceState) {

        // Create the habit event instance
        HabitEventInstance.getInstance();
        habitEventInstance = HabitEventInstance.getInstance();

        //get view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_generate_q_r_habit_event, null);
        //get reference to image where QR will be
        QRImage = view.findViewById(R.id.QR_code_image);
        //get location and reflection from the HabitEvent
        location = habitEventInstance.getLocation();
        reflection = habitEventInstance.getReflection();

        //make the QRCode
        // window manager service initialization to get dimensions for qr code
        WindowManager WManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        // get default display
        Display DDisplay = WManager.getDefaultDisplay();
        //creating a point that will be our QR code
        Point p = new Point();
        DDisplay.getSize(p);
        int width = p.x;
        int height = p.y;
        // getting dimensions of QR code from the width and height, by getting smaller of 2(it has to be square)
        int dimensions = Math.min(width, height);
        //encoding the QR code with the dimensions
        Log.e("Tag", "location: "+location+" Reflection: "+reflection);
        QREncoder = new QRGEncoder(location+reflection, null, QRGContents.Type.TEXT, dimensions);
        try {
            // converting to bitmap
            bitmap = QREncoder.encodeAsBitmap();
            //set image to bitmap
            QRImage.setImageBitmap(bitmap);
        } catch (WriterException exception) {
            Log.e("Error", exception.toString());
        }

        // Construct the QRHabitEvent fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view)
                .setTitle("Share Habit Event")
                .setNegativeButton("Back", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //not needed, but left for future possibilities
                    }
                }).create();
    }
}