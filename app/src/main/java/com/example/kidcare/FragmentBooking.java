package com.example.kidcare;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kidcare.Adapter.AdapterBooking;
import com.example.kidcare.Model.ModelBooking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class FragmentBooking extends Fragment implements IRecyclerView {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("user");
    DatabaseReference bookingReference = FirebaseDatabase.getInstance().getReference("daycare");
    TextView userName;
    ArrayList<ModelBooking> bookingList;
    AdapterBooking mAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutmanager;
    Dialog dialog;
    TextView tvBookingNotfound;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    SwipeRefreshLayout swipeRefreshLayout;

    String daycareIDclicked;
    String currentChildClicked;
    String currentBookingID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.fragment_booking, container, false);
//        status = rootView.findViewById(R.id.tv_status_boooking);
        bookingList = new ArrayList<>();
        mRecyclerView = rootView.findViewById(R.id.rv_booking);
        mRecyclerView.setHasFixedSize(true);
        mLayoutmanager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutmanager);
        tvBookingNotfound = rootView.findViewById(R.id.tv_booking_not_found);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshBooking);
        swipeRefreshLayout.setOnRefreshListener(this::getData);
        getData();
        if (bookingList.isEmpty()) {
            tvBookingNotfound.setVisibility(View.VISIBLE);
        } else {
            tvBookingNotfound.setVisibility(View.INVISIBLE);
        }
//
        Collections.sort(bookingList,ModelBooking.bookingZAComparator);
        mAdapter = new AdapterBooking(bookingList, this);
        mAdapter.updateData(bookingList);
        mRecyclerView.setAdapter(mAdapter);


        return rootView;
    }

    @Override
    public void OnItemClick(int position) {
        showCustomDialog(position);
    }

    @Override
    public void OnItemClick2(int position) {
        daycareIDclicked=bookingList.get(position).getDaycareID();
        currentChildClicked=bookingList.get(position).getChildName();
        currentBookingID = bookingList.get(position).getBookingID();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1000);
    }

    @Override
    public void OnItemClick3(int position) {
        daycareIDclicked=bookingList.get(position).getDaycareID();
        currentChildClicked=bookingList.get(position).getChildName();
        currentBookingID = bookingList.get(position).getBookingID();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        bookingReference.child(daycareIDclicked).child("booking").child(currentBookingID).child("date").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                bookingReference.child(daycareIDclicked).child("booking").child(currentBookingID).child("media").child(task.getResult().getValue().toString()).child("imageUrl").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        String url = task.getResult().getValue().toString();
                        Log.d("testgambarr", url);
                        confirmationDialog(url);
                    }
                });
            }
        });


    }

    void showCustomDialog(int position) {
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_confirm_delete);
        TextView tvConfirm = dialog.findViewById(R.id.tv_confirm);

        Button submitButton = dialog.findViewById(R.id.bt_confirm_delete);
        Button cancelButton = dialog.findViewById(R.id.bt_cancel_delete);
        bookingReference.child(bookingList.get(position).getDaycareID()).child("booking").child(bookingList.get(position).getBookingID()).child("status").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().getValue().toString().equals("waiting")) {
                    tvConfirm.setText("Apakah anda yakin untuk membatalkan?");
                } else {
                    tvConfirm.setText("Apakah anda yakin untuk keluar dari daycare?");

                }
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.notifyDataSetChanged();
                bookingReference.child(bookingList.get(position).getDaycareID()).child("booking").child(bookingList.get(position).getBookingID()).child("status").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.getResult().getValue().toString().equals("waiting")) {
                            bookingReference.child(bookingList.get(position).getDaycareID()).child("booking").child(bookingList.get(position).getBookingID()).child("status").setValue("request cancel");
                            mAdapter.notifyDataSetChanged();
                        }
                        if (task.getResult().getValue().toString().equals("accepted")) {
                            bookingReference.child(bookingList.get(position).getDaycareID()).child("booking").child(bookingList.get(position).getBookingID()).child("status").setValue("request cancel");
                            mAdapter.notifyDataSetChanged();

                        }
                        if (task.getResult().getValue().toString().equals("finished")) {
                            bookingReference.child(bookingList.get(position).getDaycareID()).child("booking").child(bookingList.get(position).getBookingID()).child("status").setValue("request quit");
                            mAdapter.notifyDataSetChanged();

                        }
                    }
                });
                dialog.dismiss();
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

    void getData() {
        bookingList.clear();
        bookingReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot ds : task.getResult().getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        if (ds1.getKey().equals("booking")) {
                            for (DataSnapshot ds2 : ds1.getChildren()) {
                                if (ds2.child("UID").getValue(String.class).equals(user.getUid())) {
                                    bookingList.add(new ModelBooking(ds.child("profile").child("daycareName").getValue().toString(),
                                            ds2.child("firstName").getValue().toString() +
                                                    " " + ds2.child("lastName").getValue().toString(),
                                            ds2.child("date").getValue().toString(),
                                            ds2.child("status").getValue(String.class),
                                            ds.child("profile").child("email").getValue(String.class).replace(".", "").replace("@", ""),
                                            ds2.getKey(), ds2.child("price").getValue().toString(),ds2.child("accountNumber").getValue().toString()));
                                    tvBookingNotfound.setVisibility(View.INVISIBLE);
                                    Collections.sort(bookingList,ModelBooking.bookingZAComparator);
                                    mAdapter.notifyDataSetChanged();

                                }
                            }
                        }
                    }
                }
            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
//            confirmationDialog(imageUri,currentBookingID);
            uploadImageToFirebase(imageUri,currentBookingID);
            Toast.makeText(this.getContext(), "Bukti pembayaran berhasil di upload. Silahkan menunggu bukti pembayaran di verifikasi pihak daycare", Toast.LENGTH_LONG).show();
        }
    }


    private void uploadImageToFirebase(Uri imageUri,String bookingID) {
//        confirmationDialog(imageUri);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final StorageReference fileRef = storageRef.child("daycare/" + daycareIDclicked+"/"+user.getUid()+"/"+dtf.format(now));
        fileRef.putFile(imageUri).addOnSuccessListener((OnSuccessListener<? super UploadTask.TaskSnapshot>) (taskSnapshot) -> {
            fileRef.getDownloadUrl().addOnSuccessListener((OnSuccessListener<? super Uri>) (uri) -> {
                String url = uri.toString();
                bookingReference.child(daycareIDclicked).child("booking").child(bookingID).child("media").child(dtf2.format(now)).child("imageUrl").setValue(url);
                bookingReference.child(daycareIDclicked).child("booking").child(bookingID).child("media").child(dtf2.format(now)).child("date").setValue(dtf2.format(now));
                bookingReference.child(daycareIDclicked).child("booking").child(bookingID).child("status").setValue("uploaded");
//                dbref.child(user.getUid()).child("profilePict").setValue(url);
            });
        });
    }
    void confirmationDialog(String imageUri) {
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_confirm_upload);
        ImageView ivConfirmUpload = dialog.findViewById(R.id.iv_confirmUpload);
        Picasso.get().load(imageUri).into(ivConfirmUpload);
        ivConfirmUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),ActivityImageView.class);
                intent.putExtra("url",imageUri);
                startActivity(intent);
            }
        });
        Button submitButton = dialog.findViewById(R.id.bt_confirm);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1000);
            }
        });
        dialog.show();
    }

}