package com.b07group47.taamcollectionmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URLConnection;

public class ViewActivity extends BaseActivity {
    private Button buttonDeleteItem, buttonReportItem;
    private Item item;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaController = new MediaController(this);
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
        setImage((int)item.getLotNumber());
        //setVideo((int)item.getLotNumber());
        itemLot.setText("Lot# " + item.getLotNumber());
        itemTitle.setText(item.getTitle());
        itemDescription.setText(item.getDescription());
        itemCategory.setText("Category: " + item.getCategory());
        itemPeriod.setText("Period: " + item.getPeriod());
    }

    private void setVideo(int lot) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageReference.child("images/" + lot + ".jpg");
        //String mimetype = URLConnection.guessContentTypeFromName(filePath.toString());


    }


    private void setImage(int lot){
        VideoView itemVideo = findViewById(R.id.itemVideo);
        ImageView itemImage = findViewById(R.id.itemImage);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageReference.child("images/"+ lot +".jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //String mimetype = URLConnection.guessContentTypeFromName(uri.toString());
                String mimetype = item.getImgID();
                if (mimetype.equals("image/jpeg")) {
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
                            Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    itemImage.setVisibility(View.INVISIBLE);
                    itemVideo.setVisibility(View.VISIBLE);
                    mediaController.setAnchorView(itemVideo);
                    mediaController.setMediaPlayer(itemVideo);
                    itemVideo.setMediaController(mediaController);
                    itemVideo.setVideoURI(Uri.parse(uri.toString()));
                    itemVideo.start();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                return;
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