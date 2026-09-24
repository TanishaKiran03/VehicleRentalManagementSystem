package com.vehiclerental;

public class Vehicle {

    private int vehicleId;
    private String vehicleNumber;
    private String brand;
    private String model;
    private String type;
    private double rentalPricePerDay;
    private String status;

    public Vehicle(String vehicleNumber, String brand, String model,
                   String type, double rentalPricePerDay) {

        this.vehicleNumber = vehicleNumber;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.rentalPricePerDay = rentalPricePerDay;
        this.status = "AVAILABLE";
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public double getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public String getStatus() {
        return status;
    }
}