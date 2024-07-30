package com.b07group47.taamcollectionmanager;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddItemActivity extends BaseActivity {
    private EditText editTextLotNumber, editTextName, editTextDescription, editTextCategory, editTextPeriod;
    private Button buttonAdd;
    private FirebaseFirestore db;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAnswers();
        initButtons();
        db = FirebaseFirestore.getInstance();
        // Set up the spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void initButtons(){
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v -> {
            Log.d(TAG, "Add button clicked");
            addItem();
        });
    }

    private void initAnswers(){
        editTextLotNumber = findViewById(R.id.editTextLotNumber);
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextPeriod = findViewById(R.id.editTextPeriod);
    }

    private void backtoMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromAdmin", true); // Pass the extra
        startActivity(intent);
        finish();
    }

    private void addItem() {
        String lotNumber = editTextLotNumber.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String category = editTextCategory.getText().toString().toLowerCase();
        String description = editTextDescription.getText().toString().toLowerCase();
        String period = editTextPeriod.getText().toString().toLowerCase();

        String image = "test/test"; //change this later this is just for now

        if (lotNumber.isEmpty() || name.isEmpty() || category.isEmpty() || period.isEmpty() || description.isEmpty()) {
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

        Map<String, Object> artifact = new HashMap<>();
        artifact.put("image", image);
        artifact.put("category", category);
        artifact.put("description", description);
        artifact.put("lot", lot);
        artifact.put("name", name);
        artifact.put("period", period);
        checkAndAddItem(lot, artifact);
    }

    private void checkAndAddItem(int lot, Map<String, Object> artifact) {
        db.collection("artifactData")
                .whereEqualTo("lot", lot)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            Toast.makeText(this, "Lot number already exists. Please use a different lot number.", Toast.LENGTH_SHORT).show();
                        } else {
                            addNewItem(lot, artifact);
                        }
                    } else {
                        Log.w(TAG, "Error checking lot number", task.getException());
                        Toast.makeText(this, "Error checking lot number. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addNewItem(int lot, Map<String, Object> artifact) {
        db.collection("artifactData")
                .add(artifact)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(AddItemActivity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                        backtoMain();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(AddItemActivity.this, "Error adding item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}