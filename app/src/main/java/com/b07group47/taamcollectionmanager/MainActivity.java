package com.b07group47.taamcollectionmanager;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.util.Log;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private final List<Item> itemList = new ArrayList<>();
    private ItemAdapter itemAdapter;
    private Button buttonReport;
    private Button buttonDelete;
    private ImageView buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initButtons();
        createTable();
        handleIntent();
    }

    private void initButtons() {
        buttonReport = findViewById(R.id.buttonReport);
        buttonAdd = findViewById(R.id.buttonAddItem);
        buttonDelete = findViewById(R.id.buttonDeleteItem);
        buttonAdd.setOnClickListener(v -> switchToActivity(this, new Intent(this, AddItemActivity.class)));

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
        for (int i = 1; i <= 6; i++) {
            itemList.add(new Item(i, "Mew Exhibition", "This is a display of the Mew Dynasty artful pottery and decor", "Mew", "100BC", R.drawable.mew_vase));
        }
        itemAdapter.notifyItemInserted(insertIndex);
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
