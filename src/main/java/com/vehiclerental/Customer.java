package com.vehiclerental;

public class Customer {

    private int customerId;
    private String name;
    private String email;
    private String phone;
    private String licenseNumber;

    public Customer(String name, String email, String phone, String licenseNumber) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
}