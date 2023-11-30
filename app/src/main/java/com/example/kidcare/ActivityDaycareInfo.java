package com.example.kidcare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.List;
import java.util.TimeZone;

public class ActivityDaycareInfo extends AppCompatActivity implements View.OnClickListener {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbrefUser = database.getReference("user");
    DatabaseReference dbrefDaycare = database.getReference("daycare");
    AutoCompleteTextView childPick;
    String daycareID;
    ArrayList<String> childName;
    Dialog dialog;
    String geoLatitude;
    String geoLongitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daycare_info);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ImageView backButton = findViewById(R.id.iv_backButtonDaycareInfo);
        String daycareName = getIntent().getStringExtra("daycareName");
        String description = getIntent().getStringExtra("description");
        String facilities = getIntent().getStringExtra("facilities");
        String email = getIntent().getStringExtra("email");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        String price = getIntent().getStringExtra("price");
        String quota = getIntent().getStringExtra("quota");
        String currentQuota = getIntent().getStringExtra("currentQuota");
        String mediaUrl = getIntent().getStringExtra("mediaUrl");
        geoLatitude = getIntent().getStringExtra("latitude");
        geoLongitude = getIntent().getStringExtra("longitude");
        daycareID = email.replace("@", "").replace(".", "");

        Button selectDaycare = findViewById(R.id.bt_selectdaycare);
        Button openGmap = findViewById(R.id.btn_openGmap);
        ImageSlider imageSlider = findViewById(R.id.sliderCarousel);
        TextView tvDaycareName = findViewById(R.id.tv_currentDaycareName);
        TextView tvDaycareDescription = findViewById(R.id.tv_daycareDescription);
        TextView tvFacilities = findViewById(R.id.tv_facilities);
        TextView tvPrice = findViewById(R.id.tv_daycarePrice);
        ArrayList<SlideModel> imageList = new ArrayList<>();

        tvDaycareName.setText("Daycare " + daycareName);
        tvDaycareDescription.setText(description);
        tvFacilities.setText("-" + facilities.replace(",", "\n-"));
        tvPrice.setText("Rp. " + price);
        openGmap.setOnClickListener(this);

        String[] daycareMedia = mediaUrl.split(",");

        for (String a : daycareMedia) {
            imageList.add(new SlideModel(a, ScaleTypes.CENTER_CROP));
        }
        imageSlider.setImageList(imageList);
        imageSlider.startSliding(3000);
        backButton.setOnClickListener(this);
        checkQuota(currentQuota, selectDaycare);
