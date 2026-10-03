package com.vegvendor.vegvendorbackend;

public class StockItem {
    private String itemName;
    private double pricePerKg;
    private double quantityKg;

    public StockItem() {}

    public StockItem(String itemName, double pricePerKg, double quantityKg) {
        this.itemName = itemName;
        this.pricePerKg = pricePerKg;
        this.quantityKg = quantityKg;
    }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public double getPricePerKg() { return pricePerKg; }
    public void setPricePerKg(double pricePerKg) { this.pricePerKg = pricePerKg; }

    public double getQuantityKg() { return quantityKg; }
    public void setQuantityKg(double quantityKg) { this.quantityKg = quantityKg; }
}
