package com.fca.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.fca.agenda.utils.ApplicationConstants;

/**
 * Created by fabiano.alvarenga on 10/04/18.
 */

public class SplashScreenActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(this, ApplicationConstants.SplashActivity.WAIT_START_MAIN);

    }

    @Override
    public void run() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