//        new Intent(this, ActivityMain.class);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_selectdaycare:
                if (user != null) {
                    showCustomDialog();
                    break;
                } else {
                    Toast.makeText(this, "Silahkan login untuk mendaftar ke daycare ini!", Toast.LENGTH_SHORT).show();
                    break;
                }

            case R.id.iv_backButtonDaycareInfo:
                Intent intent = new Intent(ActivityDaycareInfo.this, ActivityMain.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bt_confirm:
                if (childPick.getText().toString().equals("") || childPick.getText().toString().equals("Pilih Anak")) {
                    childPick.setError("Pilih salah satu anak!");
                } else {
                    dbrefUser.child(user.getUid()).child("child").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                                if ((dataSnapshot.child("firstName").getValue().toString().replace(" ", "") + dataSnapshot.child("lastName").getValue().toString().replace(" ", "")).equals(childPick.getText().toString().replace(" ", ""))) {
                                    String key = dbrefDaycare.child(daycareID).child("booking").push().getKey();
                                    dbrefDaycare.child(daycareID).child("booking").child(key).child("firstName").setValue(dataSnapshot.child("firstName").getValue().toString());
                                    dbrefDaycare.child(daycareID).child("booking").child(key).child("lastName").setValue(dataSnapshot.child("lastName").getValue().toString());
                                    dbrefDaycare.child(daycareID).child("booking").child(key).child("UID").setValue(user.getUid());
                                    dbrefDaycare.child(daycareID).child("booking").child(key).child("childID").setValue(dataSnapshot.getKey());
                                    dbrefDaycare.child(daycareID).child("booking").child(key).child("date").setValue(getCurrentDate());
                                    dbrefDaycare.child(daycareID).child("booking").child(key).child("status").setValue("waiting");
                                    dbrefDaycare.child(daycareID).child("booking").child(key).child("birthDate").setValue(dataSnapshot.child("birthDate").getValue().toString());
                                    dbrefDaycare.child(daycareID).child("booking").child(key).child("gender").setValue(dataSnapshot.child("gender").getValue().toString());
                                    dbrefDaycare.child(daycareID).child("booking").child(key).child("price").setValue("noll");
                                    dbrefDaycare.child(daycareID).child("booking").child(key).child("accountNumber").setValue("noll");
                                    dbrefDaycare.child(daycareID).child("booking").child(key).child("lastPayment").setValue(null);

                                    dbrefUser.child(user.getUid()).child("child").child(dataSnapshot.getKey()).child("status").setValue("waiting");
                                    dbrefUser.child(user.getUid()).child("child").child(dataSnapshot.getKey()).child("daycareID").setValue(daycareID);
                                    dialog.dismiss();
                                    Toast.makeText(ActivityDaycareInfo.this.getApplication(), "Proses booking berhasil, silahkan menuju ke menu Transaksi.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
                break;
            case R.id.btn_openGmap:
                Log.d("geoMap", "geo:" + getIntent().getStringExtra("latitude") + "," + getIntent().getStringExtra("longitude"));
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + getIntent().getStringExtra("latitude") + "," + getIntent().getStringExtra("longitude"));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
        }

    }

    void showCustomDialog() {
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_confirm_booking);
        childPick = dialog.findViewById(R.id.et_child_pick);
        getChildName();
        ArrayAdapter childList = new ArrayAdapter(getApplication().getApplicationContext(), R.layout.select_child_dropdown_item_small, childName);
        childPick.setAdapter(childList);
        Button submitButton = dialog.findViewById(R.id.bt_confirm);
        submitButton.setOnClickListener(this);
        dialog.show();
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("WIB"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = format.format(calendar.getTime());
        return formattedDate;
    }

    void checkQuota(String currentQuota, Button select) {
        if (Integer.parseInt(currentQuota) == 0) {
            select.setText("Kuota penuh");
            select.setBackgroundColor(getResources().getColor(R.color.gray_checklist));
            select.setTextColor(getResources().getColor(R.color.white));
            select.setEnabled(false);
        } else {
            select.setOnClickListener(this);
        }
    }


    void getChildName() {
        childName = new ArrayList<>();
        try {
            if (user.getEmail() != null) {
                dbrefUser.child(user.getUid()).child("child").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        for (DataSnapshot ds : task.getResult().getChildren()) {
                            if (ds.child("daycareID").getValue().equals("null")) {
                                childName.add(ds.child("firstName").getValue().toString() + " " + ds.child("lastName").getValue().toString());
                            }
                        }
                    }
                });
//                dbrefUser.child(user.getUid()).child("child").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot ds : snapshot.getChildren()) {
//                            if (ds.child("daycareID").getValue().equals("null")) {
//                                childName.add(ds.child("firstName").getValue().toString() + " " + ds.child("lastName").getValue().toString());
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }
        } catch (Exception e) {

        }

    }

    void registerChildToDaycare() {
        dbrefUser.child(user.getUid()).child("child").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    if ((dataSnapshot.child("firstName").getValue().toString().replace(" ", "") + dataSnapshot.child("lastName").getValue().toString().replace(" ", "")).equals(childPick.getText().toString().replace(" ", ""))) {
                        String key = dbrefDaycare.child(daycareID).child("booking").push().getKey();
                        dbrefDaycare.child(daycareID).child("booking").child(key).child("firstName").setValue(dataSnapshot.child("firstName").getValue().toString());
                        dbrefDaycare.child(daycareID).child("booking").child(key).child("lastName").setValue(dataSnapshot.child("lastName").getValue().toString());
                        dbrefDaycare.child(daycareID).child("booking").child(key).child("UID").setValue(user.getUid());
                        dbrefDaycare.child(daycareID).child("booking").child(key).child("childID").setValue(dataSnapshot.getKey());
                        dbrefDaycare.child(daycareID).child("booking").child(key).child("date").setValue(getCurrentDate());
                        dbrefDaycare.child(daycareID).child("booking").child(key).child("status").setValue("waiting");
                        dbrefDaycare.child(daycareID).child("booking").child(key).child("birthDate").setValue(dataSnapshot.child("birthDate").getValue().toString());
                        dbrefDaycare.child(daycareID).child("booking").child(key).child("gender").setValue(dataSnapshot.child("gender").getValue().toString());
                        dbrefDaycare.child(daycareID).child("booking").child(key).child("price").setValue("noll");
                        dbrefDaycare.child(daycareID).child("booking").child(key).child("accountNumber").setValue("noll");
                        dbrefDaycare.child(daycareID).child("booking").child(key).child("lastPayment").setValue(null);

                        dbrefUser.child(user.getUid()).child("child").child(dataSnapshot.getKey()).child("status").setValue("waiting");
                        dbrefUser.child(user.getUid()).child("child").child(dataSnapshot.getKey()).child("daycareID").setValue(daycareID);
                        dialog.dismiss();
                        Toast.makeText(ActivityDaycareInfo.this.getApplication(), "Proses booking berhasil, silahkan menuju ke menu Transaksi.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    void showCustomDialog2() {
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_confirm_booking);
        childPick = dialog.findViewById(R.id.et_child_pick);
        getChildName();
        ArrayAdapter childList = new ArrayAdapter(getApplication().getApplicationContext(), R.layout.select_child_dropdown_item_small, childName);
        childPick.setAdapter(childList);
        Button submitButton = dialog.findViewById(R.id.bt_confirm);
        submitButton.setOnClickListener(this);
        dialog.show();
    }
}
