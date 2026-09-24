package com.vehiclerental;

import java.time.LocalDate;

public class Rental {

    private int rentalId;
    private int customerId;
    private int vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAmount;
    private String status;

    public Rental(int customerId, int vehicleId,
                  LocalDate startDate, LocalDate endDate,
                  double totalAmount) {

        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
        this.status = "ACTIVE";
    }

    public int getRentalId() {
        return rentalId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }
}