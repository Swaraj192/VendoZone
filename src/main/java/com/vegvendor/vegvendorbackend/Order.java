package com.vegvendor.vegvendorbackend;

import java.util.List;

public class Order {
    private String orderId;
    private String vendorId;
    private String consumerId;
    private List<OrderItem> items;
    private String status;

    public Order() {}

    public Order(String orderId, String vendorId, String consumerId, List<OrderItem> items, String status) {
        this.orderId = orderId;
        this.vendorId = vendorId;
        this.consumerId = consumerId;
        this.items = items;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }

    public String getConsumerId() { return consumerId; }
    public void setConsumerId(String consumerId) { this.consumerId = consumerId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
