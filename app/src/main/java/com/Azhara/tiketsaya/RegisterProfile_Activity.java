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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterProfile_Activity extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_continue, add_photo;
    ImageView register_photo;
    EditText edtNama, edtBio;
    DatabaseReference reference;
    StorageReference storage;
    Uri photo_location;
    Integer photo_max = 1;
    String USERNAME_KEY = "username_key";
    String username_key = "";
    String new_username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile_);

        getUsernameLocal();

        btn_continue = findViewById(R.id.btn_continue);
        btn_back = findViewById(R.id.btn_back);
        add_photo = findViewById(R.id.btn_add_photo);
        register_photo = findViewById(R.id.photo_register);
        edtNama = findViewById(R.id.nama_lengkap);
        edtBio = findViewById(R.id.tv_bio);


        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterProfile_Activity.this, Register_Activity.class);
                startActivity(intent);
                finish();
            }
        });


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menyimpan ke firebase
                btn_continue.setEnabled(false);
                btn_continue.setText("Loading..");
                reference = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(new_username_key);
                storage = FirebaseStorage.getInstance().getReference().child("PhotoUsers").child(new_username_key);

                final String nama = edtNama.getText().toString();
                final String bio = edtBio.getText().toString();

                // Validasi File
                if (photo_location != null){
                    final StorageReference storageReference = storage.child(System.currentTimeMillis() + "."
                            + getFileExtension(photo_location));

                    storageReference.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uri_photo = uri.toString();
                                    reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                    reference.getRef().child("nama_lengkap").setValue(nama);
                                    reference.getRef().child("bio").setValue(bio);
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Intent intent = new Intent(RegisterProfile_Activity.this, Success_register_activity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }else {
                    final StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReference().child("PhotoUsers/empty_photo.png");

                        if (nama.isEmpty()){
                            Toast.makeText(getApplicationContext(), "Nama Harus Diisi", Toast.LENGTH_SHORT).show();
                            btn_continue.setEnabled(true);
                            btn_continue.setText("Continue");
                        }else {
                            if (bio.isEmpty()){
                                Toast.makeText(getApplicationContext(), "Bio Harus Diisi", Toast.LENGTH_SHORT).show();
                                btn_continue.setEnabled(true);
                                btn_continue.setText("Continue");
                            }else {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String uri_photo = uri.toString();
                                        reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                        reference.getRef().child("nama_lengkap").setValue(edtNama.getText().toString());
                                        reference.getRef().child("bio").setValue(edtBio.getText().toString());
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        Intent intent = new Intent(RegisterProfile_Activity.this, Success_register_activity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }


                }
            }
        });

    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto()
    {
        Intent findPic = new Intent();
        findPic.setType("image/*");
        findPic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(findPic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null){

            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(register_photo);

        }
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        new_username_key = sharedPreferences.getString(username_key,   "");
    }
}
