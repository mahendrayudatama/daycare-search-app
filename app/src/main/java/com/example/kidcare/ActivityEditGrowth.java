package com.example.kidcare;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kidcare.Model.ModelChildInfo;
import com.example.kidcare.Model.ModelGrowth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ActivityEditGrowth extends AppCompatActivity implements MaterialPickerOnPositiveButtonClickListener, View.OnClickListener {
    MaterialDatePicker datePicker;
    EditText date;
    EditText weight;
    EditText height;
    Button submitGrowth;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child_growth);
        weight = findViewById(R.id.et_edit_berat_badan);
        height = findViewById(R.id.et_edit_tinggi_badan);
        date = findViewById(R.id.et_edit_date_picker_growth);
        submitGrowth = findViewById(R.id.btn_edit_submitChildGrowth);
        weight.setText(getIntent().getStringExtra("weight"));
        height.setText(getIntent().getStringExtra("height"));
        date.setText(getIntent().getStringExtra("date"));


        submitGrowth.setOnClickListener(this);
        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(this);
        final long today = MaterialDatePicker.todayInUtcMilliseconds();
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Pilih tanggal").setSelection(today).build();
        datePicker.addOnPositiveButtonClickListener(this);

    }


    @Override
    public void onPositiveButtonClick(Object selection) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis((Long) selection);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = format.format(calendar.getTime());
        date.setText(formattedDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_edit_date_picker_growth:
                datePicker.show(getSupportFragmentManager(), "DATE PICKER");
                break;
            case R.id.btn_edit_submitChildGrowth:
                postData();
                break;
        }
    }

    void postData() {
        if (weight.getText().toString().isEmpty()) {
            weight.setError("data tidak boleh kosong");
            weight.requestFocus();
//            return;
        }
        if (height.getText().toString().isEmpty()) {
            height.setError("data tidak boleh kosong");
            height.requestFocus();
//            return;
        }
        if (date.getText().toString().isEmpty()) {
            date.requestFocus();
            date.setError("data tidak boleh kosong",null);
//            return;

        }
        if (weight.getText().toString().isEmpty() || height.getText().toString().isEmpty() || date.getText().toString().isEmpty()) {
            Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final String[] currentChild = new String[1];
            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("user").child(userID).child("child");
            FirebaseDatabase.getInstance().getReference("user").child(userID).child("currentChild").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    currentChild[0] = task.getResult().getValue().toString();
                    current_user_db.child(task.getResult().getValue().toString()).child("birthDate").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Date birthDate = null;
                            Date currentDate = null;
                            try {
                                birthDate = format.parse(task.getResult().getValue().toString());
                                currentDate = format.parse(date.getText().toString());
                                Calendar calendar = Calendar.getInstance();

                                calendar.setTime(birthDate);
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH) + 1;
                                int date1 = calendar.get(Calendar.DATE);
                                LocalDate localBirthDate = LocalDate.of(year, month, date1);
                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.setTime(currentDate);
                                int year2 = calendar2.get(Calendar.YEAR);
                                int month2 = calendar2.get(Calendar.MONTH) + 1;
                                int date2 = calendar2.get(Calendar.DATE);
                                LocalDate localcurrentDate = LocalDate.of(year2, month2, date2);
                                Period diff = Period.between(localBirthDate, localcurrentDate);
                                ModelGrowth modelGrowth = new ModelGrowth(Integer.toString(diff.getYears()) + " tahun " + Integer.toString(diff.getMonths()) + " bulan " + Integer.toString(diff.getDays()) + " hari", date.getText().toString(), height.getText().toString(), weight.getText().toString());
                                current_user_db.child(currentChild[0]).child("growth").child(getIntent().getStringExtra("key")).setValue(modelGrowth).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ActivityEditGrowth.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }
            });
            finish();
        }
    }
}
