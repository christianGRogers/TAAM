package com.b07group47.taamcollectionmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class PDFDocument {
    private static final String TAG = "PDFDocument";
    private static final Color GRAY = new DeviceRgb(245, 245, 245);
    private static final Color GRAY_LINE = new DeviceRgb(212, 212, 212);
    private static final Color WHITE = new DeviceRgb(255, 255, 255);
    private final ImageDownloader imageDownloader;
    protected final String documentTitle;

    public PDFDocument(ImageDownloader imageDownloader) {
        this.imageDownloader = imageDownloader;
        this.documentTitle = "TAAM Item Report";
        Log.d(TAG, "Initialized with document title: " + documentTitle);
    }

    public byte[] generatePdf(List<Item> items) throws Exception {
        Log.d(TAG, "Starting PDF generation");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Map<Long, Bitmap> imageMap = downloadAllImages(items);

        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outputStream));
             Document document = new Document(pdfDocument)) {

            TableHeaderEventHandler handler = new TableHeaderEventHandler(document, documentTitle);
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
            writeData(document, items, imageMap);
        }

        Log.d(TAG, "PDF generation complete");
        return outputStream.toByteArray();
    }

    private Map<Long, Bitmap> downloadAllImages(List<Item> items) throws Exception {
        Log.d(TAG, "Downloading all images");
        Map<Long, Bitmap> imageMap = new HashMap<>();
        List<CompletableFuture<Bitmap>> downloadFutures = new ArrayList<>();

        for (Item item : items) {
            long lotNumber = item.getLotNumber();
            if (lotNumber != 0) {
                downloadFutures.add(imageDownloader.downloadImage(lotNumber));
                Log.d(TAG, "Scheduled download for image: " + lotNumber);
            }
        }

        try {
            List<Bitmap> bitmaps = CompletableFuture.allOf(downloadFutures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> {
                        List<Bitmap> results = new ArrayList<>();
                        for (CompletableFuture<Bitmap> future : downloadFutures) {
                            try {
                                results.add(future.get());
                            } catch (InterruptedException | ExecutionException e) {
                                Log.e(TAG, "Error fetching image result", e);
                                // Handle the exception as needed
                            }
                        }
                        return results;
                    }).get();

            for (int i = 0; i < items.size(); i++) {
                long lotNumber = items.get(i).getLotNumber();
                if (lotNumber != 0) {
                    imageMap.put(lotNumber, bitmaps.get(i));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "Error downloading images", e);
            throw new Exception("Failed to download images", e);
        }

        Log.d(TAG, "All images downloaded");
        return imageMap;
    }

    private void writeData(Document document, List<Item> items, Map<Long, Bitmap> imageMap) {
        Log.d(TAG, "Starting to write data to the document. Number of items: " + items.size());

        items.forEach(item -> {
            Log.d(TAG, "Processing item with Lot Number: " + item.getLotNumber());
            addItemDetails(document, item);

            long imgID = item.getLotNumber();
            if (imgID != 0) {
                Bitmap bitmap = imageMap.get(imgID);
                insertImage(document, bitmap);
            }
        });

        Log.d(TAG, "Finished writing data to the document.");
    }

    private void insertImage(Document document, Bitmap bitmap) {
        if (bitmap == null) {
            Log.w(TAG, "No image found, skipping insertion.");
            return;
        }

        Log.d(TAG, "Inserting image");
        Table imageTable = new Table(1);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();
        Image img = new Image(ImageDataFactory.create(bitmapData));

        Cell cell = new Cell()
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(GRAY_LINE, 1))
                .add(img.scaleToFit(114, 114))
                .setPadding(7);
        imageTable.addCell(cell);
        Log.d(TAG, "Image inserted successfully");

        document.add(new Paragraph().setMarginTop(10));
        document.add(imageTable);
    }

    private void addItemDetails(Document document, Item item) {
        addItemFields(document, item);
    }

    private void addItemFields(Document document, Item item) {
        Log.d(TAG, "Adding fields for item with Lot Number: " + item.getLotNumber());

        TableFields itemFields = createItemFields(item);
        Table itemDetails = createTable(itemFields);
        document.add(itemDetails);

        Log.d(TAG, "Added fields for item with Lot Number: " + item.getLotNumber());
    }

    private TableFields createItemFields(Item item) {
        TableFields itemFields = new TableFields();
        itemFields.add("Lot Number", item.getLotNumber());
        itemFields.add("Item Name", item.getTitle());
        itemFields.add("Title", item.getTitle());
        itemFields.add("Category", item.getCategory());
        itemFields.add("Period", item.getPeriod());
        itemFields.add("Description", item.getDescription());
        return itemFields;
    }

    private Table createTable(TableFields tableFields) {
        Log.d(TAG, "Creating table");
        Table table = new Table(new float[]{220F, 300F});
        AtomicInteger rowCounter = new AtomicInteger(0);

        tableFields.fieldList.forEach(field ->
                insertIfNotNull(field.displayName, field.value, table, rowCounter)
        );
        return table;
    }

    private void insertIfNotNull(String displayName, Object value, Table table, AtomicInteger rowCounter) {
        if (value != null && !String.valueOf(value).isBlank()) {
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

    private static class TableField {
        public String displayName;
        public Object value;

        private TableField(String displayName, Object value) {
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

    private static class TableFields {
        private final List<TableField> fieldList = new ArrayList<>();

        private void add(String displayName, Object value) {
            fieldList.add(new TableField(displayName, value));
            Log.d(TAG, "Added field with displayName: " + displayName);
        }
    }
}
