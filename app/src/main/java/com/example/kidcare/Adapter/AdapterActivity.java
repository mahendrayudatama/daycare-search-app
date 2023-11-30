package com.example.kidcare.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidcare.ActivityImageView;
import com.example.kidcare.ActivityVideoView;
import com.example.kidcare.IRecyclerView;
import com.example.kidcare.Model.ModelActivity;
import com.example.kidcare.R;

import java.util.ArrayList;

public class AdapterActivity extends RecyclerView.Adapter<AdapterActivity.ViewHolder> implements IRecyclerView {

    Context context;
    ArrayList<ModelActivity> modelActivityList;
    ModelActivity modelActivity;

    public AdapterActivity(Context context, ArrayList<ModelActivity> modelActivityList) {
        this.context = context;
        this.modelActivityList = modelActivityList;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_activity, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull AdapterActivity.ViewHolder holder, int position) {
        modelActivity = modelActivityList.get(position);
        holder.activity.setText(modelActivity.getActivity());
        holder.noted.setText(modelActivity.getNoted());
        holder.time.setText(modelActivity.getTime());
        holder.status.setText(modelActivity.getStatus());


        AdapterImageView adapterImageView = new AdapterImageView(modelActivity.getModelImageUrlList(), this);
        holder.rvImageUrl.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rvImageUrl.setAdapter(adapterImageView);
        adapterImageView.notifyDataSetChanged();

        AdapterVideoView adapterVideoView = new AdapterVideoView(modelActivity.getModelVideoUrlList(),this);
        holder.rvVideoUrl.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        holder.rvVideoUrl.setAdapter(adapterVideoView);
        adapterVideoView.notifyDataSetChanged();

        holder.view.findViewById(R.id.expandableCardView).setVisibility(View.GONE);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (holder.view.findViewById(R.id.expandableCardView).getVisibility() == View.VISIBLE) {
                    holder.view.findViewById(R.id.expandableCardView).setVisibility(View.GONE);
                    holder.view.findViewById(R.id.chevron).setRotation(270);
                } else {
                    TransitionManager.beginDelayedTransition((ViewGroup) holder.view.findViewById(R.id.cardViewItem), new AutoTransition());
                    holder.view.findViewById(R.id.expandableCardView).setVisibility(View.VISIBLE);
                    holder.view.findViewById(R.id.chevron).setRotation(90);
                }
            }
        });

        if (modelActivity.getStatus().equals("true")) {
            ImageViewCompat.setImageTintList(holder.view.findViewById(R.id.check), ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange)));
            ImageViewCompat.setImageTintList(holder.view.findViewById(R.id.chevron), ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange)));
            holder.view.findViewById(R.id.viewCardList).setBackground(this.context.getDrawable(R.drawable.viewcard));
        }
        if (modelActivity.getStatus().equals("false")) {
            ImageViewCompat.setImageTintList(holder.view.findViewById(R.id.check), ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray_checklist)));
            ImageViewCompat.setImageTintList(holder.view.findViewById(R.id.chevron), ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray_checklist)));
            holder.view.findViewById(R.id.viewCardList).setBackground(this.context.getDrawable(R.drawable.viewcard_notfocused));
        }
    }

    @Override
    public int getItemCount() {
        try {
            return modelActivityList.size();

        } catch (NullPointerException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public void changeList(ArrayList<ModelActivity> list) {
        this.modelActivityList = list;
        notifyDataSetChanged();

    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(context, ActivityImageView.class);
        intent.putExtra("url",modelActivity.getModelImageUrlList().get(position).getUrl());
        Log.d("urlList",modelActivity.getModelImageUrlList().get(position).getUrl());
        context.startActivity(intent);
    }

    @Override
    public void OnItemClick2(int position) {
        Intent intent = new Intent(context, ActivityVideoView.class);
        intent.putExtra("url",modelActivity.getModelVideoUrlList().get(position).getUrl());
        context.startActivity(intent);
    }

    @Override
    public void OnItemClick3(int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvImageUrl,rvVideoUrl;
        TextView activity, time, noted, status;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvImageUrl = itemView.findViewById(R.id.rv_imageview);
            rvVideoUrl = itemView.findViewById(R.id.rv_videoview);
            activity = itemView.findViewById(R.id.titles);
            noted = itemView.findViewById(R.id.noted);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
            view = itemView;
        }
    }
}

