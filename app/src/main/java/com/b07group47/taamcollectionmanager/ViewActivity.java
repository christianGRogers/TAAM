package com.b07group47.taamcollectionmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewActivity extends BaseActivity {
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
        setImage((int)item.getLotNumber());
        itemLot.setText("Lot# " + item.getLotNumber());
        itemTitle.setText(item.getTitle());
        itemDescription.setText(item.getDescription());
        itemCategory.setText("Category: " + item.getCategory());
        itemPeriod.setText("Period:" + item.getPeriod());
    }

    private void setImage(int lot){
        ImageView itemImage = findViewById(R.id.itemImage);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageReference.child("images/"+ lot +".jpg");
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                itemImage.setImageBitmap(bm);
                itemImage.setRotation(90);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!"+lot, Toast.LENGTH_LONG).show();
            }
        });
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