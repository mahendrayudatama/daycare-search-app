package com.example.kidcare;

import android.app.Dialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.kidcare.Model.ModelMainAct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("user");
    BottomNavigationView bottomNavigationView;

    boolean isLoggedIn;
    String childStatus = "waiting";
    String currentChild;
    Dialog dialog;

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLoggedIn = user != null;
        if (isLoggedIn == true) {
            getChildData();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FragmentSearch()).commit();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    void getChildData() {
        if (isLoggedIn==true){
            dbref.child(user.getUid()).child("currentChild").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentChild = snapshot.getValue().toString();
                    dbref.child(user.getUid()).child("child").child(snapshot.getValue().toString()).child("status").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            childStatus = task.getResult().getValue().toString();
                            Log.d("currentChildMain", childStatus);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.home:
                fragment = new FragmentSearch();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                break;
            case R.id.transaction:
                if (isLoggedIn == false) {
                    startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                    overridePendingTransition(0, 0);
                    finish();
                } else {
                    fragment = new FragmentTransaction();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                    break;
                }
            case R.id.daycare:
                getChildData();
                if (isLoggedIn == true && childStatus.equals("registered")) {
                    fragment = new FragmentDaycare();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                    break;
                } else if (isLoggedIn == true && childStatus.equals("waiting")) {
                    Toast.makeText(this, "Silahkan melakukan pembayaran untuk mengakses halaman ini", Toast.LENGTH_SHORT).show();
                    break;
                } else if (isLoggedIn == false) {
                    startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                    overridePendingTransition(0, 0);
                    finish();
                    break;
                } else if (isLoggedIn == true && childStatus.equals("not registered")) {
                    Toast.makeText(this, "Anak belum terdaftar pada daycare", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.growth:
                if (isLoggedIn == true) {
                    fragment = new FragmentGrowth();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                    break;
                } else {
                    startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                    overridePendingTransition(0, 0);
                    Toast.makeText(this, "Mohon login untuk mengakses fitur ini", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                }
            case R.id.profile:
                if (isLoggedIn == false) {
                    startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                    overridePendingTransition(0, 0);
                    finish();
                    break;
                } else {
                    fragment = new FragmentProfile();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                    break;
                }
        }
        return true;
    }
}
