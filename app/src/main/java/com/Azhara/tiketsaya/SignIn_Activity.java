package com.Azhara.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class SignIn_Activity extends AppCompatActivity {

    TextView register;
    Button sign_in;
    EditText edtUsername, edtPassword;

    DatabaseReference reference;

    String USERNAME_KEY = "username_key";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_);

        register = findViewById(R.id.btn_new_account);
        sign_in = findViewById(R.id.btn_sign_in);
        edtUsername = findViewById(R.id.et_username);
        edtPassword = findViewById(R.id.et_password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn_Activity.this, Register_Activity.class);
                startActivity(intent);
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sign_in.setEnabled(false);
                sign_in.setText("Loading...");

                final String username = edtUsername.getText().toString();
                final String password = edtPassword.getText().toString();

                if (username.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Username/Email Kosong!", Toast.LENGTH_SHORT).show();
                    sign_in.setEnabled(true);
                    sign_in.setText("sign in");
                }else {
                    if (password.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Password Kosong!", Toast.LENGTH_SHORT).show();
                        sign_in.setEnabled(true);
                        sign_in.setText("sign in");
                    }else {
                        reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(username);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Validasi edtUsername
                                if (dataSnapshot.exists()){

                                    String getPasswordFirebase = dataSnapshot.child("password").getValue().toString();
                                    // Validasi Password
                                    if (password.equals(getPasswordFirebase)){
                                        // Menyimpan edtUsername key kepada local
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, edtUsername.getText().toString());
                                        editor.apply();

                                        // pindah activity
                                        Intent intent = new Intent(SignIn_Activity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Password Salah!", Toast.LENGTH_SHORT).show();
                                        sign_in.setEnabled(true);
                                        sign_in.setText("sign in");
                                    }


                                }else {
                                    Toast.makeText(getApplicationContext(), "Username tidak ada", Toast.LENGTH_SHORT).show();
                                    sign_in.setEnabled(true);
                                    sign_in.setText("sign in");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }
        });
    }
}
