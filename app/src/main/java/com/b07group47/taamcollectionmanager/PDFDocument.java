package com.b07group47.taamcollectionmanager;

import android.util.Log;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.util.List;

public class PDFDocument extends AbstractPDFDocument<Item> {
    private static final String TAG = "PDFDocument";

    public PDFDocument(ImageDownloader imageDownloader) {
        super(imageDownloader, "TAAM Item Report");
        Log.d(TAG, "Initialized with document title: " + documentTitle);
    }

    @Override
    protected void writeData(Document document, List<Item> items) {
        Log.d(TAG, "Starting to write data to the document. Number of items: " + items.size());
        
        items.forEach(item -> {
            Log.d(TAG, "Processing item with Lot Number: " + item.getLotNumber());
            startDownloadingImage(item.getImgID());
            addItemDetails(document, item);
        });
        
        Log.d(TAG, "Finished writing data to the document.");
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
}
