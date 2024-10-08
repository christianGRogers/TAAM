package com.b07group47.taamcollectionmanager;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class AddItemActivity extends BaseActivity {
    private static final int req = 1000;

    private EditText editTextLotNumber, editTextName, editTextDescription;
    private Button buttonAdd, buttonUpload;
    private ImageView image;
    private VideoView videoView;
    private MediaController mediaController;
    private Spinner spinnerCategory, spinnerPeriod;
    private FirebaseFirestore db;
    private Uri filePath = Uri.EMPTY, downloadUri = Uri.EMPTY;
    private FirebaseStorage firebaseStorage;
    private StorageReference imageRef;
    private String i;

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
        firebaseStorage = FirebaseStorage.getInstance();
        mediaController = new MediaController(this);
        // Set up the spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.periods_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(adapter);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //image reference is saved to filePath as a URI
        if (resultCode == RESULT_OK && requestCode == req && data != null) {
            filePath = data.getData();
            String mimetype = getContentResolver().getType(filePath);
            i = mimetype;
            if (mimetype.equals("image/jpeg")){
                image.setImageURI(filePath);
            }
            else{
                image.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.VISIBLE);
                mediaController.setAnchorView(videoView);
                mediaController.setMediaPlayer(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(Uri.parse(filePath.toString()));
                videoView.start();
            }

        }

    }

    private void initButtons(){
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(v -> {
            Log.d(TAG, "Add button clicked");
            addItem();
        });
        buttonUpload = findViewById(R.id.image_btn);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                String[] mimetypes = {"image/*", "video/*"};
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image or Video"), req);
            }
        });
    }

    private void initAnswers(){
        editTextLotNumber = findViewById(R.id.editTextLotNumber);
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerPeriod = findViewById(R.id.spinnerPeriod);
        image = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);
    }

    private void backtoMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromAdmin", true); // Pass the extra
        startActivity(intent);
        finish();
    }

    private void addItem() {
        String lotNumber = editTextLotNumber.getText().toString().trim();
        uploadImage(lotNumber);
        String name = editTextName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String period = spinnerPeriod.getSelectedItem().toString().trim();

        if (lotNumber.isEmpty() || name.isEmpty() || category.equals("Category") || period.equals("Period") || description.isEmpty() || Uri.EMPTY.equals(filePath)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long lot;
        try {
            lot = Long.parseLong(lotNumber);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lot number must be a number value!", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> artifact = new HashMap<>();
        artifact.put("image", i);
        artifact.put("category", category);
        artifact.put("description", description);
        artifact.put("lot", lot);
        artifact.put("name", name);
        artifact.put("period", period);
        checkAndAddItem(lot, artifact);
    }

    private void uploadImage(String lotNumber) {
        imageRef = firebaseStorage.getReference().child("images/" + lotNumber+".jpg");
        UploadTask uploadTask = imageRef.putFile(filePath);
    }

    private void checkAndAddItem(long lot, Map<String, Object> artifact) {
        ArtifactQueryFactory.getAll()
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

    private void addNewItem(long lot, Map<String, Object> artifact) {
        ArtifactQueryFactory.getAll()
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