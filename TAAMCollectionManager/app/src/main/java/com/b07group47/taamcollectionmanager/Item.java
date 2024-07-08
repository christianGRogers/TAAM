package com.b07group47.taamcollectionmanager;

public class Item {

    private String lot_number;
    private String name;
    private String category;
    private String period;
    private String description;

    public Item() {}

    public Item(String id, String title, String author, String genre) {
        this.lot_number = id;
        this.name = title;
        this.category = author;
        this.period = genre;
        this.description = "";
    }

    // Getters and setters
    public String getId() { return lot_number; }
    public void setId(String id) { this.lot_number = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
