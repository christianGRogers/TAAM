package com.b07group47.taamcollectionmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import android.util.Log;
import androidx.core.content.FileProvider;

public class PDFGenerator {
    private static final String TAG = "PDFGenerator";
    private static final String PDF_DIRECTORY = "PDFGenerator";

    public static void generateReport(Context context, List<Item> items, String reportTitle) {
        Log.d(TAG, "generateReport: Starting PDF generation for report titled: " + reportTitle);

        ImageDownloader imageDownloader = new ImageDownloader();
        PDFDocument pdfDocument = new PDFDocument(imageDownloader);

        Log.d(TAG, "generateReport: Generating PDF bytes for items");
        byte[] pdfBytes = pdfDocument.generatePdf(items);

        Log.d(TAG, "generateReport: Saving PDF to storage");
        File pdfFile = savePdfToStorage(context, pdfBytes, reportTitle);

        if (pdfFile != null) {
            Log.d(TAG, "generateReport: Opening PDF");
            openPdf(context, pdfFile);
        }
    }

    private static File savePdfToStorage(Context context, byte[] pdfBytes, String reportTitle) {
        Log.d(TAG, "savePdfToStorage: Preparing to save PDF with title: " + reportTitle);

        File pdfFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), PDF_DIRECTORY);
        if (!pdfFolder.exists()) {
            Log.d(TAG, "savePdfToStorage: PDF directory does not exist. Creating directory: " + pdfFolder.getAbsolutePath());
            pdfFolder.mkdirs();
        }

        File pdfFile = new File(pdfFolder, reportTitle + ".pdf");
        Log.d(TAG, "savePdfToStorage: PDF file path: " + pdfFile.getAbsolutePath());

        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
            fos.write(pdfBytes);
            Log.d(TAG, "savePdfToStorage: PDF successfully saved");
            return pdfFile;
        } catch (IOException e) {
            Log.e(TAG, "savePdfToStorage: Error saving PDF", e);
            return null;
        }
    }

    private static void openPdf(Context context, File pdfFile) {
        Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", pdfFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "openPdf: Error opening PDF", e);
        }
    }
}
