package com.b07group47.taamcollectionmanager;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Map;

public class Item {

    private final String TAG = "Item.java";

    private long lotNumber;
    private String title;
    private String category;
    private String period;
    private String description;
    private String imgID;

    public Item() {
    }

    public Item(Map<String, Object> map) {

        this(
            (Long) map.get("lot"),
            (String) map.get("name"),
            (String) map.get("description"),
            (String) map.get("category"),
            (String) map.get("period"),
                (String) map.get("image")
        );

    }

    public Item(long lot, String title, String description, String category, String period, String imgID) {
        this.lotNumber = lot;
        this.title = title;
        this.description = description;
        this.category = category;
        this.period = period;
        this.imgID = imgID;
    }

    // Getters and setters
    public long getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(long id) {
        this.lotNumber = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("lot %d: %s", lotNumber, title);
    }
}
