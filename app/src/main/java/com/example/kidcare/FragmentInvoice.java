package com.example.kidcare;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidcare.Adapter.AdapterInvoice;
import com.example.kidcare.Model.ModelInvoice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentInvoice extends Fragment implements IRecyclerView {
    ArrayList<ModelInvoice> invoiceList = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbrefDaycare = database.getReference("daycare");
    DatabaseReference dbrefUser = database.getReference("user");
    RecyclerView rvInvoice;
    AdapterInvoice adapterInvoice;
    RecyclerView.LayoutManager mLayoutmanager;
    TextView invoiceNotFound;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_invoice, container, false);
        rvInvoice = rootView.findViewById(R.id.rv_invoice);
        invoiceNotFound = rootView.findViewById(R.id.tv_invoice_not_found);

        getData();
        adapterInvoice = new AdapterInvoice(invoiceList, this);
        rvInvoice.setAdapter(adapterInvoice);
        mLayoutmanager = new LinearLayoutManager(this.getContext());
        rvInvoice.setLayoutManager(mLayoutmanager);
        return rootView;
    }

    @Override
    public void OnItemClick(int position) {
        downloadFile(getContext(),"Invoice "+invoiceList.get(position).getInvoiceID(),".pdf",DIRECTORY_DOWNLOADS,invoiceList.get(position).getUrl());
    }

    @Override
    public void OnItemClick2(int position) {

    }

    @Override
    public void OnItemClick3(int position) {

    }

    public void downloadFile(Context context,String fileName,String fileExtension,String destination,String url){
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED).setDestinationInExternalFilesDir(context,destination,fileName+fileExtension);
        downloadManager.enqueue(request);
        Toast.makeText(context, "File diunduh", Toast.LENGTH_SHORT).show();
    }
    void getData(){
        dbrefUser.child(user.getUid()).child("invoice").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot dataSnapshot:task.getResult().getChildren()){
                    invoiceList.add(new ModelInvoice(dataSnapshot.child("invoiceUrl").getValue().toString(),
                            dataSnapshot.child("desc").getValue().toString(),
                            dataSnapshot.child("date").getValue().toString(),
                            dataSnapshot.child("daycareName").getValue().toString(),
                            dataSnapshot.child("invoiceID").getValue().toString()));
                    adapterInvoice.notifyDataSetChanged();
                    if (invoiceList.isEmpty()) {
                        invoiceNotFound.setVisibility(View.VISIBLE);
                    } else {
                        invoiceNotFound.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }
}
