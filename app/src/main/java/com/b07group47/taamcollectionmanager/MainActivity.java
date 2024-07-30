package com.b07group47.taamcollectionmanager;

import static android.content.ContentValues.TAG;

import android.content.Intent;
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
import android.util.Log;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private final List<Item> itemList = new ArrayList<>();
    private ItemAdapter itemAdapter;
    private Button buttonReport;
    private ImageView buttonAdd;

    /**
     * Equivalent to a constructor of the activity
     * @param savedInstanceState the saved parameters of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initButtons();
        createTable();
        handleIntent();
    }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void initButtons() {
        buttonReport = findViewById(R.id.buttonReport);
        buttonAdd = findViewById(R.id.buttonAddItem);
        buttonAdd.setOnClickListener(v -> switchToActivity(new Intent(this, AddItemActivity.class)));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    /**
     * Creates the table implemented as a RecyclerView in activity_main.xml
     * Uses the ItemAdapter class to facilitate the logic of the tables
     * Uses the TableSpacing class to facilitate the spacing between elements of the table
     */
    private void createTable() {
        RecyclerView recyclerView = findViewById(R.id.table_layout);
        itemAdapter = new ItemAdapter(itemList, this);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_item_spacing);
        recyclerView.addItemDecoration(new TableSpacing(spacingInPixels));
        insertData();
    }
    
    /**
     * Populates the list of the items displayed by the table in activity_main.xml
     */
//      TODO: refactor to work with Firebase
    private void insertData() {
        for (int i = 1; i <= 10; i++) {
            itemList.add(new Item(i, "Mew Exhibition",
                    "This is a display of the Mew Dynasty artful pottery and decor",
                    "Mew", "100BC", R.drawable.mew_vase));
            itemAdapter.notifyItemInserted(itemList.size());
        }
    }

    private void handleIntent() {
        Intent intent = getIntent();
        boolean fromAdmin = intent.getBooleanExtra("fromAdmin", false);
        Log.d(TAG, "fromAdmin: " + fromAdmin); // Log the value of fromAdmin
        if (fromAdmin) {
            // Show the buttons if coming from AdminActivity
            Log.d(TAG, "AdminActivity detected, showing buttons.");
            buttonReport.setVisibility(View.VISIBLE);
            buttonAdd.setVisibility(View.VISIBLE);
            //buttonDelete.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "AdminActivity not detected, buttons remain hidden.");
        }
    }
}
