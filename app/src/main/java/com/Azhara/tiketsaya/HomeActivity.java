package com.Azhara.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    ImageView btnPisa, imgPhoto, btnTorry, btnPagoda, btnCandi, btnSphinx, btnMonas;
    CircleView profile;
    TextView tvNama, tvBio, tvCash;
    DatabaseReference reference;
    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUsernameLocal();

        btnPisa = findViewById(R.id.btn_pisa);
        btnTorry = findViewById(R.id.btn_torri);
        btnPagoda = findViewById(R.id.btn_pagoda);
        btnCandi = findViewById(R.id.btn_candi);
        btnSphinx = findViewById(R.id.btn_sphinx);
        btnMonas = findViewById(R.id.btn_monas);
        profile = findViewById(R.id.profile);
        imgPhoto = findViewById(R.id.img_photo);
        tvNama = findViewById(R.id.tv_nama_lengkap);
        tvBio = findViewById(R.id.tv_bio);
        tvCash = findViewById(R.id.tv_money);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (username_key_new.isEmpty()){
                    Intent signIn = new Intent(HomeActivity.this, SignIn_Activity.class);
                    startActivity(signIn);
                    finish();
                }else {
                    tvNama.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                    tvBio.setText(dataSnapshot.child("bio").getValue().toString());
                    tvCash.setText("$US " + dataSnapshot.child("user_balance").getValue().toString());
                    Picasso.with(HomeActivity.this)
                            .load(dataSnapshot.child("url_photo_profile").getValue().toString()).centerCrop().fit()
                            .into(imgPhoto);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btnPisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TicketDetailActivity.class);
                intent.putExtra("Jenis_tiket", "Wisata1");
                startActivity(intent);
            }
        });
        btnTorry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TicketDetailActivity.class);
                intent.putExtra("Jenis_tiket", "Wisata2");
                startActivity(intent);
            }
        });
        btnPagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TicketDetailActivity.class);
                intent.putExtra("Jenis_tiket", "Wisata3");
                startActivity(intent);
            }
        });
        btnCandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TicketDetailActivity.class);
                intent.putExtra("Jenis_tiket", "Wisata4");
                startActivity(intent);
            }
        });
        btnSphinx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TicketDetailActivity.class);
                intent.putExtra("Jenis_tiket", "Wisata5");
                startActivity(intent);
            }
        });
        btnMonas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TicketDetailActivity.class);
                intent.putExtra("Jenis_tiket", "Wisata6");
                startActivity(intent);
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, My_Profile_Activity.class);
                startActivity(intent);
            }
        });

    }

    private void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
