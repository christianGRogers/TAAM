package com.b07group47.taamcollectionmanager;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ImageDownloader {

    private static final String TAG = "ImageDownloader";
    private static final long ONE_MEGABYTE = 1024 * 1024;
    private static final int NUMBER_OF_THREADS = 12;
    private static final long DOWNLOAD_TIMEOUT = 30; // seconds

    private final ExecutorService executorService;
    private final FirebaseStorage storage;

    public ImageDownloader() {
        Log.d(TAG, "Initializing FirebaseStorage instance.");
        storage = FirebaseStorage.getInstance();
        executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }

    public void downloadImageInBackground(int imageId, ConcurrentHashMap<Integer, Future<Image>> imageMap) {
        Log.d(TAG, "Submitting image download task for imageId: " + imageId);
        Future<Image> imageFuture = executorService.submit(() -> downloadImage(imageId));
        imageMap.put(imageId, imageFuture);
    }

    private Image downloadImage(int imageId) {
        Log.d(TAG, "Starting download for imageId: " + imageId);
        CompletableFuture<Image> future = new CompletableFuture<>();
        StorageReference imageRef = storage.getReference().child("images/" + imageId + ".jpg");

        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Log.d(TAG, "Successfully downloaded image bytes for imageId: " + imageId);
            try {
                ImageData imageData = ImageDataFactory.create(bytes);
                Image image = new Image(imageData);
                future.complete(image);
                Log.d(TAG, "Successfully created Image object for imageId: " + imageId);
            } catch (Exception e) {
                Log.e(TAG, "Failed to create image from bytes for imageId: " + imageId, e);
                future.completeExceptionally(e);
            }
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "Failed to download image from Firebase for imageId: " + imageId, exception);
            future.completeExceptionally(exception);
        });

        try {
            return future.get(DOWNLOAD_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e(TAG, "Image download failed or timed out for imageId: " + imageId, e);
            return null;
        }
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
