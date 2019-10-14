package com.Azhara.tiketsaya;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTicketDetail_Activity extends AppCompatActivity {

    DatabaseReference reference;
    TextView tvNama, tvLokasi, tvDate, tvTime, tvInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail_);

        tvNama = findViewById(R.id.tv_nama_wisata);
        tvLokasi = findViewById(R.id.tv_lokasi_wisata);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        tvInformation = findViewById(R.id.tv_ketentuan);

        Bundle bundle = getIntent().getExtras();
        final String nama_wisata_baru = bundle.getString("nama_tiket");

        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvNama.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                tvLokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                tvDate.setText(dataSnapshot.child("date_wisata").getValue().toString());
                tvTime.setText(dataSnapshot.child("time_wisata").getValue().toString());
                tvInformation.setText(dataSnapshot.child("ketentuan").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

