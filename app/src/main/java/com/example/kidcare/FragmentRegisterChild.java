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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class FragmentRegisterChild extends Fragment implements View.OnClickListener, MaterialPickerOnPositiveButtonClickListener, IOnBackPressed {
    MaterialDatePicker datePicker;
    EditText birthDate;
    EditText firstName;
    EditText lastName;
    AutoCompleteTextView gender;
    Button submit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_register_child, container, false);
        firstName = rootView.findViewById(R.id.et_child_first_name);
        lastName = rootView.findViewById(R.id.et_child_last_name);
        birthDate = rootView.findViewById(R.id.et_date_picker);
        gender = rootView.findViewById(R.id.et_gender_pick);
        submit = rootView.findViewById(R.id.bt_submit_child_info);
        ArrayList<String> gender = new ArrayList<>();
        gender.add("Laki-laki");
        gender.add("Perempuan");

        ArrayAdapter genderAdapter = new ArrayAdapter(requireContext(), R.layout.select_child_dropdown_item, gender);
        this.gender.setAdapter(genderAdapter);

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
            case R.id.et_date_picker:
                datePicker.show(getActivity().getSupportFragmentManager(), "DATE PICKER");
                break;
            case R.id.bt_submit_child_info:
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
            firstName.setError("Data tidak boleh kosong");
            firstName.requestFocus();
            return;
        }
        if (lastName.getText().toString().isEmpty()) {
            lastName.setError("Data tidak boleh kosong");
            lastName.requestFocus();
            return;
        }
        if (birthDate.getText().toString().isEmpty()) {
            birthDate.setError("Data tidak boleh kosong");
            birthDate.requestFocus();
            return;
        }
        if (gender.getText().toString().isEmpty()) {
            gender.setError("Data tidak boleh kosong", null);
            gender.requestFocus();
            return;
        }
        if (firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || gender.getText().toString().isEmpty() || birthDate.getText().toString().isEmpty()) {

        } else {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("user").child(userID).child("child");
            String key = current_user_db.push().getKey();
            ModelChildInfo childInfo = new ModelChildInfo(firstName.getText().toString(), lastName.getText().toString(), birthDate.getText().toString(), gender.getText().toString(), "not registered", "null");
            current_user_db.child(key).setValue(childInfo);
            Fragment fragment = new FragmentProfile();
            Toast.makeText(getContext(), "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
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
