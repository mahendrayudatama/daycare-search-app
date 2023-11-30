package com.example.kidcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.kidcare.Adapter.AdapterViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentDaycare extends Fragment {
    FirebaseAuth user = FirebaseAuth.getInstance();
    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("user");
    DatabaseReference daycareReference = FirebaseDatabase.getInstance().getReference("daycare");
    private DrawerLayout drawerLayout;
    ViewPager viewPager;
    TabLayout tabLayout;
    AdapterViewPager adapterViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_daycare, container, false);
        viewPager = rootView.findViewById(R.id.vp_daycare);
        tabLayout = rootView.findViewById(R.id.tl_daycare);

        AdapterViewPager adapterViewPager = new AdapterViewPager(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapterViewPager.addFragment(new FragmentDashboard(),"Info TPA");
        adapterViewPager.addFragment(new FragmentActivity(),"Aktivitas");

        getData(rootView);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapterViewPager);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }
    void getData(ViewGroup rootView){
        userReference.child(user.getUid()).child("currentChild").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                userReference.child(user.getUid()).child("child").child(task.getResult().getValue().toString()).child("daycareID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        daycareReference.child(task.getResult().getValue().toString()).child("profile").child("daycareName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                TextView daycareTitle = rootView.findViewById(R.id.tv_daycare_title);
                                daycareTitle.setText(task.getResult().getValue(String.class));
                            }
                        });
                    }
                });
            }
        });
//        userReference.child(user.getUid()).child("currentChild").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userReference.child(user.getUid()).child("child").child(snapshot.getValue().toString()).child("daycareID").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        daycareReference.child(snapshot.getValue(String.class)).child("profile").child("daycareName").addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                TextView daycareTitle = rootView.findViewById(R.id.tv_daycare_title);
//                                daycareTitle.setText(snapshot.getValue(String.class));
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

}
