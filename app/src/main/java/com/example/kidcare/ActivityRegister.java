package com.example.kidcare;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kidcare.Model.ModelChildInfo;
import com.example.kidcare.Model.ModelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


public class ActivityRegister extends AppCompatActivity implements View.OnClickListener, MaterialPickerOnPositiveButtonClickListener {
    EditText editTextFirstName, editTextLastName, editTextPhoneNumber, editTextYourEmail, editTextPassword, editTextConfirmPassword;
    MaterialDatePicker datePicker;
    EditText editTextChildBirthDate;
    EditText editTextChildFirstName;
    EditText editTextChildLastName;
    AutoCompleteTextView editTextChildGender;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //user data
        editTextFirstName = findViewById(R.id.et_firstname);
        editTextLastName = findViewById(R.id.et_lastname);
        editTextPhoneNumber = findViewById(R.id.et_phone);
        editTextYourEmail = findViewById(R.id.et_emailsignin);
        editTextPassword = findViewById(R.id.et_passwordsignin);
        editTextConfirmPassword = findViewById(R.id.et_confirmPasswordsignin);
        progressBar = findViewById(R.id.progressBarSignup);
        //user child data
        editTextChildFirstName = findViewById(R.id.et_child_first_name);
        editTextChildLastName = findViewById(R.id.et_child_last_name);
        editTextChildBirthDate = findViewById(R.id.et_date_picker);
        editTextChildGender = findViewById(R.id.et_gender_pick);
        final long today = MaterialDatePicker.todayInUtcMilliseconds();
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Pilih tanggal").setSelection(today).build();
        editTextChildBirthDate.setOnClickListener(this);

        ArrayList<String> gender = new ArrayList<>();
        gender.add("Laki-laki");
        gender.add("Perempuan");

        ArrayAdapter genderAdapter = new ArrayAdapter(this, R.layout.select_child_dropdown_item, gender);
        editTextChildGender.setAdapter(genderAdapter);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.bt_signup).setOnClickListener(this);
        findViewById(R.id.bt_backsignup).setOnClickListener(this);
        findViewById(R.id.tv_login).setOnClickListener(this);
        datePicker.addOnPositiveButtonClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                finish();
                break;
            case R.id.bt_signup:
                registerUser();
                break;
            case R.id.bt_backsignup:
                finish();
                break;
            case R.id.et_date_picker:
                datePicker.show(getSupportFragmentManager(), "DATE PICKER");
                break;
        }
    }

    private void registerUser() {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String email = editTextYourEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        String childFirstName = editTextChildFirstName.getText().toString().trim();
        String childLastName = editTextChildLastName.getText().toString().trim();
        String childBirthDate = editTextChildBirthDate.getText().toString().trim();
        String childGender = editTextChildGender.getText().toString().trim();

        if (firstName.isEmpty()) {
            editTextFirstName.setError("Data tidak boleh kosong");
            editTextFirstName.requestFocus();
            return;
        }
        if (phoneNumber.isEmpty()) {
            editTextPhoneNumber.setError("Data tidak boleh kosong");
            editTextPhoneNumber.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextYourEmail.setError("Data tidak boleh kosong");
            editTextYourEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextYourEmail.setError("Mohon isi data email dengan benar");
            editTextYourEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Data tidak boleh kosong");
            editTextPassword.requestFocus();
            return;
        }
        if (!editTextConfirmPassword.getText().toString().equals(editTextPassword.getText().toString())) {
            editTextConfirmPassword.setError("Password tidak sama");
            editTextConfirmPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Panjang password minimal 6 karakter");
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Data berhasil didaftarkan", Toast.LENGTH_SHORT).show();
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("user").child(userID);
                    ModelUser userModel = new ModelUser(firstName, lastName, phoneNumber, email);
                    String key = current_user_db.push().getKey();
                    current_user_db.setValue(userModel);
                    current_user_db.child("currentChild").setValue(key);
                    current_user_db = FirebaseDatabase.getInstance().getReference().child("user").child(userID).child("child");
                    ModelChildInfo childInfo = new ModelChildInfo(childFirstName, childLastName, childBirthDate, childGender, "not registered", "null");
                    current_user_db.child(key).setValue(childInfo);
                    finish();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Email sudah dipakai", Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onPositiveButtonClick(Object selection) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis((Long) selection);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = format.format(calendar.getTime());
        editTextChildBirthDate.setText(formattedDate);
    }
}
