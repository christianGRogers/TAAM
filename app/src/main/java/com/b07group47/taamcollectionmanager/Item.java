package com.b07group47.taamcollectionmanager;

public class Item {

    private int lotNumber;
    private String title;
    private String category;
    private String period;
    private String description;
    private int imgID;

    public Item() {
    }

    public Item(int lot, String title, String description, String category, String period, int imgID) {
        this.lotNumber = lot;
        this.title = title;
        this.description = description;
        this.category = category;
        this.period = period;
        this.imgID = imgID;
    }

    // Getters and setters
    public int getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(int id) {
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

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }
}
