package com.b07group47.taamcollectionmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ImageDownloader {
    private static final String TAG = "ImageDownloader";
    private static final int MAX_RETRIES = 3;
    private static final long TIMEOUT_SECONDS = 30;
    private final FirebaseStorage storage;

    public ImageDownloader() {
        Log.d(TAG, "Initializing FirebaseStorage instance.");
        this.storage = FirebaseStorage.getInstance();
    }

    public StorageReference getImageReference(long lotNumber) {
        return storage.getReference().child("images/" + lotNumber + ".jpg");
    }

    public CompletableFuture<Bitmap> downloadImage(long lotNumber) {
        Log.d(TAG, "Starting image download for Lot Number: " + lotNumber);
        StorageReference imageRef = getImageReference(lotNumber);
        return downloadImageWithRetry(imageRef, lotNumber, MAX_RETRIES);
    }

    private CompletableFuture<Bitmap> downloadImageWithRetry(StorageReference imageRef, long lotNumber, int retriesLeft) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] bytes = Tasks.await(imageRef.getBytes(Long.MAX_VALUE), TIMEOUT_SECONDS, TimeUnit.SECONDS);
                if (bytes == null || bytes.length == 0) {
                    throw new RuntimeException("Downloaded image bytes are empty");
                }
                Log.d(TAG, "Successfully downloaded image bytes for Lot Number: " + lotNumber);
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                return rotateBitmap(bm, 90);
            } catch (Exception e) {
                Log.e(TAG, "Failed to download image for Lot Number: " + lotNumber, e);
                if (retriesLeft > 0) {
                    Log.d(TAG, "Retrying download for Lot Number: " + lotNumber + ". Retries left: " + retriesLeft);
                    return downloadImageWithRetry(imageRef, lotNumber, retriesLeft - 1).join();
                } else {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private Bitmap rotateBitmap(Bitmap source, float angle) {
        if (source == null) {
            Log.e(TAG, "Bitmap is null, cannot rotate.");
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}