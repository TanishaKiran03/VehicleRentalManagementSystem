package com.vehiclerental;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDAO {

    public void showDashboard() {

        String customerSql =
                "SELECT COUNT(*) AS total_customers " +
                "FROM customers";

        String vehicleSql =
                "SELECT " +
                "COUNT(*) AS total_vehicles, " +
                "SUM(CASE WHEN status = 'AVAILABLE' THEN 1 ELSE 0 END) AS available_vehicles, " +
                "SUM(CASE WHEN status = 'RENTED' THEN 1 ELSE 0 END) AS rented_vehicles " +
                "FROM vehicles";

        String rentalSql =
                "SELECT " +
                "COUNT(*) AS total_rentals, " +
                "SUM(CASE WHEN status = 'ACTIVE' THEN 1 ELSE 0 END) AS active_rentals, " +
                "SUM(CASE WHEN status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_rentals, " +
                "COALESCE(SUM(total_amount), 0) AS total_revenue " +
                "FROM rentals";

        String paymentSql =
                "SELECT " +
                "COALESCE(SUM(amount), 0) AS total_payments " +
                "FROM payments";

        try (Connection connection =
                     DBConnection.getConnection()) {

            int totalCustomers = 0;

            int totalVehicles = 0;
            int availableVehicles = 0;
            int rentedVehicles = 0;

            int totalRentals = 0;
            int activeRentals = 0;
            int completedRentals = 0;

            double totalRevenue = 0;
            double totalPayments = 0;

            // =============================================
            // CUSTOMER REPORT
            // =============================================

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 customerSql);
                 ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    totalCustomers =
                            resultSet.getInt(
                                    "total_customers"
                            );
                }
            }

            // =============================================
            // VEHICLE REPORT
            // =============================================

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 vehicleSql);
                 ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    totalVehicles =
                            resultSet.getInt(
                                    "total_vehicles"
                            );

                    availableVehicles =
                            resultSet.getInt(
                                    "available_vehicles"
                            );

                    rentedVehicles =
                            resultSet.getInt(
                                    "rented_vehicles"
                            );
                }
            }

            // =============================================
            // RENTAL REPORT
            // =============================================

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 rentalSql);
                 ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    totalRentals =
                            resultSet.getInt(
                                    "total_rentals"
                            );

                    activeRentals =
                            resultSet.getInt(
                                    "active_rentals"
                            );

                    completedRentals =
                            resultSet.getInt(
                                    "completed_rentals"
                            );

                    totalRevenue =
                            resultSet.getDouble(
                                    "total_revenue"
                            );
                }
            }

            // =============================================
            // PAYMENT REPORT
            // =============================================

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 paymentSql);
                 ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    totalPayments =
                            resultSet.getDouble(
                                    "total_payments"
                            );
                }
            }

            // =============================================
            // CALCULATE PENDING BALANCE
            // =============================================

            double pendingBalance =
                    totalRevenue - totalPayments;

            pendingBalance =
                    Math.round(
                            pendingBalance * 100.0
                    ) / 100.0;

            // =============================================
            // DISPLAY DASHBOARD
            // =============================================

            System.out.println();
            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "          VEHICLE RENTAL DASHBOARD"
            );

            System.out.println(
                    "=========================================="
            );

            System.out.println();

            System.out.println(
                    "CUSTOMER INFORMATION"
            );

            System.out.println(
                    "------------------------------------------"
            );

            System.out.println(
                    "Total Customers    : " +
                    totalCustomers
            );

            System.out.println();

            System.out.println(
                    "VEHICLE INFORMATION"
            );

            System.out.println(
                    "------------------------------------------"
            );

            System.out.println(
                    "Total Vehicles     : " +
                    totalVehicles
            );

            System.out.println(
                    "Available Vehicles : " +
                    availableVehicles
            );

            System.out.println(
                    "Rented Vehicles    : " +
                    rentedVehicles
            );

            System.out.println();

            System.out.println(
                    "RENTAL INFORMATION"
            );

            System.out.println(
                    "------------------------------------------"
            );

            System.out.println(
                    "Total Rentals      : " +
                    totalRentals
            );

            System.out.println(
                    "Active Rentals     : " +
                    activeRentals
            );

            System.out.println(
                    "Completed Rentals  : " +
                    completedRentals
            );

            System.out.println();

            System.out.println(
                    "FINANCIAL INFORMATION"
            );

            System.out.println(
                    "------------------------------------------"
            );

            System.out.println(
                    "Total Revenue      : ₹" +
                    String.format(
                            "%.2f",
                            totalRevenue
                    )
            );

            System.out.println(
                    "Total Payments     : ₹" +
                    String.format(
                            "%.2f",
                            totalPayments
                    )
            );

            System.out.println(
                    "Pending Balance    : ₹" +
                    String.format(
                            "%.2f",
                            pendingBalance
                    )
            );

            System.out.println();

            System.out.println(
                    "=========================================="
            );
        }

        catch (SQLException e) {

            System.out.println(
                    "Error generating dashboard."
            );

            e.printStackTrace();
        }
    }
}