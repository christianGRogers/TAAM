package com.b07group47.taamcollectionmanager;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import android.util.Log;

public abstract class AbstractPDFDocument<T> {

    private static final String TAG = "AbstractPDFDocument";

    protected static final Color GRAY = new DeviceRgb(245, 245, 245);
    protected static final Color GRAY_LINE = new DeviceRgb(212, 212, 212);
    protected static final Color WHITE = new DeviceRgb(255, 255, 255);

    private final ConcurrentHashMap<Integer, Future<Image>> imageMap = new ConcurrentHashMap<>();
    private final ImageDownloader imageDownloader;
    protected final String documentTitle;

    protected AbstractPDFDocument(ImageDownloader imageDownloader, String documentTitle) {
        this.imageDownloader = imageDownloader;
        this.documentTitle = documentTitle;
        Log.d(TAG, "Initialized with title: " + documentTitle);
    }

    public byte[] generatePdf(List<T> data) {
        Log.d(TAG, "Starting PDF generation");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outputStream));
             Document document = new Document(pdfDocument)) {
            
            TableHeaderEventHandler handler = new TableHeaderEventHandler(document, documentTitle);
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

            writeData(document, data);
        }

        Log.d(TAG, "PDF generation complete");
        return outputStream.toByteArray();
    }

    public void startDownloadingImage(int imageId) {
        if (imageId != 0) {
            Log.d(TAG, "Starting download for image ID: " + imageId);
            imageDownloader.downloadImageInBackground(imageId, imageMap);
        }
    }

    protected void insertImage(Document document, int imageId) {
        Log.d(TAG, "Inserting image with ID: " + imageId);
        Table imageTable = new Table(1);

        try {
            Image img = imageMap.get(imageId).get();
            Cell cell = new Cell()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(new SolidBorder(GRAY_LINE, 1))
                    .add(img.scaleToFit(114, 114))
                    .setPadding(7);
            imageTable.addCell(cell);
            Log.d(TAG, "Image inserted successfully");
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "Error inserting image", e);
        }

        document.add(new Paragraph().setMarginTop(10));
        document.add(imageTable);
    }

    protected Table createTable(TableFields tableFields) {
        Log.d(TAG, "Creating table");
        Table table = new Table(new float[]{220F, 300F});
        AtomicInteger rowCounter = new AtomicInteger(0);

        tableFields.fieldList.forEach(field -> 
            insertIfNotNull(field.displayName, field.value, table, rowCounter)
        );
        return table;
    }

    protected Paragraph getBlockTitle(String title) {
        Log.d(TAG, "Getting block title for: " + title);
        return new Paragraph(title)
                .setFontSize(13)
                .setBorderBottom(new SolidBorder(GRAY_LINE, 1))
                .setMarginTop(35);
    }

    protected void insertIfNotNull(String displayName, Object value, Table table, AtomicInteger rowCounter) {
        if (value != null) {
            Log.d(TAG, "Inserting field with display name: " + displayName);
            Color color = (rowCounter.getAndIncrement() % 2 == 0) ? GRAY : WHITE;

            Cell nameCell = new Cell()
                    .setBorder(Border.NO_BORDER)
                    .setBackgroundColor(color)
                    .add(new Paragraph(displayName));

            Cell valueCell = new Cell()
                    .setBorder(Border.NO_BORDER)
                    .setBackgroundColor(color)
                    .add(new Paragraph(String.valueOf(value)));

            table.addCell(nameCell);
            table.addCell(valueCell);
        }
    }

    protected static class TableField {
        public String displayName;
        public Object value;

        protected TableField(String displayName, Object value) {
            this.displayName = displayName;
            this.value = value;
            Log.d(TAG, "Created TableField with displayName: " + displayName);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TableField)) return false;
            TableField that = (TableField) o;
            return Objects.equals(displayName, that.displayName) &&
                    Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(displayName, value);
        }
    }

    protected static class TableFields {
        private List<TableField> fieldList = new ArrayList<>();

        protected void add(String displayName, Object value) {
            fieldList.add(new TableField(displayName, value));
            Log.d(TAG, "Added field with displayName: " + displayName);
        }

        protected void add(TableField field) {
            fieldList.add(field);
            Log.d(TAG, "Added TableField");
        }
    }

    protected abstract void writeData(Document document, List<T> data);
}
