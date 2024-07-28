package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private final List<Item> itemList = new ArrayList<>();
    private ItemAdapter itemAdapter;

    /**
     * Equivalent to a constructor of the activity
     * @param savedInstanceState the saved parameters of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView buttonAdd = findViewById(R.id.buttonAddItem);
        buttonAdd.setOnClickListener(v -> switchToActivity(this, AddItemActivity.class));
        createTable();
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
}