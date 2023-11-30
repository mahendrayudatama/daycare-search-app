package com.example.kidcare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kidcare.Model.ModelDaycare;
import com.example.kidcare.Adapter.AdapterSearch;
import com.example.kidcare.Model.ModelSearch;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class FragmentSearch extends Fragment implements View.OnKeyListener, IRecyclerView, AdapterView.OnItemSelectedListener {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("daycare");
    private RecyclerView mRecyclerView;
    public AdapterSearch mAdapter;
    private RecyclerView.LayoutManager mLayoutmanager;
    public Double currentLatitude = 0.1;
    public Double currentLongitude = 0.2;
    private SwipeRefreshLayout swipeRefreshLayout;
    LocationManager locationManager;
    Spinner spinner;

    ArrayList<ModelDaycare> daycareList = new ArrayList<>();
    ArrayList<ModelSearch> searchItemDistance;
    FusedLocationProviderClient fusedLocationProviderClient;

    EditText etSearch;
    TextView tvDaycareNotFound;
    TextView tvPulltoRefresh;
    ImageView ivPulltoRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        searchItemDistance = new ArrayList<>();
        tvDaycareNotFound = rootView.findViewById(R.id.tv_daycare_not_found);
        tvPulltoRefresh = rootView.findViewById(R.id.tv_pull_to_refresh);
        ivPulltoRefresh = rootView.findViewById(R.id.iv_down_arrow);
        ArrayList<String> sortName = new ArrayList<>();
        sortName.add("Sortir jarak dekat ke jauh");
        sortName.add("Sortir jarak jauh ke dekat");
        sortName.add("Sortir harga kecil ke besar");
        sortName.add("Sortir harga besar ke kecil");
        spinner = rootView.findViewById(R.id.spinner_sort);
        ArrayAdapter<CharSequence> adapterSort = new ArrayAdapter(requireContext(), R.layout.select_child_dropdown_item, sortName);
        adapterSort.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSort);
        spinner.setOnItemSelectedListener(this);
        getDataStatic();
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        if (ActivityCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    getCurrentlocation();
                    Collections.sort(searchItemDistance, ModelSearch.daycareDistanceAscendingComparator);
                    mAdapter.notifyDataSetChanged();
                }
            }, 100);
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
        mRecyclerView = rootView.findViewById(R.id.rv_search);
        mRecyclerView.setHasFixedSize(true);
        mLayoutmanager = new LinearLayoutManager(this.getContext());
        mAdapter = new AdapterSearch(searchItemDistance, this);
        mRecyclerView.setLayoutManager(mLayoutmanager);
        mRecyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(this::getCurrentlocation);
        etSearch = rootView.findViewById(R.id.et_search);
        etSearch.setOnKeyListener(this);
        mAdapter.updateData(searchItemDistance);
        if (searchItemDistance.isEmpty()) {
            tvDaycareNotFound.setVisibility(View.VISIBLE);
            tvPulltoRefresh.setVisibility(View.VISIBLE);
            ivPulltoRefresh.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    private void getDataStatic() {
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    String daycareName = dataSnapshot.child("profile").child("daycareName").getValue().toString();
                    String description = dataSnapshot.child("profile").child("description").getValue().toString();
                    String facilities = dataSnapshot.child("profile").child("facilities").getValue().toString();
                    String phoneNumber = dataSnapshot.child("profile").child("phoneNumber").getValue().toString();
                    String address = dataSnapshot.child("profile").child("address").getValue().toString();
                    String quota = dataSnapshot.child("profile").child("quota").getValue().toString();
                    String email = dataSnapshot.child("profile").child("email").getValue().toString();
                    String price = dataSnapshot.child("profile").child("price").getValue().toString();
                    String latitude = dataSnapshot.child("profile").child("latitude").getValue().toString();
                    String longitude = dataSnapshot.child("profile").child("longitude").getValue().toString();
                    String currentQuota = dataSnapshot.child("profile").child("currentQuota").getValue().toString();
                    String profilePic = dataSnapshot.child("profile").child("profilePic").getValue().toString();
                    String mediaUrl = dataSnapshot.child("profile").child("mediaUrl").getValue().toString();
                    daycareList.add(new ModelDaycare(daycareName, description, facilities, phoneNumber, price, address, email, latitude, longitude, quota, currentQuota, mediaUrl, profilePic));
                }
                getCurrentlocation();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String daycareName = dataSnapshot.child("profile").child("daycareName").getValue().toString();
                    String description = dataSnapshot.child("profile").child("description").getValue().toString();
                    String facilities = dataSnapshot.child("profile").child("facilities").getValue().toString();
                    String phoneNumber = dataSnapshot.child("profile").child("phoneNumber").getValue().toString();
                    String address = dataSnapshot.child("profile").child("address").getValue().toString();
                    String quota = dataSnapshot.child("profile").child("quota").getValue().toString();
                    String email = dataSnapshot.child("profile").child("email").getValue().toString();
                    String price = dataSnapshot.child("profile").child("price").getValue().toString();
                    String latitude = dataSnapshot.child("profile").child("latitude").getValue().toString();
                    String longitude = dataSnapshot.child("profile").child("longitude").getValue().toString();
                    String currentQuota = dataSnapshot.child("profile").child("currentQuota").getValue().toString();
                    String profilePic = dataSnapshot.child("profile").child("profilePic").getValue().toString();
                    String mediaUrl = dataSnapshot.child("profile").child("mediaUrl").getValue().toString();
                    daycareList.add(new ModelDaycare(daycareName, description, facilities, phoneNumber, price, address, email, latitude, longitude, quota, currentQuota, mediaUrl, profilePic));
                    mAdapter.notifyDataSetChanged();
                }
                getCurrentlocation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void closeKeyboard() {
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateList(double latitude, double longitude) {
        tvDaycareNotFound.setVisibility(View.INVISIBLE);
        tvPulltoRefresh.setVisibility(View.INVISIBLE);
        ivPulltoRefresh.setVisibility(View.INVISIBLE);

        for (ModelDaycare a : daycareList) {
            Double b = haversine(Double.parseDouble(a.getLatitude()), Double.parseDouble(a.getLongitude()), latitude, longitude);
            String c = String.format("%,.2f", b);
            searchItemDistance.add(new ModelSearch(a.getDaycareName(),
                    a.getPrice(),
                    c,
                    a.getProfilePic(),
                    a.getDescription(),
                    a.getFacilities(),
                    a.getPhoneNumber(),
                    a.getAddress(),
                    a.getEmail(),
                    a.getLatitude(),
                    a.getLongitude(),
                    a.getQuota(),
                    a.getCurrentQuota(),
                    a.getMediaUrl()));
        }
        mAdapter.updateData(searchItemDistance);
        Collections.sort(searchItemDistance, ModelSearch.daycareDistanceAscendingComparator);
//        Collections.sort(daycareList, ModelDaycare.daycareDistanceAscendingComparator);
        mAdapter.notifyDataSetChanged();
    }

    ArrayList<ModelSearch> filteredList = new ArrayList<>();

    private void filter(String textSearch) {
        filteredList.clear();
        for (ModelSearch item : searchItemDistance) {
            if (item.getDaycareName().toLowerCase().contains(textSearch.toLowerCase(Locale.ROOT))) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            tvDaycareNotFound.setVisibility(View.VISIBLE);
            tvPulltoRefresh.setVisibility(View.VISIBLE);
            ivPulltoRefresh.setVisibility(View.VISIBLE);
        } else {
            tvDaycareNotFound.setVisibility(View.INVISIBLE);
            tvPulltoRefresh.setVisibility(View.INVISIBLE);
            ivPulltoRefresh.setVisibility(View.INVISIBLE);
        }
        if(textSearch==""){
            getDataStatic();
        }
//        searchItemDistance=filteredList;
        mAdapter.updateData(filteredList);
        mAdapter.notifyDataSetChanged();
    }

    static double haversine(double lat1, double lon1,
                            double lat2, double lon2) {

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getCurrentlocation();
        } else {
            Toast.makeText(this.getContext(), "Permision Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentlocation() {
        searchItemDistance.clear();

        locationManager = (LocationManager) getContext().getSystemService(
                Context.LOCATION_SERVICE
        );
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();
                        updateList(currentLatitude, currentLongitude);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                currentLatitude = location1.getLatitude();
                                currentLongitude = location1.getLongitude();
                                updateList(currentLatitude, currentLongitude);
                                mAdapter.notifyDataSetChanged();
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        Log.d("testLatitude", String.valueOf(currentLongitude));
        Log.d("testDaycareList", String.valueOf(daycareList.size()));
        mAdapter.updateData(searchItemDistance);
        swipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                (i == KeyEvent.KEYCODE_ENTER)) {
            filter(etSearch.getText().toString());

            closeKeyboard();
            return true;
        }
        return false;
    }

    @Override
    public void OnItemClick(int position) {
        if (filteredList.size()>=1){
            Intent intent = new Intent(this.getContext(), ActivityDaycareInfo.class);
            intent.putExtra("daycareName", filteredList.get(position).getDaycareName());
            intent.putExtra("description", filteredList.get(position).getDescription());
            intent.putExtra("facilities", filteredList.get(position).getFacilities());
            intent.putExtra("email", filteredList.get(position).getEmail());
            intent.putExtra("phoneNumber", filteredList.get(position).getPhoneNumber());
            intent.putExtra("price", filteredList.get(position).getDaycarePrice());
            intent.putExtra("quota", filteredList.get(position).getQuota());
            intent.putExtra("currentQuota", filteredList.get(position).getCurrentQuota());
            intent.putExtra("mediaUrl", filteredList.get(position).getMediaUrl());
            intent.putExtra("latitude", filteredList.get(position).getLatitude());
            intent.putExtra("longitude", filteredList.get(position).getLongitude());
            startActivity(intent);
        }else{
            Intent intent = new Intent(this.getContext(), ActivityDaycareInfo.class);
            intent.putExtra("daycareName", searchItemDistance.get(position).getDaycareName());
            intent.putExtra("description", searchItemDistance.get(position).getDescription());
            intent.putExtra("facilities", searchItemDistance.get(position).getFacilities());
            intent.putExtra("email", searchItemDistance.get(position).getEmail());
            intent.putExtra("phoneNumber", searchItemDistance.get(position).getPhoneNumber());
            intent.putExtra("price", searchItemDistance.get(position).getDaycarePrice());
            intent.putExtra("quota", searchItemDistance.get(position).getQuota());
            intent.putExtra("currentQuota", searchItemDistance.get(position).getCurrentQuota());
            intent.putExtra("mediaUrl", searchItemDistance.get(position).getMediaUrl());
            intent.putExtra("latitude", searchItemDistance.get(position).getLatitude());
            intent.putExtra("longitude", searchItemDistance.get(position).getLongitude());
            startActivity(intent);
        }
    }

    @Override
    public void OnItemClick2(int position) {

    }

    @Override
    public void OnItemClick3(int position) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getItemAtPosition(i).toString()) {
            case "Sortir jarak dekat ke jauh":
                Collections.sort(searchItemDistance, ModelSearch.daycareDistanceAscendingComparator);
                Collections.sort(filteredList, ModelSearch.daycareDistanceAscendingComparator);
                mAdapter.notifyDataSetChanged();
//                Toast.makeText(this.getContext(), adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                break;
            case "Sortir jarak jauh ke dekat":
                Collections.sort(searchItemDistance, ModelSearch.daycareDistanceDescendingComparator);
                Collections.sort(filteredList, ModelSearch.daycareDistanceDescendingComparator);
                mAdapter.notifyDataSetChanged();
//                Toast.makeText(this.getContext(), adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                break;
            case "Sortir harga kecil ke besar":
                Collections.sort(searchItemDistance, ModelSearch.daycareAZComparator);
                Collections.sort(filteredList, ModelSearch.daycareAZComparator);
                mAdapter.notifyDataSetChanged();
//                Toast.makeText(this.getContext(), adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                break;
            case "Sortir harga besar ke kecil":
                Collections.sort(searchItemDistance, ModelSearch.daycareZAComparator);
                Collections.sort(filteredList, ModelSearch.daycareZAComparator);
                mAdapter.notifyDataSetChanged();
//                Toast.makeText(this.getContext(), adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Collections.sort(searchItemDistance, ModelSearch.daycareDistanceAscendingComparator);
        mAdapter.notifyDataSetChanged();
    }
}
