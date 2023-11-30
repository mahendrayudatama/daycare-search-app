package com.example.kidcare.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidcare.IRecyclerView;
import com.example.kidcare.Model.ModelDate;
import com.example.kidcare.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterDate extends RecyclerView.Adapter<AdapterDate.ViewHolder> {


    List<String> day;
    List<String> date;



    public AdapterDate(List<String> day, List<String> date) {
        this.day = day;
        this.date = date;
    }
    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_date, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull AdapterDate.ViewHolder holder, int position) {
        holder.day.setText(day.get(position));
        holder.date.setText(date.get(position));
        holder.view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        holder.view.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_date);
                        break;
                    case 1:
                        holder.view.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_date);
                        break;
                    case 2:
                        holder.view.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_date);
                        break;
                    case 3:
                        holder.view.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_date);
                        break;
                    case 4:
                        holder.view.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_date);
                        break;
                    case 5:
                        holder.view.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_date);
                        break;
                    default:
                        holder.view.findViewById(R.id.cardViewDate).setBackgroundResource(R.drawable.viewcard_notfocused);
                        break;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return day.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView day, date;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            view = itemView;
        }
    }

}