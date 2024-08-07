package com.b07group47.taamcollectionmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

public class ImageDownloader {

    private static final String TAG = "ImageDownloader";
    private final FirebaseStorage storage;

    public ImageDownloader() {
        Log.d(TAG, "Initializing FirebaseStorage instance.");
        storage = FirebaseStorage.getInstance();
    }

    public CompletableFuture<Image> downloadImage(String lotNumber) {
        CompletableFuture<Image> future = new CompletableFuture<>();

        StorageReference storageReference = storage.getReference();
        StorageReference imageRef = storageReference.child("images/" + lotNumber + ".jpg");

        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap rotatedBm = rotateBitmap(bm, 90);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    rotatedBm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bitmapData = stream.toByteArray();
                    Image image = new Image(ImageDataFactory.create(bitmapData));
                    future.complete(image);
                    Log.d(TAG, "Successfully downloaded and processed image for Lot Number: " + lotNumber);
                } catch (Exception e) {
                    Log.e(TAG, "Error processing image for Lot Number: " + lotNumber, e);
                    future.completeExceptionally(e);
                }
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to download image for Lot Number: " + lotNumber, e);
            future.completeExceptionally(e);
        });

        return future;
    }

    private Bitmap rotateBitmap(Bitmap source, float angle) {
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}