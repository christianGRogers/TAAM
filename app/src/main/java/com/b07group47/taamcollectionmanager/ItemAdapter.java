package com.b07group47.taamcollectionmanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final List<Item> itemList;
    private final Context context;

    public ItemAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_card, parent, false);
        return new ItemViewHolder(view, context, itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.lotNum.setText("" + item.getLotNumber());
        holder.cardHeader.setText(item.getTitle());
        holder.cardDesc.setText(item.getDescription());
        holder.cardPeriod.setText(item.getPeriod());
        holder.cardCategory.setText(item.getCategory());
        holder.cardImg.setImageResource(R.drawable.mew_vase);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView lotNum, cardHeader, cardDesc, cardCategory, cardPeriod;
        ImageView cardImg;

        public ItemViewHolder(@NonNull View itemView, Context context, List<Item> items) {
            super(itemView);
            lotNum = itemView.findViewById(R.id.lotNum);
            cardHeader = itemView.findViewById(R.id.cardHeader);
            cardDesc = itemView.findViewById(R.id.cardDescription);
            cardCategory = itemView.findViewById(R.id.cardCategory);
            cardPeriod = itemView.findViewById(R.id.cardPeriod);
            cardImg = itemView.findViewById(R.id.cardImage);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();

                if (pos != RecyclerView.NO_POSITION) {
                    Intent i = new Intent(context, ViewActivity.class);
                    i.putExtra("LOT", items.get(pos).getLotNumber());
                    i.putExtra("TITLE", items.get(pos).getTitle());
                    i.putExtra("DESCRIPTION", items.get(pos).getDescription());
                    i.putExtra("CATEGORY", items.get(pos).getCategory());
                    i.putExtra("PERIOD", items.get(pos).getPeriod());
                    i.putExtra("IMAGE", items.get(pos).getImgID());

                    context.startActivity(i);
                }
            });
        }
    }
}
