package com.example.kidcare.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidcare.IRecyclerView;
import com.example.kidcare.Model.ModelInvoice;
import com.example.kidcare.R;

import java.util.ArrayList;

public class AdapterInvoice extends RecyclerView.Adapter<AdapterInvoice.ViewHolder> {

    ArrayList<ModelInvoice> invoiceList;
    private IRecyclerView mIRecyclerView;

    public AdapterInvoice(ArrayList<ModelInvoice> invoiceList,IRecyclerView iRecyclerView) {
        this.invoiceList = invoiceList;
        mIRecyclerView = iRecyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_invoice, parent, false);
        ViewHolder vh = new ViewHolder(v,mIRecyclerView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.invoiceDesc.setText(invoiceList.get(position).getInvoiceID());
        holder.date.setText(invoiceList.get(position).getDate());
        holder.invoiceDaycareName.setText(invoiceList.get(position).getDaycareName());

    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView invoiceDesc,date,invoiceDaycareName;
        public Button downloadInvoice;

        public ViewHolder(@NonNull View itemView,IRecyclerView iRecyclerView) {
            super(itemView);
            date = itemView.findViewById(R.id.tv_invoice_date);
            invoiceDesc = itemView.findViewById(R.id.tv_invoice_desc);
            invoiceDaycareName = itemView.findViewById(R.id.tv_invoice_daycare_name);
            downloadInvoice = itemView.findViewById(R.id.btn_download);
            downloadInvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iRecyclerView!=null){
                        int pos = getAdapterPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            iRecyclerView.OnItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
