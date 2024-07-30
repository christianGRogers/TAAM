package com.b07group47.taamcollectionmanager;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.media.Image;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.GetTokenResult;

public class ViewActivity extends BaseActivity {
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;
    private String title, description, category, period;
    private int lot, imageID;
    private Button buttonDeleteItem;
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
        itemPeriod.setText("Period:" + item.getPeriod());
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