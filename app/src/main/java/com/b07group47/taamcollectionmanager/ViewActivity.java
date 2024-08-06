package com.b07group47.taamcollectionmanager;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewActivity extends BaseActivity {
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;
    private String title, description, category, period;
    private int lot, imageID;
    private Button buttonDeleteItem, buttonReportItem;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = getPassedAttributes();
        initButtons();
        setLayoutValues();
        handleIntent();

    }

    private void initButtons() {
        buttonDeleteItem = findViewById(R.id.buttonDeleteItem);
        buttonDeleteItem.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeleteItemActivity.class);
            intent.putExtra("LOT", item.getLotNumber());
            switchToActivity(intent);
        });

        buttonReportItem = findViewById(R.id.buttonReportItem);
        buttonReportItem.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportActivity.class);
            intent.putExtra("LOT", item.getLotNumber());
            intent.putExtra("TITLE", item.getTitle());
            intent.putExtra("DESCRIPTION", item.getDescription());
            intent.putExtra("CATEGORY", item.getCategory());
            intent.putExtra("PERIOD", item.getPeriod());
            switchToActivity(intent);
        });
    }

    /**
     * Auto-fills the appropriate values in the layout based on the attributes passed in the Intent
     * which invoked the activity and which are stores in the 'item' object
     */
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
        itemPeriod.setText("Period: " + item.getPeriod());
        itemImage.setImageResource(item.getImgID());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_view;
    }

    private void handleIntent() {
        //ill fix this later
        buttonDeleteItem.setVisibility(View.VISIBLE);
    }
}