package com.CeramicKoala.cs2340.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import com.CeramicKoala.cs2340.R;
 
public class SplashScreen extends Activity {
 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datsplashscreen);
        MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.ohmy);
        mp.start();
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer.
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
 
}
