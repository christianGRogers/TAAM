package com.b07group47.taamcollectionmanager;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewActivity extends BaseActivity {
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = getPassedAttributes();
        setLayoutValues();
    }

    private void setLayoutValues() {
        TextView itemLot = findViewById(R.id.itemLot);
        TextView itemTitle = findViewById(R.id.itemTitle);
        TextView itemDescription = findViewById(R.id.itemDescription);
        TextView itemCategory = findViewById(R.id.itemCategory);
        TextView itemPeriod = findViewById(R.id.itemPeriod);
        ImageView itemImage = findViewById(R.id.itemImage);

        itemLot.setText("Lot# " + item.getLotNumber());
        itemTitle.setText(item.getTitle());
        itemDescription.setText(item.getDescription());
        itemCategory.setText("Category: " + item.getCategory());
        itemPeriod.setText("Period:" + item.getPeriod());
        itemImage.setImageResource(item.getImgID());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_view;
    }
}