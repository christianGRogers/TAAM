package com.b07group47.taamcollectionmanager;

import android.util.Log;
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

public class PDFDocument {
    private static final String TAG = "PDFDocument";

    protected static final Color GRAY = new DeviceRgb(245, 245, 245);
    protected static final Color GRAY_LINE = new DeviceRgb(212, 212, 212);
    protected static final Color WHITE = new DeviceRgb(255, 255, 255);

    private final ConcurrentHashMap<Integer, Future<Image>> imageMap = new ConcurrentHashMap<>();
    private final ImageDownloader imageDownloader;
    protected final String documentTitle;

    public PDFDocument(ImageDownloader imageDownloader) {
        this.imageDownloader = imageDownloader;
        this.documentTitle = "TAAM Item Report";
        Log.d(TAG, "Initialized with document title: " + documentTitle);
    }

    public byte[] generatePdf(List<Item> items) {
        Log.d(TAG, "Starting PDF generation");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outputStream));
             Document document = new Document(pdfDocument)) {

            TableHeaderEventHandler handler = new TableHeaderEventHandler(document, documentTitle);
            pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

            writeData(document, items);
        }

        Log.d(TAG, "PDF generation complete");
        return outputStream.toByteArray();
    }

    private void writeData(Document document, List<Item> items) {
        Log.d(TAG, "Starting to write data to the document. Number of items: " + items.size());

        items.forEach(item -> {
            Log.d(TAG, "Processing item with Lot Number: " + item.getLotNumber());
            startDownloadingImage(item.getImgID());
            addItemDetails(document, item);
        });

        Log.d(TAG, "Finished writing data to the document.");
    }

    private void startDownloadingImage(int imageId) {
        if (imageId != 0) {
            Log.d(TAG, "Starting download for image ID: " + imageId);
            imageDownloader.downloadImageInBackground(imageId, imageMap);
        }
    }

    private void addItemDetails(Document document, Item item) {
        addItemFields(document, item);
        addItemImage(document, item);
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

    private void addItemImage(Document document, Item item) {
        int imgID = item.getImgID();
        Log.d(TAG, "Adding image for item with Lot Number: " + item.getLotNumber() + ", Image ID: " + imgID);

        if (imgID != 0) {
            insertImage(document, imgID);
        }
        document.add(new Paragraph());

        Log.d(TAG, "Added image for item with Lot Number: " + item.getLotNumber());
    }

    private void insertImage(Document document, int imageId) {
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

    private Table createTable(TableFields tableFields) {
        Log.d(TAG, "Creating table");
        Table table = new Table(new float[]{220F, 300F});
        AtomicInteger rowCounter = new AtomicInteger(0);

        tableFields.fieldList.forEach(field ->
                insertIfNotNull(field.displayName, field.value, table, rowCounter)
        );
        return table;
    }

    private Paragraph getBlockTitle(String title) {
        Log.d(TAG, "Getting block title for: " + title);
        return new Paragraph(title)
                .setFontSize(13)
                .setBorderBottom(new SolidBorder(GRAY_LINE, 1))
                .setMarginTop(35);
    }

    private void insertIfNotNull(String displayName, Object value, Table table, AtomicInteger rowCounter) {
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
        private List<TableField> fieldList = new ArrayList<>();

        private void add(String displayName, Object value) {
            fieldList.add(new TableField(displayName, value));
            Log.d(TAG, "Added field with displayName: " + displayName);
        }

        private void add(TableField field) {
            fieldList.add(field);
            Log.d(TAG, "Added TableField");
        }
    }
}