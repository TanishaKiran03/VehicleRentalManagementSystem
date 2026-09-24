package com.vehiclerental;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PaymentDAO {

    // =====================================================
    // MAKE PAYMENT
    // =====================================================

    public void makePayment(
            int rentalId,
            double amount,
            LocalDate paymentDate,
            String paymentMethod) {

        String rentalSql =
                "SELECT rental_id, total_amount, status " +
                "FROM rentals " +
                "WHERE rental_id = ?";

        String paidSql =
                "SELECT COALESCE(SUM(amount), 0) AS total_paid " +
                "FROM payments " +
                "WHERE rental_id = ?";

        String paymentSql =
                "INSERT INTO payments " +
                "(rental_id, amount, payment_date, payment_method) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection =
                     DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                double rentalAmount;
                String rentalStatus;

                // -----------------------------------------
                // CHECK RENTAL
                // -----------------------------------------

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     rentalSql)) {

                    statement.setInt(
                            1,
                            rentalId
                    );

                    try (ResultSet resultSet =
                                 statement.executeQuery()) {

                        if (!resultSet.next()) {

                            System.out.println(
                                    "Rental not found."
                            );

                            connection.rollback();
                            return;
                        }

                        rentalAmount =
                                resultSet.getDouble(
                                        "total_amount"
                                );

                        rentalStatus =
                                resultSet.getString(
                                        "status"
                                );
                    }
                }

                // -----------------------------------------
                // CHECK RENTAL STATUS
                // -----------------------------------------

                if (!rentalStatus.equalsIgnoreCase(
                        "ACTIVE")) {

                    System.out.println(
                            "Payment cannot be made for an inactive rental."
                    );

                    connection.rollback();
                    return;
                }

                // -----------------------------------------
                // VALIDATE PAYMENT AMOUNT
                // -----------------------------------------

                if (amount <= 0) {

                    System.out.println(
                            "Payment amount must be greater than 0."
                    );

                    connection.rollback();
                    return;
                }

                // -----------------------------------------
                // GET TOTAL ALREADY PAID
                // -----------------------------------------

                double totalPaid = 0;

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     paidSql)) {

                    statement.setInt(
                            1,
                            rentalId
                    );

                    try (ResultSet resultSet =
                                 statement.executeQuery()) {

                        if (resultSet.next()) {

                            totalPaid =
                                    resultSet.getDouble(
                                            "total_paid"
                                    );
                        }
                    }
                }

                // -----------------------------------------
                // CALCULATE REMAINING BALANCE
                // -----------------------------------------

                double remainingBalance =
                        rentalAmount - totalPaid;

                // Prevent floating-point display issues
                remainingBalance =
                        Math.round(
                                remainingBalance * 100.0
                        ) / 100.0;

                // -----------------------------------------
                // CHECK FULL PAYMENT
                // -----------------------------------------

                if (remainingBalance <= 0) {

                    System.out.println();
                    System.out.println(
                            "This rental has already been fully paid."
                    );

                    System.out.println(
                            "Rental Amount : ₹" +
                            rentalAmount
                    );

                    System.out.println(
                            "Total Paid    : ₹" +
                            totalPaid
                    );

                    connection.rollback();
                    return;
                }

                // -----------------------------------------
                // PREVENT OVERPAYMENT
                // -----------------------------------------

                if (amount > remainingBalance) {

                    System.out.println();
                    System.out.println(
                            "Payment exceeds remaining balance."
                    );

                    System.out.println(
                            "Rental Amount     : ₹" +
                            rentalAmount
                    );

                    System.out.println(
                            "Already Paid      : ₹" +
                            totalPaid
                    );

                    System.out.println(
                            "Remaining Balance : ₹" +
                            remainingBalance
                    );

                    connection.rollback();
                    return;
                }

                // -----------------------------------------
                // VALIDATE PAYMENT METHOD
                // -----------------------------------------

                if (!paymentMethod.equalsIgnoreCase("Cash")
                        && !paymentMethod.equalsIgnoreCase("Card")
                        && !paymentMethod.equalsIgnoreCase("UPI")) {

                    System.out.println(
                            "Invalid payment method."
                    );

                    System.out.println(
                            "Use Cash, Card, or UPI."
                    );

                    connection.rollback();
                    return;
                }

                // -----------------------------------------
                // INSERT PAYMENT
                // -----------------------------------------

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     paymentSql)) {

                    statement.setInt(
                            1,
                            rentalId
                    );

                    statement.setDouble(
                            2,
                            amount
                    );

                    statement.setDate(
                            3,
                            java.sql.Date.valueOf(
                                    paymentDate
                            )
                    );

                    statement.setString(
                            4,
                            paymentMethod
                    );

                    statement.executeUpdate();
                }

                // -----------------------------------------
                // CALCULATE NEW BALANCE
                // -----------------------------------------

                double newTotalPaid =
                        totalPaid + amount;

                double newRemainingBalance =
                        rentalAmount - newTotalPaid;

                newTotalPaid =
                        Math.round(
                                newTotalPaid * 100.0
                        ) / 100.0;

                newRemainingBalance =
                        Math.round(
                                newRemainingBalance * 100.0
                        ) / 100.0;

                connection.commit();

                // -----------------------------------------
                // DISPLAY PAYMENT DETAILS
                // -----------------------------------------

                System.out.println();
                System.out.println(
                        "Payment recorded successfully!"
                );

                System.out.println(
                        "----------------------------------"
                );

                System.out.println(
                        "Rental ID      : " +
                        rentalId
                );

                System.out.println(
                        "Payment Amount : ₹" +
                        amount
                );

                System.out.println(
                        "Payment Method : " +
                        paymentMethod
                );

                System.out.println(
                        "Total Paid     : ₹" +
                        newTotalPaid
                );

                System.out.println(
                        "Remaining      : ₹" +
                        newRemainingBalance
                );

                System.out.println(
                        "----------------------------------"
                );

                if (newRemainingBalance == 0) {

                    System.out.println(
                            "Rental payment completed!"
                    );
                }

            } catch (SQLException e) {

                connection.rollback();

                System.out.println(
                        "Error processing payment."
                );

                e.printStackTrace();
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error."
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // VIEW PAYMENTS
    // =====================================================

    public void viewPayments() {

        String sql =
                "SELECT p.payment_id, " +
                "p.rental_id, " +
                "c.name AS customer_name, " +
                "v.vehicle_number, " +
                "r.total_amount, " +
                "p.amount, " +
                "p.payment_date, " +
                "p.payment_method " +
                "FROM payments p " +
                "JOIN rentals r " +
                "ON p.rental_id = r.rental_id " +
                "JOIN customers c " +
                "ON r.customer_id = c.customer_id " +
                "JOIN vehicles v " +
                "ON r.vehicle_id = v.vehicle_id " +
                "ORDER BY p.payment_id DESC";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            System.out.println();
            System.out.println(
                    "========== PAYMENT LIST =========="
            );

            boolean found = false;

            while (resultSet.next()) {

                found = true;

                int rentalId =
                        resultSet.getInt(
                                "rental_id"
                        );

                System.out.println(
                        "Payment ID     : " +
                        resultSet.getInt(
                                "payment_id"
                        )
                );

                System.out.println(
                        "Rental ID      : " +
                        rentalId
                );

                System.out.println(
                        "Customer       : " +
                        resultSet.getString(
                                "customer_name"
                        )
                );

                System.out.println(
                        "Vehicle        : " +
                        resultSet.getString(
                                "vehicle_number"
                        )
                );

                System.out.println(
                        "Rental Amount  : ₹" +
                        resultSet.getDouble(
                                "total_amount"
                        )
                );

                System.out.println(
                        "Payment Amount : ₹" +
                        resultSet.getDouble(
                                "amount"
                        )
                );

                System.out.println(
                        "Payment Date   : " +
                        resultSet.getDate(
                                "payment_date"
                        )
                );

                System.out.println(
                        "Payment Method : " +
                        resultSet.getString(
                                "payment_method"
                        )
                );

                System.out.println(
                        "----------------------------------"
                );
            }

            if (!found) {

                System.out.println(
                        "No payments found."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error retrieving payments."
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // VIEW BALANCE FOR A RENTAL
    // =====================================================

    public void viewRentalBalance(int rentalId) {

        String rentalSql =
                "SELECT total_amount " +
                "FROM rentals " +
                "WHERE rental_id = ?";

        String paidSql =
                "SELECT COALESCE(SUM(amount), 0) AS total_paid " +
                "FROM payments " +
                "WHERE rental_id = ?";

        try (Connection connection =
                     DBConnection.getConnection()) {

            double rentalAmount;

            // -----------------------------------------
            // GET RENTAL AMOUNT
            // -----------------------------------------

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 rentalSql)) {

                statement.setInt(
                        1,
                        rentalId
                );

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (!resultSet.next()) {

                        System.out.println(
                                "Rental not found."
                        );

                        return;
                    }

                    rentalAmount =
                            resultSet.getDouble(
                                    "total_amount"
                            );
                }
            }

            // -----------------------------------------
            // GET TOTAL PAID
            // -----------------------------------------

            double totalPaid = 0;

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 paidSql)) {

                statement.setInt(
                        1,
                        rentalId
                );

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {

                        totalPaid =
                                resultSet.getDouble(
                                        "total_paid"
                                );
                    }
                }
            }

            double remaining =
                    rentalAmount - totalPaid;

            remaining =
                    Math.round(
                            remaining * 100.0
                    ) / 100.0;

            System.out.println();
            System.out.println(
                    "========== RENTAL BALANCE =========="
            );

            System.out.println(
                    "Rental ID       : " +
                    rentalId
            );

            System.out.println(
                    "Rental Amount   : ₹" +
                    rentalAmount
            );

            System.out.println(
                    "Total Paid      : ₹" +
                    totalPaid
            );

            System.out.println(
                    "Remaining       : ₹" +
                    remaining
            );

            System.out.println(
                    "------------------------------------"
            );

            if (remaining == 0) {

                System.out.println(
                        "Payment Status  : FULLY PAID"
                );

            } else {

                System.out.println(
                        "Payment Status  : BALANCE DUE"
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error checking rental balance."
            );

            e.printStackTrace();
        }
    }
}