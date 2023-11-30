package com.example.kidcare;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FragmentProfile extends Fragment implements View.OnClickListener, TextWatcher {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbref = database.getReference("user");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    String currentUser;
    String currentChild;
    TextView email;
    TextView phoneNumber;
    TextView name;
    ArrayList<String> childName;
    AutoCompleteTextView selectChild;
    Dialog dialog;
    ArrayAdapter childListAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        CardView cardViewProfile = rootView.findViewById(R.id.cardViewProfile);
        cardViewProfile.setBackgroundResource(R.drawable.top_rounded);

        rootView.findViewById(R.id.bt_logout).setOnClickListener(this);
        rootView.findViewById(R.id.bt_add_child).setOnClickListener(this);
        rootView.findViewById(R.id.bt_edit_profile).setOnClickListener(this);
        rootView.findViewById(R.id.bt_edit_child).setOnClickListener(this);
        rootView.findViewById(R.id.bt_delete_child).setOnClickListener(this);

        name = rootView.findViewById(R.id.tv_name);
        phoneNumber = rootView.findViewById(R.id.tv_phoneNumber);
        email = rootView.findViewById(R.id.tv_email);
        ImageView iv_profile = rootView.findViewById(R.id.im_profile);

        StorageReference profileRef = storageRef.child("user/" + user.getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(
                (OnSuccessListener<? super Uri>) (uri) -> {
                    Picasso.get().load(uri).into(iv_profile);
                });
        selectChild = rootView.findViewById(R.id.et_selectChild);

        selectChild.addTextChangedListener(this);
        getChildName();
        childName = new ArrayList<>();
        childListAdapter = new ArrayAdapter(requireContext(), R.layout.select_child_dropdown_item, childName);
        selectChild.setAdapter(childListAdapter);
        setProfileData();
        return rootView;
    }

    private void setProfileData() {
        try {
            if (user.getEmail() != null) {
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if (ds.child("email").getValue().equals(user.getEmail())) {
                                name.setText(ds.child("firstName").getValue(String.class) + " " + ds.child("lastName").getValue(String.class));
                                phoneNumber.setText(ds.child("phoneNumber").getValue(String.class));
                                email.setText(ds.child("email").getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            if (name != null) {
                name.setText(currentUser);
            }
        } catch (Exception e) {

        }
    }

    void getChildName() {
        childName = new ArrayList<>();

        if (user.getEmail() != null) {
            dbref.child(user.getUid()).child("child").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    childName.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        childName.add(ds.child("firstName").getValue().toString() + " " + ds.child("lastName").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_logout:
                showCustomDialogLogout();
                break;
            case R.id.bt_add_child:
                Fragment fragment = new FragmentRegisterChild();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                break;
            case R.id.bt_edit_profile:
                Intent intent = new Intent(view.getContext(), ActivityEditProfile.class);
                String[] a = name.getText().toString().split(" ");
                intent.putExtra("firstName", a[0]);
                intent.putExtra("lastName", a[1]);
                intent.putExtra("phoneNumber", phoneNumber.getText().toString());
                intent.putExtra("email", email.getText().toString());
                startActivity(intent);
                break;
            case R.id.bt_edit_child:
                if (selectChild.getText().toString().isEmpty()) {
                    selectChild.requestFocus();
                    selectChild.setError("Pilih salah satu anak", null);
                } else {
                    fragment = new FragmentEditChildData();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

                }
                break;
            case R.id.bt_delete_child:
                if (selectChild.getText().toString().isEmpty()) {
                    selectChild.requestFocus();
                    selectChild.setError("Pilih salah satu anak", null);
                } else {
                    dbref.child(user.getUid()).child("child").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            ArrayList<String> childList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                                childList.add(dataSnapshot.getKey());
                            }
                            if (childList.size() == 1) {
                                Toast.makeText(getContext(), "Jumlah anak minimal satu", Toast.LENGTH_SHORT).show();
                            } else if (childList.size() > 1) {
                                showCustomDialog();
                            }

                        }
                    });
                }
                break;
            case R.id.bt_confirm_delete:
                dbref.child(user.getUid()).child("child").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        ArrayList<String> childList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                            childList.add(dataSnapshot.getKey());
                        }
                        dbref.child(user.getUid()).child("currentChild").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                String childID = task.getResult().getValue().toString();
                                dbref.child(user.getUid()).child("child").child(task.getResult().getValue().toString()).child("status").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.getResult().getValue().toString().equals("not registered")) {
                                            childList.remove(childID);
                                            childList.get(0);
                                            dbref.child(user.getUid()).child("currentChild").setValue(childList.get(0));
                                            dbref.child(user.getUid()).child("child").child(childID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    childName = new ArrayList<>();
                                                    getChildName();
                                                    childListAdapter = new ArrayAdapter(requireContext(), R.layout.select_child_dropdown_item, childName);
                                                    childListAdapter.notifyDataSetChanged();
                                                    Toast.makeText(getContext(), "Data anak berhasil dihapus", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getContext(), "Data anak yang terdaftar tidak bisa dihapus", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });

                        dialog.dismiss();
                    }
                });

                break;
            case R.id.bt_cancel_delete:
                dialog.dismiss();
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Toast.makeText(getContext(), charSequence + " telah terpilih, Silahkan menuju halaman daycare", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (selectChild.getText().toString().equals("")) {
            selectChild.setError("Pilih Anak!");
        } else {
            dbref.child(user.getUid()).child("child").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        if ((dataSnapshot.child("firstName").getValue().toString().replace(" ","") + dataSnapshot.child("lastName").getValue().toString().replace(" ","")).equals(selectChild.getText().toString().replace(" ",""))) {
                            dbref.child(user.getUid()).child("currentChild").setValue(dataSnapshot.getKey());
                        }
                    }
                }
            });
        }
    }

    void showCustomDialog() {
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_confirm_delete);
        TextView tvConfirm = dialog.findViewById(R.id.tv_confirm);
                tvConfirm.setText("Apakah anda yakin untuk menghapus data " + selectChild.getText() + "?");
        dbref.child(user.getUid()).child("currentChild").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

            }
        });
        Button submitButton = dialog.findViewById(R.id.bt_confirm_delete);
        Button cancelButton = dialog.findViewById(R.id.bt_cancel_delete);
        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        dialog.show();
    }

    void showCustomDialogLogout() {
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_confirm_delete);
        TextView tvConfirm = dialog.findViewById(R.id.tv_confirm);
        tvConfirm.setText("Apakah anda yakin untuk keluar?");
        Button submitButton = dialog.findViewById(R.id.bt_confirm_delete);
        Button cancelButton = dialog.findViewById(R.id.bt_cancel_delete);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FirebaseAuth.getInstance().signOut();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), "Berhasil keluar", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), ActivityMain.class));
            }
        });
        cancelButton.setOnClickListener(this);
        dialog.show();
    }
}
