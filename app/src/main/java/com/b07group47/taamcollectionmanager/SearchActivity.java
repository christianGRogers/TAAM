package com.b07group47.taamcollectionmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.mutation.ArrayTransformOperation;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    private EditText editTextLotNumber, editTextName;
    private Spinner spinnerCategory, spinnerPeriod;


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editTextLotNumber = findViewById(R.id.editTextLotNumber);
        editTextName = findViewById(R.id.editTextName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerPeriod = findViewById(R.id.spinnerPeriod);
        Button buttonSearch = findViewById(R.id.buttonSearch);

        // Set up the spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.periods_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(adapter);
    
        buttonSearch.setOnClickListener(v -> searchItem());
    }

    private void searchItem() {
        String lotNumber = editTextLotNumber.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String period = spinnerPeriod.getSelectedItem().toString();

        Bundle b = new Bundle(4);
        if (!lotNumber.isEmpty()) {
            b.putLong("lot", Long.parseLong(lotNumber));
        }
        if (!name.isEmpty())
            b.putString("name", name);
        if (!category.isEmpty() && !category.equals(getResources().getStringArray(R.array.categories_array)[0])) {
            b.putString("category", category);
        }
        if (!period.isEmpty() && !period.equals(getResources().getStringArray(R.array.periods_array)[0])) {
            b.putString("period", period);
        }

        Intent i = new Intent(this, MainActivity.class);
        i.putExtras(b);
        i.putExtra("fromSearch", true);
        startActivity(i);
        finish();
    }
}