package com.example.warehousemanagementarifah;

public class Item {
    private String name;
    private String barcode;
    private String category;
    private int quantity;
    private String stockIn;
    private String location;
    private String description;
    private String imageUri;

    // Constructor, getters, and setters
    public Item(String barcode, String name, int quantity,String location, String imageUri) {
        this.name = name;
        this.barcode = barcode;
        this.location = location;
        this.quantity = quantity;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStockIn() {
        return stockIn;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStockIn(String stockIn) {
        this.stockIn = stockIn;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

}
