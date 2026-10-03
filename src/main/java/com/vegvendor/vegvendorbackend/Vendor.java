package com.vegvendor.vegvendorbackend;

public class Vendor {
    private String vendorId;
    private String name;
    private String phone;
    private String password;
    private String zoneName;
    private boolean isActive;

    public Vendor() {}

    public Vendor(String vendorId, String name, String phone, String password, String zoneName, boolean isActive) {
        this.vendorId = vendorId;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.zoneName = zoneName;
        this.isActive = isActive;
    }

    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}