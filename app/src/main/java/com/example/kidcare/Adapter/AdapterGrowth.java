package com.example.kidcare.Adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidcare.IRecyclerView;
import com.example.kidcare.Model.ModelGrowth;
import com.example.kidcare.Model.ModelGrowthList;
import com.example.kidcare.R;

import java.util.ArrayList;

public class AdapterGrowth extends RecyclerView.Adapter<AdapterGrowth.ViewHolder> implements View.OnClickListener {

    ArrayList<ModelGrowthList> growthList;
    private IRecyclerView mIRecyclerView;

    public AdapterGrowth(ArrayList<ModelGrowthList> growthList,IRecyclerView iRecyclerView) {

        this.growthList = growthList;
        this.mIRecyclerView = iRecyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_growth, parent, false);
        ViewHolder vh = new ViewHolder(v,mIRecyclerView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.childAge.setText(growthList.get(position).getAge());
        holder.growthDate.setText(growthList.get(position).getDate());
        holder.childHeight.setText(growthList.get(position).getHeight()+" cm");
        holder.childWeight.setText(growthList.get(position).getWeight()+" kg");
        holder.itemView.findViewById(R.id.expandableCardViewGrowth).setVisibility(View.GONE);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return growthList.size();
    }

    @Override
    public void onClick(View view) {
        if (view.findViewById(R.id.expandableCardViewGrowth).getVisibility() == View.VISIBLE) {
            view.findViewById(R.id.expandableCardViewGrowth).setVisibility(View.GONE);
//            TransitionManager.beginDelayedTransition(view.findViewById(R.id.cardViewItemGrowth), new AutoTransition());
            view.findViewById(R.id.chevron).setRotation(270);

        } else {
            TransitionManager.beginDelayedTransition((ViewGroup) view.findViewById(R.id.cardViewItemGrowth), new AutoTransition());
            view.findViewById(R.id.expandableCardViewGrowth).setVisibility(View.VISIBLE);
            view.findViewById(R.id.chevron).setRotation(90);

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView childAge, growthDate, childHeight, childWeight;
        public Button editGrowthData, deleteGrowthData;

        public ViewHolder(@NonNull View itemView,IRecyclerView iRecyclerView) {
            super(itemView);
            childAge = itemView.findViewById(R.id.tv_growthChildAge);
            growthDate = itemView.findViewById(R.id.tv_growthDate);
            childHeight = itemView.findViewById(R.id.tv_growthChildHeight);
            childWeight = itemView.findViewById(R.id.tv_growthChildWeight);
            editGrowthData = itemView.findViewById(R.id.btn_edit_growth_data);
            deleteGrowthData = itemView.findViewById(R.id.btn_delete_growth_data);
            editGrowthData.setOnClickListener(new View.OnClickListener() {
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
            deleteGrowthData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iRecyclerView != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            iRecyclerView.OnItemClick2(pos);
                        }
                    }
                }
            });
        }
    }
}
