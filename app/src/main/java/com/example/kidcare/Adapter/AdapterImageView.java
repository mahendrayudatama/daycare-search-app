package com.example.kidcare.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidcare.IRecyclerView;
import com.example.kidcare.Model.ModelUrl;
import com.example.kidcare.R;

import java.util.ArrayList;

public class AdapterImageView extends RecyclerView.Adapter<AdapterImageView.ExamplerViewHolder> {
    private ArrayList<ModelUrl> mItems;
    private IRecyclerView mIRecyclerView;


    public AdapterImageView(ArrayList<ModelUrl> Items, IRecyclerView iRecyclerView) {
        mItems = Items;
        mIRecyclerView = iRecyclerView;
    }

    @NonNull
    @Override
    public ExamplerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_imageview, parent, false);
        ExamplerViewHolder evh = new ExamplerViewHolder(v, mIRecyclerView);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExamplerViewHolder holder, int position) {
        ModelUrl currentItem = mItems.get(position);
        int imagePos = position + 1;
        holder.mTextView1.setText("Image-" + imagePos);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ExamplerViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;

        public ExamplerViewHolder(@NonNull View itemView, IRecyclerView iRecyclerView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.tv_imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
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
        }
    }
}