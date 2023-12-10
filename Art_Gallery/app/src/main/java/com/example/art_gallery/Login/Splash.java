package com.example.art_gallery.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.art_gallery.MainActivity;
import com.example.art_gallery.R;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        fAuth = FirebaseAuth.getInstance();

        fAuth = FirebaseAuth.getInstance();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (fAuth.getCurrentUser() != null) {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(Splash.this, Login_Page.class));
                }
            }
        } , 500);

    }
}