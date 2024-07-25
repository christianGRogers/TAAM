package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewActivity extends BaseActivity {
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView itemInfo = findViewById(R.id.itemInfo);
        Button back_btn = findViewById(R.id.back_btn);
        ImageView itemImage = findViewById(R.id.itemImage);

        itemInfo.setText("Sample text\nitem information goes here");
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_view;
    }
}