package com.vegvendor.vegvendorbackend;

public class Consumer {
    private String consumerId;
    private String name;
    private String phone;
    private String password;

    public Consumer() {}

    public Consumer(String consumerId, String name, String phone, String password) {
        this.consumerId = consumerId;
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

    public String getConsumerId() { return consumerId; }
    public void setConsumerId(String consumerId) { this.consumerId = consumerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}