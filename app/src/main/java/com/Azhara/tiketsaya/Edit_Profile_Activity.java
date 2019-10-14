package com.Azhara.tiketsaya;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Edit_Profile_Activity extends AppCompatActivity {

    EditText edtName, edtBio, edtPassword, edtEmail;
    Button btnSave, btnAddphoto;
    ImageView imgProfile;
    LinearLayout btnBack;

    String USERNAME_KEY = "username_key";
    String username_key = "";
    String username_key_new = "";

    DatabaseReference reference;
    StorageReference storage;
    Integer upload_photo_max = 1;
    Uri photo_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile_);

        getUserLocal();

        imgProfile = findViewById(R.id.img_profile);
        edtName = findViewById(R.id.edt_name);
        edtBio = findViewById(R.id.edt_bio);
        edtPassword = findViewById(R.id.edt_password);
        edtEmail = findViewById(R.id.edt_email);
        btnSave = findViewById(R.id.btn_save);
        btnAddphoto = findViewById(R.id.btn_add_photo);
        btnBack = findViewById(R.id.btn_back);

        btnAddphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(Edit_Profile_Activity.this)
                        .load(dataSnapshot.child("url_photo_profile").getValue().toString()).centerCrop().fit()
                        .into(imgProfile);
                edtName.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                edtBio.setText(dataSnapshot.child("bio").getValue().toString());
                edtPassword.setText(dataSnapshot.child("password").getValue().toString());
                edtEmail.setText(dataSnapshot.child("email_address").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSave.setEnabled(false);
                btnSave.setText("Loading...");

                storage = FirebaseStorage.getInstance().getReference().child("PhotoUsers").child(username_key_new);

                final String editName = edtName.getText().toString();
                final String editBio = edtBio.getText().toString();
                final String editPassword = edtPassword.getText().toString();
                final String editEmail = edtEmail.getText().toString();

                if (editName.isEmpty()){
                    btnSave.setEnabled(true);
                    btnSave.setText("save");
                    Toast.makeText(getApplicationContext(), "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }else {
                    if(editBio.isEmpty()){
                        btnSave.setEnabled(true);
                        btnSave.setText("save profile");
                        Toast.makeText(getApplicationContext(), "Bio tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    }else {
                        if (editPassword.isEmpty()){
                            btnSave.setEnabled(true);
                            btnSave.setText("save profile");
                            Toast.makeText(getApplicationContext(), "Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                        }else {
                            if (editEmail.isEmpty()){
                                btnSave.setEnabled(true);
                                btnSave.setText("save profile");
                                Toast.makeText(getApplicationContext(), "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                            }else {
                                if (photo_location != null){
                                    final StorageReference storageReference = storage.child(System.currentTimeMillis() +
                                            "." + getFileExtension(photo_location));

                                    storageReference.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    final String uri_photo = uri.toString();

                                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            dataSnapshot.getRef().child("url_photo_profile").setValue(uri_photo);
                                                            dataSnapshot.getRef().child("nama_lengkap").setValue(editName);
                                                            dataSnapshot.getRef().child("bio").setValue(editBio);
                                                            dataSnapshot.getRef().child("password").setValue(editPassword);
                                                            dataSnapshot.getRef().child("email_address").setValue(editEmail);

                                                            Intent intent = new Intent(Edit_Profile_Activity.this, My_Profile_Activity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }else{
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            dataSnapshot.getRef().child("nama_lengkap").setValue(editName);
                                            dataSnapshot.getRef().child("bio").setValue(editBio);
                                            dataSnapshot.getRef().child("password").setValue(editPassword);
                                            dataSnapshot.getRef().child("email_address").setValue(editEmail);

                                            Intent intent = new Intent(Edit_Profile_Activity.this, My_Profile_Activity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }
                    }
                }




            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void findPhoto(){
        Intent findpic = new Intent();
        findpic.setType("image/*");
        findpic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(findpic, upload_photo_max);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == upload_photo_max && resultCode == RESULT_OK && data != null && data.getData() != null){

            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(imgProfile);

        }
    }

    private void getUserLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
