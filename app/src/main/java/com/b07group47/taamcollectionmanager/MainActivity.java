package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initButtons();
    }

    private void initButtons() {
        // TODO: Embed data table into main screen
        Button dataTable = findViewById(R.id.data_table);
        Button buttonViewItems = findViewById(R.id.buttonView);
        Button buttonSearch = findViewById(R.id.buttonSearch);
        Button buttonReport = findViewById(R.id.buttonReport);
        Button buttonAdd = findViewById(R.id.buttonAddItem);
        Button buttonDelete = findViewById(R.id.buttonDeleteItem);
        Button buttonBack = findViewById(R.id.buttonBack);

        dataTable.setOnClickListener(v -> switchToActivity(this, MainTableActivity.class));
        buttonViewItems.setOnClickListener(v -> switchToActivity(this, ViewActivity.class));
        buttonSearch.setOnClickListener(v -> switchToActivity(this, SearchActivity.class));
        buttonReport.setOnClickListener(v -> switchToActivity(this, ReportActivity.class));
        buttonAdd.setOnClickListener(v -> switchToActivity(this, AddItemActivity.class));
        buttonDelete.setOnClickListener(v -> switchToActivity(this, DeleteItemActivity.class));
        buttonBack.setOnClickListener(v -> switchToActivity(this, MainActivity.class));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }
}