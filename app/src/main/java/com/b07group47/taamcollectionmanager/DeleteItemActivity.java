package com.b07group47.taamcollectionmanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteItemActivity extends BaseActivity {
    private EditText editTextTitle;
    private Spinner spinnerCategory;

    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editTextTitle = findViewById(R.id.editTextLotNumber);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        Button buttonDelete = findViewById(R.id.buttonDelete);

        db = FirebaseDatabase.getInstance("https://b07-demo-summer-2024-default-rtdb.firebaseio.com/");

        // Set up the spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        buttonDelete.setOnClickListener(v -> deleteItemByTitle());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_delete_item;
    }

    private void deleteItemByTitle() {
        String title = editTextTitle.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().toLowerCase();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter item title", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference itemsRef = db.getReference("categories/" + category);
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean itemFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null && item.getTitle().equalsIgnoreCase(title)) {
                        snapshot.getRef().removeValue().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to delete item", Toast.LENGTH_SHORT).show();
                            }
                        });
                        itemFound = true;
                        break;
                    }
                }
                if (!itemFound) {
                    Toast.makeText(getApplicationContext(), "Item not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}