package com.example.kidcare;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidcare.Adapter.AdapterActivity;
import com.example.kidcare.Adapter.AdapterDate;
import com.example.kidcare.Adapter.AdapterImageView;
import com.example.kidcare.Model.ModelActivity;
import com.example.kidcare.Model.ModelDate;
import com.example.kidcare.Model.ModelUrl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentActivity extends Fragment implements View.OnClickListener {
    FirebaseAuth user = FirebaseAuth.getInstance();
    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("user");
    DatabaseReference daycareReference = FirebaseDatabase.getInstance().getReference("daycare");
    RecyclerView noteList;
    RecyclerView dateRecyclerView;
    RecyclerView recyclerViewImageView;
    AdapterActivity adapterActivity;
    AdapterDate adapterDate;
    AdapterImageView adapterImageView;
    List<String> date = new ArrayList<>();
    List<String> day = new ArrayList<>();



    ArrayList<ArrayList<ModelActivity>> localList;
    ArrayList<ModelActivity> adapterList;
    ArrayList<String> listDate;
    private Calendar calendar;
    private SimpleDateFormat dateFormatDay;
    private SimpleDateFormat dateFormatDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_activity, container, false);
        ViewGroup inView = (ViewGroup) inflater.inflate(R.layout.card_activity, container, false);
        recyclerViewImageView = inView.findViewById(R.id.rv_imageview);
        noteList = rootView.findViewById(R.id.noteList);
        dateRecyclerView = rootView.findViewById(R.id.dateList);
        getData();
        recyclerViewImageView.setOnClickListener(this);
        noteList.setOnClickListener(this);
        dateRecyclerView.setOnClickListener(this);
        noteList.setHasFixedSize(true);
        calendar = Calendar.getInstance();
        dateFormatDay = new SimpleDateFormat("EEE");
        dateFormatDate = new SimpleDateFormat("yyyy-MM-dd");

        localList = new ArrayList<>();
        adapterList = new ArrayList<>();
        listDate = new ArrayList<>();

        adapterActivity = new AdapterActivity(this.getContext(), adapterList);
        adapterDate = new AdapterDate(day, date);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this.getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 4);

        dateRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        switch (position) {
                            case 0:
                                try {
                                    for (int childCount = dateRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                                        AdapterDate.ViewHolder holder = (AdapterDate.ViewHolder) dateRecyclerView.getChildViewHolder(dateRecyclerView.getChildAt(i));
                                        holder.itemView.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_notfocused);
                                    }
                                    adapterActivity.changeList(localList.get(0));

                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                try {
                                    for (int childCount = dateRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                                        AdapterDate.ViewHolder holder = (AdapterDate.ViewHolder) dateRecyclerView.getChildViewHolder(dateRecyclerView.getChildAt(i));
                                        holder.itemView.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_notfocused);
                                    }
                                    adapterActivity.changeList(localList.get(1));

                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                try {
                                    for (int childCount = dateRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                                        AdapterDate.ViewHolder holder = (AdapterDate.ViewHolder) dateRecyclerView.getChildViewHolder(dateRecyclerView.getChildAt(i));
                                        holder.itemView.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_notfocused);
                                    }
                                    adapterActivity.changeList(localList.get(2));

                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 3:
                                try {
                                    for (int childCount = dateRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                                        AdapterDate.ViewHolder holder = (AdapterDate.ViewHolder) dateRecyclerView.getChildViewHolder(dateRecyclerView.getChildAt(i));
                                        holder.itemView.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_notfocused);
                                    }
                                    adapterActivity.changeList(localList.get(3));

                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 4:
                                try {
                                    for (int childCount = dateRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                                        AdapterDate.ViewHolder holder = (AdapterDate.ViewHolder) dateRecyclerView.getChildViewHolder(dateRecyclerView.getChildAt(i));
                                        holder.itemView.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_notfocused);
                                    }
                                    adapterActivity.changeList(localList.get(4));

                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 5:
                                try {
                                    for (int childCount = dateRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                                        AdapterDate.ViewHolder holder = (AdapterDate.ViewHolder) dateRecyclerView.getChildViewHolder(dateRecyclerView.getChildAt(i));
                                        holder.itemView.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_notfocused);
                                    }
                                    adapterActivity.changeList(localList.get(5));

                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                try {
                                    for (int childCount = dateRecyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                                        AdapterDate.ViewHolder holder = (AdapterDate.ViewHolder) dateRecyclerView.getChildViewHolder(dateRecyclerView.getChildAt(i));
                                        holder.itemView.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_notfocused);
                                    }
                                    adapterActivity.changeList(localList.get(0));

                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                })
        );


        noteList.setLayoutManager(linearLayoutManager);
        noteList.setAdapter(adapterActivity);
        dateRecyclerView.setLayoutManager(linearLayoutManager1);
        dateRecyclerView.setAdapter(adapterDate);
        recyclerViewImageView.setLayoutManager(linearLayoutManager2);
        recyclerViewImageView.setAdapter(adapterImageView);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dateList:

                break;
            case R.id.noteList:

                break;
        }
    }
    void getData(){
        userReference.child(user.getUid()).child("currentChild").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                userReference.child(user.getUid()).child("child").child(task.getResult().getValue().toString()).child("daycareID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        daycareReference.child(task.getResult().getValue(String.class)).child("activity").orderByKey().limitToLast(6).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {

                                    Log.d("testgetKey", dataSnapshot.getKey().substring(8, 10));
                                    date.add(dataSnapshot.getKey().substring(8, 10));
                                    Date date1 = null;
                                    try {
                                        date1 = dateFormatDate.parse(dataSnapshot.getKey().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    day.add(dateFormatDay.format(date1));
                                    ArrayList<ModelActivity> dt = new ArrayList<>();
                                    listDate.add(dataSnapshot.getKey().substring(8, 10));
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                        ModelActivity activityData = data.getValue(ModelActivity.class);
                                        String activity = data.child("activity").getValue().toString();
                                        String time = data.child("time").getValue().toString();
                                        String noted = data.child("noted").getValue().toString();
                                        String status = data.child("status").getValue().toString();
                                        String urlVideo = data.child("videoUrl").getValue().toString();
                                        String urlImage = data.child("imageUrl").getValue().toString();
                                        ArrayList<ModelUrl> modelImageUrlList = new ArrayList<>();
                                        ArrayList<ModelUrl> modelVideoUrlList = new ArrayList<>();
                                        if (urlVideo.isEmpty()!=true) {
                                            String[] videoUrl = urlVideo.split(",");
                                            for (String urlSplitted : videoUrl) {
                                                modelVideoUrlList.add(new ModelUrl(urlSplitted));
                                                Log.d("urlsplitted2", "onDataChange: " + modelVideoUrlList.toArray().length+urlSplitted);
                                            }
                                        }
                                        if (urlImage.isEmpty()!=true) {
                                            String[] url = urlImage.split(",");
                                            for (String urlSplitted : url) {
                                                modelImageUrlList.add(new ModelUrl(urlSplitted));
                                                Log.d("urlsplitted", "onDataChange: " + modelImageUrlList.toArray().length+ urlSplitted);
                                            }
                                        }
                                        dt.add(new ModelActivity(activity, time, noted, status, urlImage, urlVideo, modelImageUrlList, modelVideoUrlList));

                                    }
                                    localList.add(dt);
                                    adapterDate.notifyDataSetChanged();
                                }
                                adapterActivity.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });
    }
}
//        userReference.child(user.getUid()).child("currentChild").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userReference.child(user.getUid()).child("child").child(snapshot.getValue().toString()).child("daycareID").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        daycareReference.child(snapshot.getValue(String.class)).child("activity").orderByKey().limitToLast(6).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                                    Log.d("testgetKey", dataSnapshot.getKey().substring(8, 10));
//                                    date.add(dataSnapshot.getKey().substring(8, 10));
//                                    Date date1 = null;
//                                    try {
//                                        date1 = dateFormatDate.parse(dataSnapshot.getKey().toString());
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//                                    day.add(dateFormatDay.format(date1));
//                                    ArrayList<ModelActivity> dt = new ArrayList<>();
//                                    listDate.add(dataSnapshot.getKey().substring(8, 10));
//                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
////                                        ModelActivity activityData = data.getValue(ModelActivity.class);
//                                        String activity = data.child("activity").getValue().toString();
//                                        String time = data.child("time").getValue().toString();
//                                        String noted = data.child("noted").getValue().toString();
//                                        String status = data.child("status").getValue().toString();
//                                        String urlVideo = data.child("videoUrl").getValue().toString();
//                                        String urlImage = data.child("imageUrl").getValue().toString();
//                                        ArrayList<ModelUrl> modelImageUrlList = new ArrayList<>();
//                                        ArrayList<ModelUrl> modelVideoUrlList = new ArrayList<>();
//                                        if (urlVideo.isEmpty()!=true) {
//                                            String[] videoUrl = urlVideo.split(",");
//                                            for (String urlSplitted : videoUrl) {
//                                                modelVideoUrlList.add(new ModelUrl(urlSplitted));
//                                                Log.d("urlsplitted2", "onDataChange: " + modelVideoUrlList.toArray().length+urlSplitted);
//                                            }
//                                        }
//                                        if (urlImage.isEmpty()!=true) {
//                                            String[] url = urlImage.split(",");
//                                            for (String urlSplitted : url) {
//                                                modelImageUrlList.add(new ModelUrl(urlSplitted));
//                                                Log.d("urlsplitted", "onDataChange: " + modelImageUrlList.toArray().length+ urlSplitted);
//                                            }
//                                        }
//                                        dt.add(new ModelActivity(activity, time, noted, status, urlImage, urlVideo, modelImageUrlList, modelVideoUrlList));
//
//                                    }
//                                    localList.add(dt);
//                                    adapterDate.notifyDataSetChanged();
//                                }
//                                adapterActivity.notifyDataSetChanged();
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

