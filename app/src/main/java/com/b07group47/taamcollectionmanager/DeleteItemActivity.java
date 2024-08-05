package com.b07group47.taamcollectionmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

public class DeleteItemActivity extends BaseActivity {
    private long lot;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageRef, imageRef;
    private Button buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        getLotFromIntent();
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();

        initButtons();
    }


    private void initButtons(){
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(v -> deleteItemByTitle());
    }


    private void getLotFromIntent(){
        lot = getIntent().getIntExtra("LOT", -1);
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_delete_item;
    }


    private void backtoMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromAdmin", true); // Pass the extra
        startActivity(intent);
        finish();
    }

    private void deleteItemByTitle() {
        if (this.lot == -1) {
            Toast.makeText(DeleteItemActivity.this, "Invalid lot ID", Toast.LENGTH_SHORT).show();
            backtoMain();
            return;
        }
        feature/Search
        Query query = ArtifactQueryFactory.getFilteredQuery(lot, null, null, null);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                // Assuming there is only one document with the given lot value
                task.getResult().getDocuments().get(0).getReference().delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(DeleteItemActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                            backtoMain();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(DeleteItemActivity.this, "Error deleting item", Toast.LENGTH_SHORT).show();
                            backtoMain();
                        });
            } else {
                Toast.makeText(DeleteItemActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                backtoMain();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(DeleteItemActivity.this, "Error querying item", Toast.LENGTH_SHORT).show();
            backtoMain();
        });
        //delete image
        imageRef = storageRef.child("images/"+ lot +".jpg");
        imageRef.delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                int errorCode = ((StorageException) exception).getErrorCode();
                String errorMessage = exception.getMessage();
                Toast.makeText(DeleteItemActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(DeleteItemActivity.this, "Image deleted successfully", Toast.LENGTH_SHORT).show();

            }
        });

    }

}