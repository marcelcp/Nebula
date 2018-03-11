package com.note.nebula.nebula.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.note.nebula.nebula.R;
import com.note.nebula.nebula.apps_security.Authenticator;
import com.note.nebula.nebula.apps_security.SecureNote;


public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splashscreen);

        Authenticator authenticator = new Authenticator(this.getApplicationContext());

        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = null;
                if(Authenticator.hasUser()) {
                    mainIntent = new Intent(SplashScreen.this, SignIn.class);
                } else {
                    mainIntent = new Intent(SplashScreen.this, SignUp.class);
                }
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
