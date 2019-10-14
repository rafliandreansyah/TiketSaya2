package com.Azhara.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Animation animate1,animate2;
    ImageView logo;
    TextView logo_sub;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getUsernameLocal();

        // Load Anim
        animate1 = AnimationUtils.loadAnimation(this, R.anim.top_bottom_scale);
        animate2 = AnimationUtils.loadAnimation(this,R.anim.bottom_top_scale);

        // Target Xml
        logo = findViewById(R.id.Logo);
        logo_sub = findViewById(R.id.Logo_subtitle);

        // Membuat animasi pada xml yang ditarget
        logo.startAnimation(animate1);
        logo_sub.startAnimation(animate2);




    }
    private void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");

        if (username_key_new.isEmpty()){
            // Handler untuk setting timer
            Handler splash = new Handler();
            splash.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Menuju Activity lain
                    Intent intent = new Intent(SplashActivity.this, GetStarted_Activity.class);
                    // Memulai menuju activity lain
                    startActivity(intent);
                    // Finish berfungsi untuk mengakhiri activity
                    finish();
                }
            },2000); // mengatur waktu 2000 ms = 2 detik
        }else {
            // Handler untuk setting timer
            Handler splash = new Handler();
            splash.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Menuju Activity lain
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    // Memulai menuju activity lain
                    startActivity(intent);
                    // Finish berfungsi untuk mengakhiri activity
                    finish();
                }
            },2000); // mengatur waktu 2000 ms = 2 detik
        }
    }
}
