package com.Azhara.tiketsaya;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TicketDetailActivity extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_continue;
    ImageView imgBackground;
    TextView tvNamaWisata, tvLocation, tvPhotoSpot, tvWifi, tvFestival;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        imgBackground = findViewById(R.id.img_background);
        tvNamaWisata =  findViewById(R.id.tv_nama_wisata);
        tvLocation =  findViewById(R.id.tv_location);
        tvPhotoSpot =  findViewById(R.id.tv_photo_spot);
        tvWifi = findViewById(R.id.tv_wifi_available);
        tvFestival = findViewById(R.id.tv_festival);

        // Mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("Jenis_tiket");

        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(TicketDetailActivity.this).
                        load(dataSnapshot.child("url_thumbnail").getValue().toString()).centerCrop().fit()
                        .into(imgBackground);
                tvNamaWisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                tvLocation.setText(dataSnapshot.child("lokasi").getValue().toString());
                tvPhotoSpot.setText(dataSnapshot.child("is_photo_spot").getValue().toString());
                tvWifi.setText(dataSnapshot.child("is_Wifi").getValue().toString());
                tvFestival.setText(dataSnapshot.child("is_Festival").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketDetailActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_continue = findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicketDetailActivity.this, CheckOutActivity.class);
                intent.putExtra("Jenis_tiket", jenis_tiket_baru);
                startActivity(intent);
            }
        });
    }
}
