package com.duffin22.mdmessage;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by matthewtduffin on 14/08/16.
 */
public class FirebaseContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
