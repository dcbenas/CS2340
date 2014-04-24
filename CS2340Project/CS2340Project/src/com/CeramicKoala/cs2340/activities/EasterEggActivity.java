package com.CeramicKoala.cs2340.activities;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.engsoc.dodge.DGame;

import android.os.Bundle;

public class EasterEggActivity extends AndroidApplication {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        cfg.useAccelerometer = true;
        cfg.useCompass = true;
        
        initialize(new DGame(), cfg);
    }
}
