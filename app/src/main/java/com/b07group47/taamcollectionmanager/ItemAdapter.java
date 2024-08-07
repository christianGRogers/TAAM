package com.b07group47.taamcollectionmanager;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * class which handles the logic of the table present on the main screen, i.e.
 * 1. Adding a new item to the table (onCreateViewHolder)
 * 2. Modifying the newly-added item to display specific text/properties (onBindViewHolder)
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final List<Item> itemList;
    private final Context context;

    public ItemAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    /**
     * Called (before onBindViewHolder) whenever a new item is being added to the RecyclerView table
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return new ItemViewHolder representing an item in the table
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_card, parent, false);
        return new ItemViewHolder(view, context, itemList);
    }

    /**
     * Called whenever the ViewHolder representing an item in the table is being added to the list
     * Modifies the attributes of the item and sets the fields to appropriate values
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.lotNum.setText("" + item.getLotNumber());
        holder.cardHeader.setText(item.getTitle());
        holder.cardDesc.setText(item.getDescription());
        holder.cardPeriod.setText(item.getPeriod());
        holder.cardCategory.setText(item.getCategory());
        //sets image correctly
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageReference.child("images/"+ item.getLotNumber() +".jpg");
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.cardImg.setImageBitmap(bm);
                holder.cardImg.setRotation(90);
            }
        });
    }

    /**
     * Returns the size of the tables
     *
     * @return the number of elements in the list of items which is displayed by the RecyclerView
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * class representing an item in the table, contains references to all the
     * modifiable areas of the item such as TextViews and ImageViews as well as
     * onClickListeners for the buttons
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final Context context;
        private final List<Item> items;
        TextView lotNum, cardHeader, cardDesc, cardCategory, cardPeriod;
        ImageView cardImg, reportBtn, deleteBtn;

        /**
         * Equivalent to a constructor of ItemViewHolder
         *
         * @param itemView the id of the xml layout that this object corresponds to
         * @param context  the activity which contains the table (in this case always the main table)
         * @param items    the list of items in the table (used to determine which item was clicked)
         */
        public ItemViewHolder(@NonNull View itemView, Context context, List<Item> items) {
            super(itemView);
            this.context = context;
            this.items = new ArrayList<>(items);
            lotNum = itemView.findViewById(R.id.lotNum);
            cardHeader = itemView.findViewById(R.id.cardHeader);
            cardDesc = itemView.findViewById(R.id.cardDescription);
            cardCategory = itemView.findViewById(R.id.cardCategory);
            cardPeriod = itemView.findViewById(R.id.cardPeriod);
            cardImg = itemView.findViewById(R.id.cardImage);
            reportBtn = itemView.findViewById(R.id.reportIcon);
            deleteBtn = itemView.findViewById(R.id.deleteIcon);

            itemView.setOnClickListener(v -> openActivity(ViewActivity.class));
            reportBtn.setOnClickListener(v -> openActivity(ReportActivity.class));
            deleteBtn.setOnClickListener(v -> openActivity(DeleteItemActivity.class));
        }
        /**
         * Used to open a new activity which will receive all the attributes corresponding
         * to the item in the table that was clicked by the user
         *
         * @param activity activity to open
         */
        private void openActivity(Class<? extends AppCompatActivity> activity) {
            int pos = getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION) {
                Intent i = new Intent(context, activity);
                i.putExtra("LOT", items.get(pos).getLotNumber());
                i.putExtra("TITLE", items.get(pos).getTitle());
                i.putExtra("DESCRIPTION", items.get(pos).getDescription());
                i.putExtra("CATEGORY", items.get(pos).getCategory());
                i.putExtra("PERIOD", items.get(pos).getPeriod());
                i.putExtra("IMAGE", items.get(pos).getImgID());
                context.startActivity(i);
            }
        }
    }
}
