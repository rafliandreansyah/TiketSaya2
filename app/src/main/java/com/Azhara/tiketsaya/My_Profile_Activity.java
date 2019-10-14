package com.Azhara.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class My_Profile_Activity extends AppCompatActivity implements View.OnClickListener {

    Button edit_profile, btnSignOut;
    TextView tvNama, tvBio;
    ImageView imgProfile, btnHome;
    RecyclerView rvTiket;

    DatabaseReference reference, reference2;
    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";


    ArrayList<MyTicket> list = new ArrayList<>();
    TiketAdapter tiketAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getUsernameLocal();

        btnHome = findViewById(R.id.btn_to_home);
        imgProfile = findViewById(R.id.img_profile_picture);
        edit_profile = findViewById(R.id.btn_editProfile);
        tvNama = findViewById(R.id.tv_nama);
        tvBio = findViewById(R.id.tv_bio);
        rvTiket = findViewById(R.id.rv_tiket);
        btnSignOut = findViewById(R.id.btn_sign_out);


        rvTiket.setLayoutManager(new LinearLayoutManager(this));
        rvTiket.setHasFixedSize(true);

        btnHome.setOnClickListener(this);
        edit_profile.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);


        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(My_Profile_Activity.this)
                        .load(dataSnapshot.child("url_photo_profile").getValue().toString()).centerCrop().fit()
                        .into(imgProfile);
                tvNama.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                tvBio.setText(dataSnapshot.child("bio").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference2 = FirebaseDatabase.getInstance().getReference().child("MyTiket").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnaphot1: dataSnapshot.getChildren()){
                        MyTicket tic = dataSnaphot1.getValue(MyTicket.class);
                        list.add(tic);
                }
                tiketAdapter = new TiketAdapter(My_Profile_Activity.this, list);
                rvTiket.setAdapter(tiketAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_to_home:
                Intent home = new Intent(My_Profile_Activity.this, HomeActivity.class);
                startActivity(home);
                break;

            case  R.id.btn_editProfile:
                Intent edit =  new Intent(My_Profile_Activity.this, Edit_Profile_Activity.class);
                startActivity(edit);
                break;

            case R.id.btn_sign_out:
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                Intent signOut = new Intent(My_Profile_Activity.this, SignIn_Activity.class);
                signOut.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(signOut);
                finish();
                break;
        }

    }

    private void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
