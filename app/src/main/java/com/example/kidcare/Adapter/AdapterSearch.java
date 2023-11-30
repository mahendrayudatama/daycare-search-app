package com.example.kidcare.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidcare.IRecyclerView;
import com.example.kidcare.Model.ModelSearch;
import com.example.kidcare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ExamplerViewHolder> {
    private ArrayList<ModelSearch> mItems;
    private IRecyclerView mIRecyclerView;

    public void filterList(ArrayList<ModelSearch> filteredList) {
        mItems = filteredList;
        this.notifyDataSetChanged();
    }

    public AdapterSearch(ArrayList<ModelSearch> Items, IRecyclerView iRecyclerView) {
        mItems = Items;
        mIRecyclerView = iRecyclerView;
    }

    @NonNull
    @Override
    public ExamplerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_daycare, parent, false);
        ExamplerViewHolder evh = new ExamplerViewHolder(v, mIRecyclerView);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExamplerViewHolder holder, int position) {
        ModelSearch currentItem = mItems.get(position);

        holder.mTextView1.setText(currentItem.getDaycareName());
        holder.mTextView2.setText(currentItem.getDaycarePrice());
        holder.mTextView3.setText(currentItem.getDaycareDistance());
        if (currentItem.getDaycarePic().isEmpty()) {

        } else {
            Picasso.get().load(currentItem.getDaycarePic()).into(holder.mImageView);
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ExamplerViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public ImageView mImageView;
        OnCardListener onCardListener;

        public ExamplerViewHolder(@NonNull View itemView, IRecyclerView iRecyclerView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.titlesDaycare);
            mTextView2 = itemView.findViewById(R.id.daycare_price);
            mTextView3 = itemView.findViewById(R.id.daycare_distance);
            mImageView = itemView.findViewById(R.id.daycare_search_pic);
            this.onCardListener = onCardListener;
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

    public void updateData(ArrayList<ModelSearch> mItems) {
        this.mItems = mItems;
        notifyDataSetChanged();
    }

    public interface OnCardListener {
        void onCardClick(int position);
    }


}