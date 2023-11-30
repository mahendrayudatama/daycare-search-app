package com.example.kidcare.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidcare.IRecyclerView;
import com.example.kidcare.Model.ModelBooking;
import com.example.kidcare.Model.ModelSearch;
import com.example.kidcare.R;

import java.util.ArrayList;

public class AdapterBooking extends RecyclerView.Adapter<AdapterBooking.ViewHolder> {
    ArrayList<ModelBooking> bookingList;
    private IRecyclerView mIRecyclerView;

    public AdapterBooking(ArrayList<ModelBooking> bookingList, IRecyclerView iRecyclerView) {
        this.bookingList = bookingList;
        this.mIRecyclerView = iRecyclerView;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_booking, parent, false);
        ViewHolder vh = new ViewHolder(v, mIRecyclerView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBooking.ViewHolder holder, int position) {
        ModelBooking currentItem = bookingList.get(position);

        holder.daycareName.setText(currentItem.getDaycareName());
        holder.childName.setText(currentItem.getChildName());
        holder.bookingDate.setText(currentItem.getDate());


        if (currentItem.getStatus().equals("waiting")){
            holder.status.setText("Menunggu");
            holder.status.setBackgroundColor(Color.parseColor("#B3e67e22"));
            holder.button.setText("Batalkan");
            holder.button.setEnabled(true);
        }
        if (currentItem.getStatus().equals("accepted")){
            holder.status.setText("Diterima");
            holder.status.setBackgroundColor(Color.parseColor("#B32ecc71"));
            holder.relativeLayout_change.setBackgroundResource(R.drawable.viewcard_green);
            holder.button.setText("Batalkan");
            holder.button.setEnabled(true);
            holder.uploadButton.setVisibility(View.VISIBLE);
            holder.desc.setText("Silahkan melakukan pembayaran dengan cara transfer sejumlah Rp."+currentItem.getPrice()+" ke rek "+currentItem.getAccountNumber()+
                    " dan mengupload bukti pembayaran atau dengan cara datang ke tempat penitipan anak secara langsung.");
            holder.desc.setVisibility(View.VISIBLE);
        }
        if (currentItem.getStatus().equals("uploaded")){
            holder.status.setText("Menuggu verifikasi dari pihak TPA");
            holder.status.setBackgroundColor(Color.parseColor("#B32ecc71"));
            holder.relativeLayout_change.setBackgroundResource(R.drawable.viewcard_green);
            holder.button.setText("Batalkan");
            holder.button.setEnabled(true);
            holder.viewImage.setVisibility(View.VISIBLE);
//            holder.desc.setText("Silahkan melakukan pembayaran dengan cara transfer sejumlah Rp."+currentItem.getPrice()+" ke rek "+currentItem.getAccountNumber()+
//                    " dan mengupload bukti pembayaran atau dengan cara datang ke tempat penitipan anak secara langsung.");
//            holder.desc.setVisibility(View.VISIBLE);
        }
        if (currentItem.getStatus().equals("rejected")){
            holder.status.setText("Ditolak");
            holder.status.setBackgroundColor(Color.parseColor("#B3e74c3c"));
            holder.relativeLayout_change.setBackgroundResource(R.drawable.viewcard_red);
            holder.button.setText("");
            holder.button.setEnabled(false);
            holder.button.setVisibility(View.INVISIBLE);
        }
        if (currentItem.getStatus().equals("finished")){
            holder.status.setText("Selesai");
            holder.status.setBackgroundColor(Color.parseColor("#B32ecc71"));
            holder.relativeLayout_change.setBackgroundResource(R.drawable.viewcard_green);
            holder.button.setText("Permintaan Keluar");
            holder.button.setEnabled(true);
        }
        if (currentItem.getStatus().equals("request cancel")){
            holder.status.setText("Permintaan Pembatalan Diproses");
            holder.status.setBackgroundColor(Color.parseColor("#B3e74c3c"));
            holder.relativeLayout_change.setBackgroundResource(R.drawable.viewcard_red);
            holder.button.setText("");
            holder.button.setEnabled(false);
        }
        if (currentItem.getStatus().equals("cancelled")){
            holder.status.setText("Permintaan Pembatalan Disetujui");
            holder.status.setBackgroundColor(Color.parseColor("#B3e74c3c"));
            holder.relativeLayout_change.setBackgroundResource(R.drawable.viewcard_red);
            holder.button.setText("");
            holder.button.setEnabled(false);
            holder.button.setVisibility(View.GONE);
        }
        if (currentItem.getStatus().equals("request quit")){
            holder.status.setText("Permintaan Keluar diproses");
            holder.status.setBackgroundColor(Color.parseColor("#B3e74c3c"));
            holder.relativeLayout_change.setBackgroundResource(R.drawable.viewcard_red);
            holder.button.setText("");
            holder.button.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return  bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView daycareName;
        public TextView childName;
        public TextView bookingDate;
        public TextView status;
        public TextView desc;
        public Button button;
        public Button uploadButton;
        public Button viewImage;
        public RelativeLayout relativeLayout_change;
        AdapterSearch.OnCardListener onCardListener;

        public ViewHolder(@NonNull View itemView, IRecyclerView iRecyclerView) {
            super(itemView);
            daycareName = itemView.findViewById(R.id.tv_bookingDaycareName);
            childName = itemView.findViewById(R.id.tv_bookingChildName);
            bookingDate = itemView.findViewById(R.id.tv_bookingDate);
            status = itemView.findViewById(R.id.tv_status_boooking);
            relativeLayout_change = itemView.findViewById(R.id.rl_view_card_list);
            button = itemView.findViewById(R.id.btn_booking);
            uploadButton = itemView.findViewById(R.id.btn_upload_transaction);
            desc = itemView.findViewById(R.id.tv_booking_desc);
            viewImage = itemView.findViewById(R.id.btn_view_upload);

            this.onCardListener = onCardListener;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iRecyclerView != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            iRecyclerView.OnItemClick(pos);

                        }
                    }
                }
            });
            uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        iRecyclerView.OnItemClick2(pos);
                    }
                }
            });
            viewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        iRecyclerView.OnItemClick3(pos);
                    }
                }
            });
        }

    }

    public void updateData(ArrayList<ModelBooking> mItems) {
        this.bookingList = mItems;
        notifyDataSetChanged();
    }

    public interface OnCardListener {
        void onCardClick(int position);
    }
}
