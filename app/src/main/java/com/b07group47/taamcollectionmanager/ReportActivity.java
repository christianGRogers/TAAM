package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends BaseActivity {
    private static final String TAG = "ReportActivity";
    private Item item;
    private EditText editTextLotNumber, editTextName, editTextCategory, editTextCategoryDescPic,
            editTextPeriod, editTextPeriodDescPic;
    private Button buttonReportLot, buttonReportName, buttonReportCategory, buttonReportCategoryDescPic,
            buttonReportPeriod, buttonReportPeriodDescPic, buttonReportAll, buttonReportAllDescPic;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Initializing ReportActivity");
        item = getPassedAttributes();
        db = FirebaseFirestore.getInstance();
        setLayoutValues();
        setupButtonListeners();
    }

    private void setLayoutValues() {
        Log.d(TAG, "setLayoutValues: Setting layout values based on passed attributes");
        editTextLotNumber = findViewById(R.id.editTextLotNumber);
        editTextName = findViewById(R.id.editTextName);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextCategoryDescPic = findViewById(R.id.editTextCategoryDescPic);
        editTextPeriod = findViewById(R.id.editTextPeriod);
        editTextPeriodDescPic = findViewById(R.id.editTextPeriodDescPic);

        buttonReportLot = findViewById(R.id.buttonReportLot);
        buttonReportName = findViewById(R.id.buttonReportName);
        buttonReportCategory = findViewById(R.id.buttonReportCategory);
        buttonReportCategoryDescPic = findViewById(R.id.buttonReportCategoryDescPic);
        buttonReportPeriod = findViewById(R.id.buttonReportPeriod);
        buttonReportPeriodDescPic = findViewById(R.id.buttonReportPeriodDescPic);
        buttonReportAll = findViewById(R.id.buttonReportAll);
        buttonReportAllDescPic = findViewById(R.id.buttonReportAllDescPic);

        editTextLotNumber.setText("" + item.getLotNumber());
        editTextName.setText(item.getTitle());
        editTextCategory.setText(item.getCategory());
        editTextCategoryDescPic.setText(item.getCategory());
        editTextPeriod.setText(item.getPeriod());
        editTextPeriodDescPic.setText(item.getPeriod());
    }

    private void setupButtonListeners() {
        Log.d(TAG, "setupButtonListeners: Setting up button listeners");
        buttonReportLot.setOnClickListener(v -> {
            Log.d(TAG, "setupButtonListeners: buttonReportLot clicked");
            generateReportByLotNumber();
        });
        buttonReportName.setOnClickListener(v -> {
            Log.d(TAG, "setupButtonListeners: buttonReportName clicked");
            generateReportByName();
        });
        buttonReportCategory.setOnClickListener(v -> {
            Log.d(TAG, "setupButtonListeners: buttonReportCategory clicked");
            generateReportByCategory(false);
        });
        buttonReportCategoryDescPic.setOnClickListener(v -> {
            Log.d(TAG, "setupButtonListeners: buttonReportCategoryDescPic clicked");
            generateReportByCategory(true);
        });
        buttonReportPeriod.setOnClickListener(v -> {
            Log.d(TAG, "setupButtonListeners: buttonReportPeriod clicked");
            generateReportByPeriod(false);
        });
        buttonReportPeriodDescPic.setOnClickListener(v -> {
            Log.d(TAG, "setupButtonListeners: buttonReportPeriodDescPic clicked");
            generateReportByPeriod(true);
        });
        buttonReportAll.setOnClickListener(v -> {
            Log.d(TAG, "setupButtonListeners: buttonReportAll clicked");
            generateReportForAllItems(false);
        });
        buttonReportAllDescPic.setOnClickListener(v -> {
            Log.d(TAG, "setupButtonListeners: buttonReportAllDescPic clicked");
            generateReportForAllItems(true);
        });
    }

    private void generateReportByLotNumber() {
        String lotNumber = editTextLotNumber.getText().toString().trim();
        if (lotNumber.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a lot number", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "generateReportByLotNumber: Lot number is empty");
            return;
        }

        Log.d(TAG, "generateReportByLotNumber: Generating report for lot number " + lotNumber);
        Query query = ArtifactQueryFactory.getFilteredQuery(Long.parseLong(lotNumber), null, null, null);
        fetchDataAndGenerateReport(query, "Lot Number: " + lotNumber);
    }

    private void generateReportByName() {
        String name = editTextName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "generateReportByName: Name is empty");
            return;
        }

        Log.d(TAG, "generateReportByName: Generating report for name " + name);
        Query query = ArtifactQueryFactory.getFilteredQuery(null, name, null, null);
        fetchDataAndGenerateReport(query, "Name: " + name);
    }

    private void generateReportByCategory(boolean descPicOnly) {
        String category = descPicOnly ? editTextCategoryDescPic.getText().toString().trim()
                : editTextCategory.getText().toString().trim();
        if (category.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a category", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "generateReportByCategory: Category is empty");
            return;
        }

        Log.d(TAG, "generateReportByCategory: Generating report for category " + category + " with descPicOnly: " + descPicOnly);
        Query query = ArtifactQueryFactory.getFilteredQuery(null, null, category, null);
        fetchDataAndGenerateReport(query, "Category: " + category, descPicOnly);
    }

    private void generateReportByPeriod(boolean descPicOnly) {
        String period = descPicOnly ? editTextPeriodDescPic.getText().toString().trim()
                : editTextPeriod.getText().toString().trim();
        if (period.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a period", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "generateReportByPeriod: Period is empty");
            return;
        }

        Log.d(TAG, "generateReportByPeriod: Generating report for period " + period + " with descPicOnly: " + descPicOnly);
        Query query = ArtifactQueryFactory.getFilteredQuery(null, null, null, period);
        fetchDataAndGenerateReport(query, "Period: " + period, descPicOnly);
    }

    private void generateReportForAllItems(boolean descPicOnly) {
        Log.d(TAG, "generateReportForAllItems: Generating report for all items with descPicOnly: " + descPicOnly);
        Query query = ArtifactQueryFactory.getAll();
        fetchDataAndGenerateReport(query, "All Items", descPicOnly);
    }

    private void fetchDataAndGenerateReport(Query query, String reportTitle) {
        fetchDataAndGenerateReport(query, reportTitle, false);
    }

    private void fetchDataAndGenerateReport(Query query, String reportTitle, boolean descPicOnly) {
        Log.d(TAG, "fetchDataAndGenerateReport: Fetching data for report " + reportTitle + " with descPicOnly: " + descPicOnly);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Item> items = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Item item = document.toObject(Item.class);
                    if (descPicOnly) {
                        item = new Item(item.getLotNumber(), item.getDescription(), "", "", "", item.getImgID());
                    }
                    items.add(item);
                }
                PDFGenerator.generateReport(ReportActivity.this, items, reportTitle);
                Toast.makeText(getApplicationContext(), "Generated report for " + reportTitle + " with " + items.size() + " items", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "fetchDataAndGenerateReport: Successfully generated report for " + reportTitle + " with " + items.size() + " items");
            } else {
                Toast.makeText(getApplicationContext(), "Failed to generate report: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "fetchDataAndGenerateReport: Failed to generate report: " + task.getException().getMessage());
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_report;
    }
}