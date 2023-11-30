package com.example.kidcare;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kidcare.Model.ModelChildInfo;
import com.example.kidcare.Model.ModelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class FragmentEditChildData extends Fragment implements View.OnClickListener, MaterialPickerOnPositiveButtonClickListener, IOnBackPressed {

    MaterialDatePicker datePicker;
    EditText birthDate;
    EditText firstName;
    EditText lastName;
    AutoCompleteTextView gender;
    Button submit;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("user");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit_child_data, container, false);
        firstName = rootView.findViewById(R.id.et_edit_child_first_name);
        lastName = rootView.findViewById(R.id.et_edit_child_last_name);
        birthDate = rootView.findViewById(R.id.et_edit_date_picker);
        gender = rootView.findViewById(R.id.et_edit_gender_pick);
        submit = rootView.findViewById(R.id.bt_submit_edited_child_info);

        ArrayList<String> genderList = new ArrayList<>();
        genderList.add("Laki-laki");
        genderList.add("Perempuan");

        ArrayAdapter genderAdapter = new ArrayAdapter(requireContext(), R.layout.select_child_dropdown_item, genderList);
        this.gender.setAdapter(genderAdapter);

        dbref.child(user.getUid()).child("currentChild").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                dbref.child(user.getUid()).child("child").child(task.getResult().getValue().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        firstName.setText(task.getResult().child("firstName").getValue().toString());
                        lastName.setText(task.getResult().child("lastName").getValue().toString());
                        birthDate.setText(task.getResult().child("birthDate").getValue().toString());
                    }
                });
//                        addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        firstName.setText(snapshot.child("firstName").getValue().toString());
//                        lastName.setText(snapshot.child("lastName").getValue().toString());
//                        birthDate.setText(snapshot.child("birthDate").getValue().toString());
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }
        });

        birthDate.setInputType(InputType.TYPE_NULL);
        final long today = MaterialDatePicker.todayInUtcMilliseconds();
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Pilih tanggal").setSelection(today).build();
        birthDate.setOnClickListener(this);
        submit.setOnClickListener(this);
        datePicker.addOnPositiveButtonClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_edit_date_picker:
                datePicker.show(getActivity().getSupportFragmentManager(), "DATE PICKER");
                break;
            case R.id.bt_submit_edited_child_info:
                postData();
                break;
        }

    }

    @Override
    public void onPositiveButtonClick(Object selection) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis((Long) selection);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = format.format(calendar.getTime());
        birthDate.setText(formattedDate);
    }

    void postData() {
        if (firstName.getText().toString().isEmpty()) {
            firstName.requestFocus();
            firstName.setError("Data tidak boleh kosong", null);
            return;
        }
        if (lastName.getText().toString().isEmpty()) {
            lastName.requestFocus();
            lastName.setError("Data tidak boleh kosong", null);
            return;
        }
        if (birthDate.getText().toString().isEmpty()) {
            birthDate.requestFocus();
            birthDate.setError("Data tidak boleh kosong", null);
            return;

        }
        if (gender.getText().toString().isEmpty()) {
            gender.requestFocus();
            gender.setError("Pilih salah satu gender", null);
            return;
        } else {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("user").child(userID).child("child");
            dbref.child(user.getUid()).child("currentChild").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    String key = task.getResult().getValue().toString();
                    dbref.child(user.getUid()).child("child").child(task.getResult().getValue().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            ModelChildInfo childInfo = new ModelChildInfo(firstName.getText().toString(),
                                    lastName.getText().toString(),
                                    birthDate.getText().toString(),
                                    gender.getText().toString(),
                                    task.getResult().child("status").getValue().toString(),
                                    task.getResult().child("daycareID").getValue().toString());
                            current_user_db.child(key).setValue(childInfo);
                        }
                    });
                }
            });
            Toast.makeText(getContext(), "Data berhasil diubah", Toast.LENGTH_SHORT).show();
            Fragment fragment = new FragmentProfile();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        }

    }

    @Override
    public boolean onBackPressed() {
        Fragment fragment = new FragmentProfile();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
        return true;
    }
}
