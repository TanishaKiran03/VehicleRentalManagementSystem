package com.vehiclerental;

import java.time.LocalDate;

public class Payment {

    private int paymentId;
    private int rentalId;
    private double amount;
    private LocalDate paymentDate;
    private String paymentMethod;

    public Payment(int rentalId, double amount,
                   LocalDate paymentDate,
                   String paymentMethod) {

        this.rentalId = rentalId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public int getRentalId() {
        return rentalId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}