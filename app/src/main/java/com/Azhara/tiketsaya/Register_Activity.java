package com.Azhara.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register_Activity extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_continue;
    EditText edtUsername, edtPassword, edtEmail;
    DatabaseReference reference, reference2;

    String USERNAME_KEY = "username_key";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);
        //EditText
        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
        edtEmail = findViewById(R.id.email_address);

        reference2 = FirebaseDatabase.getInstance().getReference().child("Users");


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_continue.setEnabled(false);
                btn_continue.setText("Loading...");

                final String username = edtUsername.getText().toString();
                final String password = edtPassword.getText().toString();
                final String email = edtEmail.getText().toString();

                if (username.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Username Harus Diisi", Toast.LENGTH_SHORT).show();
                    btn_continue.setEnabled(true);
                    btn_continue.setText("Continue");
                }else {
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                btn_continue.setEnabled(true);
                                btn_continue.setText("Continue");
                                Toast.makeText(getApplicationContext(), "Username Sudah ada! Gunakan yang lain.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (password.isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Password Harus Diisi", Toast.LENGTH_SHORT).show();
                                    btn_continue.setEnabled(true);
                                    btn_continue.setText("Continue");
                                }
                                else {
                                    if (email.isEmpty()){
                                        Toast.makeText(getApplicationContext(), "Email Harus Diisi", Toast.LENGTH_SHORT).show();
                                        btn_continue.setEnabled(true);
                                        btn_continue.setText("Continue");
                                    }else {

                                        // Menyimpan data kepada local storage
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, edtUsername.getText().toString());
                                        editor.apply();

                                        reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                                .child(username);
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().child("username").setValue(username);
                                                dataSnapshot.getRef().child("password").setValue(password);
                                                dataSnapshot.getRef().child("email_address").setValue(email);
                                                dataSnapshot.getRef().child("user_balance").setValue(1000);

                                                // Pindah Activity
                                                Intent intent = new Intent(Register_Activity.this, RegisterProfile_Activity.class);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pindah Activity
                Intent intent = new Intent(Register_Activity.this, SignIn_Activity.class);
                startActivity(intent);
            }
        });


    }
}
