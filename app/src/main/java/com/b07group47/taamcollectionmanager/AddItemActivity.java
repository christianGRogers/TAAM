package com.b07group47.taamcollectionmanager;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;

import java.util.UUID;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItemActivity extends BaseActivity {

    private static final int req = 1000;

    private EditText editTextLotNumber, editTextName, editTextDescription;
    private Spinner spinnerCategory, spinnerPeriod;
    private Button buttonAdd, buttonUpload;
    private ImageView image;
    private FirebaseDatabase db;
    private Uri filePath;


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
        buttonAdd = findViewById(R.id.buttonAdd);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonUpload = findViewById(R.id.image_btn);
        image = findViewById(R.id.imageView);

        db = FirebaseDatabase.getInstance();

        // Set up the spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.periods_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(adapter);

        //get image from gallery
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), req);
            }
        });

        buttonAdd.setOnClickListener(v -> addItem());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        filePath = data.getData();
        if (resultCode == RESULT_OK && requestCode == req) {
            image.setImageURI(filePath);
        }

    }

    private void addItem() {
        String lotNumber = editTextLotNumber.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().toLowerCase();
        String period = spinnerPeriod.getSelectedItem().toString().toLowerCase();
        String description = editTextDescription.getText().toString().trim();
        String image = filePath.toString();


        if (lotNumber.isEmpty() || name.isEmpty() || category.isEmpty() || period.isEmpty() || filePath==null) {
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
        Item item = new Item(lot, description, name, category, period, image);

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