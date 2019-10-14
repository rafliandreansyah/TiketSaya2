package com.Azhara.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class CheckOutActivity extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_pay, btn_minus, btn_plus;
    TextView text_jml_ticket, total_harga, tvBalance, tvNamaWisata, tvLokasiWisata, tvKetentuan;
    Integer value_jmlTicker = 1;
    Integer harga = 50;
    Integer balance = 200;
    Integer totalHarga = 0;
    ImageView notice_uang;

    DatabaseReference reference, reference2, reference3, reference4;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    String dateWisata;
    String timeWisata;

    Integer noTransaksi = new Random().nextInt();
    Integer sisaBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        getUsernameLocal();

        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("Jenis_tiket");

        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);
        text_jml_ticket = findViewById(R.id.text_jumlah_ticket);
        total_harga = findViewById(R.id.text_totalHarga);
        tvBalance = findViewById(R.id.tv_balance);
        btn_pay = findViewById(R.id.btn_pay);
        notice_uang = findViewById(R.id.notice_uang);
        tvNamaWisata = findViewById(R.id.tv_nama_wisata);
        tvLokasiWisata = findViewById(R.id.tv_lokasi_wisata);
        tvKetentuan = findViewById(R.id.tv_ketentuan);

        // Set Default btn_minus
        text_jml_ticket.setText(value_jmlTicker.toString());
        btn_minus.animate().alpha(0).setDuration(300).start();
        btn_minus.setEnabled(false);

        // set Value baru
        notice_uang.setVisibility(View.GONE);

        //Mengambil data user dari firebase
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                balance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                tvBalance.setText("$US " + balance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Mengambil data wisata dari firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvNamaWisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                tvLokasiWisata.setText(dataSnapshot.child("lokasi").getValue().toString());
                tvKetentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
                harga = Integer.valueOf(dataSnapshot.child("harga_ticket").getValue().toString());
                dateWisata = dataSnapshot.child("date_wisata").getValue().toString();
                timeWisata = dataSnapshot.child("time_wisata").getValue().toString();
                totalHarga = harga * value_jmlTicker;
                total_harga.setText("$US" + totalHarga + "");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_jmlTicker += 1;
                text_jml_ticket.setText(value_jmlTicker.toString());
                if (value_jmlTicker > 1){
                    btn_minus.animate().alpha(1).setDuration(300).start();
                    btn_minus.setEnabled(true);
                }
                totalHarga = harga * value_jmlTicker;
                total_harga.setText("$US" + totalHarga + "");

                if (totalHarga > balance){
                    btn_pay.animate().translationY(200).alpha(0).setDuration(300).start();
                    btn_pay.setEnabled(false);
                    tvBalance.setTextColor(Color.parseColor("#D1206B"));
                    notice_uang.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_jmlTicker -= 1;
                text_jml_ticket.setText(value_jmlTicker.toString());
                if (value_jmlTicker < 2 ){
                    btn_minus.animate().alpha(0).setDuration(300).start();
                    btn_minus.setEnabled(false);
                }
                totalHarga = harga * value_jmlTicker;
                total_harga.setText("$US" + totalHarga + "");
                if (totalHarga <= balance){
                    btn_pay.animate().translationY(0).alpha(1).setDuration(300).start();
                    btn_pay.setEnabled(true);
                    tvBalance.setTextColor(Color.parseColor("#203DD1"));
                    notice_uang.setVisibility(View.GONE);
                }
            }
        });


        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutActivity.this, TicketDetailActivity.class);
                startActivity(intent);
            }
        });


        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Membuat table baru "MyTiket" di firebase dari hasil pembelian tiket
                reference3 = FirebaseDatabase.getInstance().getReference()
                        .child("MyTiket")
                        .child(username_key_new)
                        .child(tvNamaWisata.getText().toString() + noTransaksi);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("id_tiket").setValue(tvNamaWisata.getText()
                                .toString()+ noTransaksi);
                        dataSnapshot.getRef().child("nama_wisata").setValue(tvNamaWisata.getText().toString());
                        dataSnapshot.getRef().child("lokasi").setValue(tvLokasiWisata.getText().toString());
                        dataSnapshot.getRef().child("jumlah_tiket").setValue(value_jmlTicker.toString());
                        dataSnapshot.getRef().child("date").setValue(dateWisata);
                        dataSnapshot.getRef().child("time_wisata").setValue(timeWisata);
                        dataSnapshot.getRef().child("ketentuan").setValue(tvKetentuan.getText().toString());

                        Intent intent = new Intent(CheckOutActivity.this, SuccessBuy_Activity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                reference4 = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisaBalance = balance - totalHarga;
                        dataSnapshot.getRef().child("user_balance").setValue(sisaBalance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
