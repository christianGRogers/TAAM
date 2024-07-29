package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItemActivity extends BaseActivity {
    private EditText editTextLotNumber, editTextName, editTextDescription;
    private Spinner spinnerCategory, spinnerPeriod;
    private FirebaseDatabase db;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editTextLotNumber = findViewById(R.id.editTextLotNumber);
        editTextName = findViewById(R.id.editTextName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerPeriod = findViewById(R.id.spinnerPeriod);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        editTextDescription = findViewById(R.id.editTextDescription);

        db = FirebaseDatabase.getInstance("https://b07-demo-summer-2024-default-rtdb.firebaseio.com/");

        // Set up the spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.periods_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(adapter);

        buttonAdd.setOnClickListener(v -> addItem());
    }

    private void addItem() {
        String lotNumber = editTextLotNumber.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().toLowerCase();
        String period = spinnerPeriod.getSelectedItem().toString().toLowerCase();
        String description = editTextDescription.getText().toString().trim();

        if (lotNumber.isEmpty() || name.isEmpty() || category.isEmpty() || period.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int lot;
        try {
            lot = Integer.parseInt(lotNumber);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lot number must be a number value!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (category.equals("category")){
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        if (period.equals("period")){
            Toast.makeText(this, "Please select a period", Toast.LENGTH_SHORT).show();
            return;
        }

        //DatabaseReference itemsRef = db.getReference("categories/" + category);
        DatabaseReference itemsRef = db.getReference("artifactData");
        //String id = itemsRef.push().getKey();
        Item item = new Item(lot, description, name, category, period, R.drawable.mew_vase);

        itemsRef.child(lotNumber).setValue(item).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}