package com.vehiclerental;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalDAO {

    // =====================================================
    // CREATE RENTAL
    // =====================================================

    public void createRental(
            int customerId,
            int vehicleId,
            LocalDate startDate,
            LocalDate endDate) {

        if (endDate.isBefore(startDate)) {

            System.out.println(
                    "End date cannot be before start date."
            );

            return;
        }

        String customerSql =
                "SELECT customer_id FROM customers " +
                "WHERE customer_id = ?";

        String vehicleSql =
                "SELECT rental_price_per_day, status " +
                "FROM vehicles WHERE vehicle_id = ?";

        String rentalSql =
                "INSERT INTO rentals " +
                "(customer_id, vehicle_id, start_date, " +
                "end_date, total_amount, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        String updateVehicleSql =
                "UPDATE vehicles " +
                "SET status = 'RENTED' " +
                "WHERE vehicle_id = ?";

        try (Connection connection =
                     DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                // CHECK CUSTOMER
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     customerSql)) {

                    statement.setInt(1, customerId);

                    try (ResultSet resultSet =
                                 statement.executeQuery()) {

                        if (!resultSet.next()) {

                            System.out.println(
                                    "Customer not found."
                            );

                            connection.rollback();
                            return;
                        }
                    }
                }

                // CHECK VEHICLE
                double pricePerDay;

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     vehicleSql)) {

                    statement.setInt(1, vehicleId);

                    try (ResultSet resultSet =
                                 statement.executeQuery()) {

                        if (!resultSet.next()) {

                            System.out.println(
                                    "Vehicle not found."
                            );

                            connection.rollback();
                            return;
                        }

                        String status =
                                resultSet.getString("status");

                        if (!status.equalsIgnoreCase(
                                "AVAILABLE")) {

                            System.out.println(
                                    "Vehicle is currently not available."
                            );

                            connection.rollback();
                            return;
                        }

                        pricePerDay =
                                resultSet.getDouble(
                                        "rental_price_per_day"
                                );
                    }
                }

                // CALCULATE RENTAL DAYS
                long days =
                        ChronoUnit.DAYS.between(
                                startDate,
                                endDate
                        );

                if (days == 0) {
                    days = 1;
                }

                double totalAmount =
                        days * pricePerDay;

                // INSERT RENTAL
                int rentalId;

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     rentalSql,
                                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

                    statement.setInt(1, customerId);
                    statement.setInt(2, vehicleId);

                    statement.setDate(
                            3,
                            java.sql.Date.valueOf(startDate)
                    );

                    statement.setDate(
                            4,
                            java.sql.Date.valueOf(endDate)
                    );

                    statement.setDouble(
                            5,
                            totalAmount
                    );

                    statement.setString(
                            6,
                            "ACTIVE"
                    );

                    statement.executeUpdate();

                    try (ResultSet generatedKeys =
                                 statement.getGeneratedKeys()) {

                        if (generatedKeys.next()) {

                            rentalId =
                                    generatedKeys.getInt(1);

                        } else {

                            System.out.println(
                                    "Unable to create rental."
                            );

                            connection.rollback();
                            return;
                        }
                    }
                }

                // UPDATE VEHICLE STATUS
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     updateVehicleSql)) {

                    statement.setInt(1, vehicleId);

                    statement.executeUpdate();
                }

                connection.commit();

                System.out.println();
                System.out.println(
                        "Rental created successfully!"
                );

                System.out.println(
                        "Rental ID     : " + rentalId
                );

                System.out.println(
                        "Rental Days   : " + days
                );

                System.out.println(
                        "Price Per Day : ₹" + pricePerDay
                );

                System.out.println(
                        "Total Amount  : ₹" + totalAmount
                );

                System.out.println(
                        "Vehicle Status: RENTED"
                );

            } catch (SQLException e) {

                connection.rollback();

                System.out.println(
                        "Error creating rental."
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
    // VIEW ALL RENTALS
    // =====================================================

    public void viewRentals() {

        String sql =
                "SELECT r.rental_id, " +
                "c.name AS customer_name, " +
                "v.vehicle_number, " +
                "v.brand, " +
                "v.model, " +
                "r.start_date, " +
                "r.end_date, " +
                "r.total_amount, " +
                "r.status " +
                "FROM rentals r " +
                "JOIN customers c " +
                "ON r.customer_id = c.customer_id " +
                "JOIN vehicles v " +
                "ON r.vehicle_id = v.vehicle_id " +
                "ORDER BY r.rental_id DESC";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            System.out.println();
            System.out.println(
                    "========== RENTAL LIST =========="
            );

            boolean found = false;

            while (resultSet.next()) {

                found = true;

                printRental(resultSet);
            }

            if (!found) {

                System.out.println(
                        "No rentals found."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error retrieving rentals."
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // SEARCH RENTALS
    // =====================================================

    public void searchRentals(String keyword) {

        String sql =
                "SELECT r.rental_id, " +
                "c.name AS customer_name, " +
                "v.vehicle_number, " +
                "v.brand, " +
                "v.model, " +
                "r.start_date, " +
                "r.end_date, " +
                "r.total_amount, " +
                "r.status " +
                "FROM rentals r " +
                "JOIN customers c " +
                "ON r.customer_id = c.customer_id " +
                "JOIN vehicles v " +
                "ON r.vehicle_id = v.vehicle_id " +
                "WHERE c.name LIKE ? " +
                "OR v.vehicle_number LIKE ? " +
                "OR v.brand LIKE ? " +
                "OR v.model LIKE ? " +
                "OR r.status LIKE ? " +
                "ORDER BY r.rental_id DESC";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            String searchPattern =
                    "%" + keyword + "%";

            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            statement.setString(4, searchPattern);
            statement.setString(5, searchPattern);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                System.out.println();
                System.out.println(
                        "========== RENTAL SEARCH RESULTS =========="
                );

                boolean found = false;

                while (resultSet.next()) {

                    found = true;

                    printRental(resultSet);
                }

                if (!found) {

                    System.out.println(
                            "No matching rentals found."
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error searching rentals."
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // PRINT RENTAL
    // =====================================================

    private void printRental(
            ResultSet resultSet)
            throws SQLException {

        System.out.println(
                "Rental ID      : " +
                resultSet.getInt("rental_id")
        );

        System.out.println(
                "Customer       : " +
                resultSet.getString("customer_name")
        );

        System.out.println(
                "Vehicle        : " +
                resultSet.getString("vehicle_number")
                + " - "
                + resultSet.getString("brand")
                + " "
                + resultSet.getString("model")
        );

        System.out.println(
                "Start Date     : " +
                resultSet.getDate("start_date")
        );

        System.out.println(
                "End Date       : " +
                resultSet.getDate("end_date")
        );

        System.out.println(
                "Total Amount   : ₹" +
                resultSet.getDouble("total_amount")
        );

        System.out.println(
                "Status         : " +
                resultSet.getString("status")
        );

        System.out.println(
                "---------------------------------"
        );
    }


    // =====================================================
    // RETURN VEHICLE
    // =====================================================

    public void returnVehicle(int rentalId) {

        String rentalSql =
                "SELECT vehicle_id, status " +
                "FROM rentals " +
                "WHERE rental_id = ?";

        String updateRentalSql =
                "UPDATE rentals " +
                "SET status = 'COMPLETED' " +
                "WHERE rental_id = ?";

        String updateVehicleSql =
                "UPDATE vehicles " +
                "SET status = 'AVAILABLE' " +
                "WHERE vehicle_id = ?";

        try (Connection connection =
                     DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                int vehicleId;

                // FIND RENTAL
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     rentalSql)) {

                    statement.setInt(1, rentalId);

                    try (ResultSet resultSet =
                                 statement.executeQuery()) {

                        if (!resultSet.next()) {

                            System.out.println(
                                    "Rental not found."
                            );

                            connection.rollback();
                            return;
                        }

                        String status =
                                resultSet.getString(
                                        "status"
                                );

                        if (!status.equalsIgnoreCase(
                                "ACTIVE")) {

                            System.out.println(
                                    "This rental has already been completed."
                            );

                            connection.rollback();
                            return;
                        }

                        vehicleId =
                                resultSet.getInt(
                                        "vehicle_id"
                                );
                    }
                }

                // COMPLETE RENTAL
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     updateRentalSql)) {

                    statement.setInt(1, rentalId);

                    statement.executeUpdate();
                }

                // MAKE VEHICLE AVAILABLE
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     updateVehicleSql)) {

                    statement.setInt(1, vehicleId);

                    statement.executeUpdate();
                }

                connection.commit();

                System.out.println();
                System.out.println(
                        "Vehicle returned successfully!"
                );

                System.out.println(
                        "Rental Status : COMPLETED"
                );

                System.out.println(
                        "Vehicle Status: AVAILABLE"
                );

            } catch (SQLException e) {

                connection.rollback();

                System.out.println(
                        "Error returning vehicle."
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
}