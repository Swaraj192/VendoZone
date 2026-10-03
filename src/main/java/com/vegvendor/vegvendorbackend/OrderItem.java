package com.vegvendor.vegvendorbackend;

public class OrderItem {
    private String itemName;
    private double qty;
    private double price;

    public OrderItem() {}

    public OrderItem(String itemName, double qty, double price) {
        this.itemName = itemName;
        this.qty = qty;
        this.price = price;
    }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public double getQty() { return qty; }
    public void setQty(double qty) { this.qty = qty; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
