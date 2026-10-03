package com.vegvendor.vegvendorbackend;

public class StockItem {
    private String itemName;
    private double pricePerKg;
    private double quantityKg;
    private String unit;

    public StockItem() {}

    public StockItem(String itemName, double pricePerKg, double quantityKg, String unit) {
        this.itemName = itemName;
        this.pricePerKg = pricePerKg;
        this.quantityKg = quantityKg;
        this.unit = unit;
    }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public double getPricePerKg() { return pricePerKg; }
    public void setPricePerKg(double pricePerKg) { this.pricePerKg = pricePerKg; }

    public double getQuantityKg() { return quantityKg; }
    public void setQuantityKg(double quantityKg) { this.quantityKg = quantityKg; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}