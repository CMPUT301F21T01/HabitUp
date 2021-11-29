package com.example.habitup;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

/**
 * MyAppGlideModule class by Vivian
 * This is used to handle the GlideApp for storing image to fire store
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    /**
     * This is called when a fragment is first attached to its context.
     * @param context
     * the context of current activity
     * @param glide
     * the Glide currently in use
     * @param registry
     */
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        // Register FirebaseImageLoader to handle StorageReference
        registry.append(StorageReference.class, InputStream.class,
                new FirebaseImageLoader.Factory());
    }

}