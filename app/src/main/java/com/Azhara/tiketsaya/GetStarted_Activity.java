package com.Azhara.tiketsaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStarted_Activity extends AppCompatActivity {

    Button signIn, register;
    ImageView img;
    TextView text;
    Animation from_top, from_bottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        // Mentarget Id pada Button Sign in
        signIn = findViewById(R.id.btn_sign_in);

        // Target Id pada button Sign in
        register = findViewById(R.id.btn_new_account);

        img = findViewById(R.id.imageView2);
        text= findViewById(R.id.textView);

        from_top = AnimationUtils.loadAnimation(this, R.anim.top_bottom);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_top);

        img.startAnimation(from_top);
        text.startAnimation(from_top);

        signIn.startAnimation(from_bottom);
        register.startAnimation(from_bottom);

        // Melakukan Fungsi Klik dengan setOnClickListener
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent adalah Fungsi untuk menuju activity lain
                Intent SignIn = new Intent(GetStarted_Activity.this, SignIn_Activity.class);
                // Memulai Intent
                startActivity(SignIn);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetStarted_Activity.this, Register_Activity.class);
                startActivity(intent);
            }
        });
    }
}
