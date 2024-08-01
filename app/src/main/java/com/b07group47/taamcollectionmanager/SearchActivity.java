package com.b07group47.taamcollectionmanager;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class SearchActivity extends BaseActivity {
    private EditText editTextLotNumber, editTextName;
    private Spinner spinnerCategory, spinnerPeriod;

    private FirebaseFirestore db;
    private FilteredSearcher search;
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
    
        db = FirebaseFirestore.getInstance();
    
        // Set up the spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    
        buttonSearch.setOnClickListener(v -> searchItem());

        search = new FilteredSearcher();
    }

    private void searchItem() {
        String lotNumber = editTextLotNumber.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().toLowerCase();
        String period = spinnerPeriod.getSelectedItem().toString().toLowerCase();

        int lot;
        try {
            lot = Integer.parseInt(lotNumber);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lot number must be a number value!", Toast.LENGTH_SHORT).show();
            return;
        }



    }
}