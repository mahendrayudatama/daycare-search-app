package com.example.kidcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.kidcare.Adapter.AdapterViewPager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentTransaction extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("user");

    ViewPager viewPager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_transaction, container, false);
        TextView userName = rootView.findViewById(R.id.tv_helloBooking);
        viewPager = rootView.findViewById(R.id.vp_transaction);
        tabLayout = rootView.findViewById(R.id.tl_transaction);

        AdapterViewPager adapterViewPager = new AdapterViewPager(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapterViewPager.addFragment(new FragmentBooking(), "Booking");
        adapterViewPager.addFragment(new FragmentInvoice(), "Invoice");
        getData(userName);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapterViewPager);
        tabLayout.setupWithViewPager(viewPager);


        return rootView;
    }

    void getData(TextView userName) {
        userReference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                userName.setText("Halo, " + task.getResult().child("firstName").getValue(String.class).substring(0, 1).toUpperCase() + task.getResult().child("firstName").getValue(String.class).substring(1)
                        + " " + task.getResult().child("lastName").getValue(String.class).substring(0, 1).toUpperCase() + task.getResult().child("lastName").getValue(String.class).substring(1));
            }
        });
    }

}
