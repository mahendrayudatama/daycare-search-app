package com.example.kidcare;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class FragmentDashboard extends Fragment {
    TextView boy_num, boy_percent, girl_num, girl_percent, currentQuota, currentQuotaPercentage;
    PieChart pieChart;
    FirebaseAuth user = FirebaseAuth.getInstance();
    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("user");
    DatabaseReference daycareReference = FirebaseDatabase.getInstance().getReference("daycare");
    Button buttonOpenMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);
        boy_num = rootView.findViewById(R.id.boy_num);
        boy_percent = rootView.findViewById(R.id.boy_percentage);
        girl_num = rootView.findViewById(R.id.girl_num);
        girl_percent = rootView.findViewById(R.id.girl_percentage);
        pieChart = rootView.findViewById(R.id.piechart);
        currentQuota = rootView.findViewById(R.id.currentQuota);
        currentQuotaPercentage = rootView.findViewById(R.id.currentQuotaPercentage);
//       buttonOpenMap = rootView.findViewById(R.id.btn_openGmapDashboard);
        getData();
        return rootView;
    }

    public void setData(int boy, int girl, int quota) {
        int boy_p = (int) (((float) boy / quota) * 100);
        int girl_p = (int) (((float) girl / quota) * 100);
        int currentQuotaPercentage = (int) (((float) quota - (boy + girl)) / quota * 100);
        int currentQuota = (int) ((float) quota - (girl + boy));
        Log.d("TAGcurrent", "setData: " + quota);
        boy_num.setText(Integer.toString(boy));
        girl_num.setText(Integer.toString(girl));
        this.currentQuota.setText(Integer.toString(currentQuota));

        boy_percent.setText(Integer.toString(boy_p));
        girl_percent.setText(Integer.toString(girl_p));
        this.currentQuotaPercentage.setText(Integer.toString(currentQuotaPercentage));

        pieChart.addPieSlice(
                new PieModel(
                        "Laki-Laki",
                        Integer.parseInt(boy_num.getText().toString()),
                        Color.parseColor("#29B6F6")));
        pieChart.addPieSlice(
                new PieModel(
                        "Perempuan",
                        Integer.parseInt(girl_num.getText().toString()),
                        Color.parseColor("#f368e0")));
        pieChart.addPieSlice(
                new PieModel(
                        "Sisa Kuota",
                        currentQuota,
                        Color.parseColor("#b2bec3")));

        pieChart.startAnimation();
    }
    void getData(){
        userReference.child(user.getCurrentUser().getUid()).child("currentChild").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                userReference.child(user.getCurrentUser().getUid()).child("child").child(task.getResult().getValue().toString()).child("daycareID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        daycareReference.child(task.getResult().getValue().toString()).child("child").addValueEventListener(new ValueEventListener() {
                            String currentDaycare = task.getResult().getValue().toString();

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int boy = 0;
                                int girl = 0;
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    if (ds.child("gender").getValue().equals("Laki-laki")) {
                                        boy++;
                                    } else {
                                        girl++;
                                    }
                                }
                                int finalBoy = boy;
                                int finalGirl = girl;
                                daycareReference.child(currentDaycare).child("profile").child("quota").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        int quota = task.getResult().getValue(Integer.class);
                                        setData(finalBoy, finalGirl, quota);
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
            }
        });
    }
}
