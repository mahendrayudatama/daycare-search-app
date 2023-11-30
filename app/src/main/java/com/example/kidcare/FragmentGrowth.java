package com.example.kidcare;


import static com.example.kidcare.Model.ModelGrowth.dateComparatorAZ;
import static com.example.kidcare.Model.ModelGrowthList.dateComparatorZA;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidcare.Adapter.AdapterGrowth;
import com.example.kidcare.Model.ModelGrowth;
import com.example.kidcare.Model.ModelGrowthList;
import com.example.kidcare.Model.ModelSearch;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FragmentGrowth extends Fragment implements View.OnClickListener, IRecyclerView {
    FirebaseAuth user = FirebaseAuth.getInstance();
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    AdapterGrowth mAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ModelGrowthList> growthList;
    Button addGrowth;
    TextView childName;
    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_growth, container, false);
        mRecyclerView = rootView.findViewById(R.id.rv_growth);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        addGrowth = rootView.findViewById(R.id.btn_submitChildGrowth);
        addGrowth.setOnClickListener(this);
        growthList = new ArrayList<>();
        childName = rootView.findViewById(R.id.tv_childNameGrowth);
        TextView tvKosong = rootView.findViewById(R.id.tv_kosong);
//        LineChart lineChart = rootView.findViewById(R.id.lineChartGrowth);
        List<Entry> linelist = new ArrayList<Entry>();
        if (growthList.isEmpty()){
            tvKosong.setVisibility(View.VISIBLE);
        }
        getData(tvKosong);
        mAdapter = new AdapterGrowth(growthList, this);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submitChildGrowth:
                Intent intent = new Intent(this.getContext(), ActivityAddChildGrowth.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(getContext(), ActivityEditGrowth.class);
        intent.putExtra("date", growthList.get(position).getDate());
        intent.putExtra("height", growthList.get(position).getHeight());
        intent.putExtra("weight", growthList.get(position).getWeight());
        intent.putExtra("key", growthList.get(position).getKey());
        startActivity(intent);
    }

    @Override
    public void OnItemClick2(int position) {
        showCustomDialog(position);
    }

    @Override
    public void OnItemClick3(int position) {

    }

    void showCustomDialog(int position) {
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_confirm_delete);
        Button submitButton = dialog.findViewById(R.id.bt_confirm_delete);
        Button cancelButton = dialog.findViewById(R.id.bt_cancel_delete);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbref.child("user").child(user.getCurrentUser().getUid()).child("currentChild").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        dbref.child("user").child(user.getCurrentUser().getUid()).child("child").child(task.getResult().getValue().toString()).child("growth").child(growthList.get(position).getKey()).removeValue();
                        dialog.dismiss();
                    }
                });
                Toast.makeText(getContext(), "Data berhasil di hapus", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    void getData(TextView tvKosong){
        dbref.child("user").child(user.getCurrentUser().getUid()).child("currentChild").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("testdatagrowth", "onComplete: " + task.getResult().getValue());
                dbref.child("user").child(user.getCurrentUser().getUid()).child("child").child(task.getResult().getValue().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        String firstName = task.getResult().child("firstName").getValue().toString();
                        String lastName = task.getResult().child("lastName").getValue().toString();
                        childName.setText(firstName.substring(0, 1).toUpperCase() + firstName.substring(1) + " " + lastName.substring(0, 1).toUpperCase() + lastName.substring(1));
                    }
                });
                dbref.child("user").child(user.getCurrentUser().getUid()).child("child").child(task.getResult().getValue().toString()).child("growth").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        growthList.clear();
                        for (DataSnapshot a : snapshot.getChildren()) {

                            tvKosong.setVisibility(View.INVISIBLE);
                            String key = a.getKey().toString();
                            String age = a.child("age").getValue().toString();
                            String date = a.child("date").getValue().toString();
                            String height = a.child("height").getValue().toString();
                            String weight = a.child("weight").getValue().toString();
                            growthList.add(new ModelGrowthList(age, date, height, weight, key));
                        }
                        growthList.sort(dateComparatorZA);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}

