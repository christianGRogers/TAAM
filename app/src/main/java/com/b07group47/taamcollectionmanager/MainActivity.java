package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;


import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private final List<Item> itemList = new ArrayList<>();
    private ItemAdapter itemAdapter;

  //    private static final String TAG = "MainActivity";

  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initButtons();
        createTable();
    }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Example: Update the document with new field
//         Map<String, Object> update = new HashMap<>();
//         update.put("name", "your_mom");

//         db.collection("test").document("BrzGwRQqes8dWASlO72S")
//                 .update(update)
//                 .addOnSuccessListener(new OnSuccessListener<Void>() {
//                     @Override
//                     public void onSuccess(Void aVoid) {
//                         Log.d(TAG, "DocumentSnapshot successfully updated!");
//                     }
//                 })
//                 .addOnFailureListener(new OnFailureListener() {
//                     @Override
//                     public void onFailure(@NonNull Exception e) {
//                         Log.w(TAG, "Error updating document", e);
//                     }
//                 });

    private void initButtons() {
//        Button buttonReport = findViewById(R.id.buttonReport);
        ImageView buttonAdd = findViewById(R.id.buttonAddItem);
//        Button buttonDelete = findViewById(R.id.buttonDeleteItem);

//        buttonReport.setOnClickListener(v -> switchToActivity(this, ReportActivity.class));
        buttonAdd.setOnClickListener(v -> switchToActivity(this, AddItemActivity.class));
//        buttonDelete.setOnClickListener(v -> switchToActivity(this, DeleteItemActivity.class));
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    private void createTable() {
        RecyclerView recyclerView = findViewById(R.id.table_layout);
        itemAdapter = new ItemAdapter(itemList, this);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_item_spacing);
        recyclerView.addItemDecoration(new TableSpacing(spacingInPixels));
        insertData();
    }

    private void insertData() {
        int insertIndex = itemList.size();
        for (int i=1; i<=6; i++){
            itemList.add(new Item(i, "Mew Exhibition", "This is a display of the Mew Dynasty artful pottery and decor", "Mew", "100BC",  R.drawable.mew_vase));
        }
        itemAdapter.notifyItemInserted(insertIndex);
    }
}
