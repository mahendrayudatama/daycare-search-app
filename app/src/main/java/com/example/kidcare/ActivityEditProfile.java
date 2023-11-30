package com.example.kidcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kidcare.Model.ModelUser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ActivityEditProfile extends AppCompatActivity implements View.OnClickListener {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbref = database.getReference("user");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    EditText etEmail;
    EditText etPhoneNumber;
    EditText etLastName;
    EditText etFirstName;
    ImageView evEditProfile;
    Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        etFirstName = findViewById(R.id.et_edit_user_first_name);
        etLastName = findViewById(R.id.et_edit_user_last_name);
        etPhoneNumber = findViewById(R.id.et_edit_user_phone_number);
        evEditProfile = findViewById(R.id.iv_edit_profile);
        saveButton = findViewById(R.id.bt_submit_edit_user);
        evEditProfile.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        Intent data = getIntent();

        String firstName = data.getStringExtra("firstName");
        String lastName = data.getStringExtra("lastName");
        String phoneNumber = data.getStringExtra("phoneNumber");


        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etPhoneNumber.setText(phoneNumber);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_edit_profile:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1000);
                break;
            case R.id.bt_submit_edit_user:
                if (etFirstName.getText().toString().isEmpty()){
                    etFirstName.requestFocus();
                    etFirstName.setError("data tidak boleh kosong");
                    return;
                }
                if (etLastName.getText().toString().isEmpty()){
                    etLastName.requestFocus();
                    etLastName.setError("data tidak boleh kosong");
                    return;
                }
                if (etPhoneNumber.getText().toString().isEmpty()){
                    etPhoneNumber.requestFocus();
                    etPhoneNumber.setError("data tidak boleh kosong");
                    return;
                } else {
                    dbref.child(user.getUid()).child("firstName").setValue(etFirstName.getText().toString());
                    dbref.child(user.getUid()).child("lastName").setValue(etLastName.getText().toString());
                    dbref.child(user.getUid()).child("phoneNumber").setValue(etPhoneNumber.getText().toString());
                    Toast.makeText(this, "Data profil berhasil diubah", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        final StorageReference fileRef = storageRef.child("user/" + user.getUid() + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener((OnSuccessListener<? super UploadTask.TaskSnapshot>) (taskSnapshot) -> {
            fileRef.getDownloadUrl().addOnSuccessListener((OnSuccessListener<? super Uri>) (uri) -> {
                String url = uri.toString();
//                dbref.child(user.getUid()).child("profilePict").setValue(url);
                Picasso.get().load(uri).into(evEditProfile);
            });
        });
    }

}
