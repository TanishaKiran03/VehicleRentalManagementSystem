package com.vehiclerental;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleDAO {

    public void addVehicle(Vehicle vehicle) {

        String sql =
                "INSERT INTO vehicles " +
                "(vehicle_number, brand, model, type, " +
                "rental_price_per_day, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, vehicle.getVehicleNumber());
            statement.setString(2, vehicle.getBrand());
            statement.setString(3, vehicle.getModel());
            statement.setString(4, vehicle.getType());
            statement.setDouble(5, vehicle.getRentalPricePerDay());
            statement.setString(6, vehicle.getStatus());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println(
                        "Vehicle added successfully!"
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error adding vehicle."
            );

            e.printStackTrace();
        }
    }

    public void viewVehicles() {

        String sql = "SELECT * FROM vehicles";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            System.out.println();
            System.out.println(
                    "========== VEHICLE LIST =========="
            );

            boolean found = false;

            while (resultSet.next()) {

                found = true;

                printVehicle(resultSet);
            }

            if (!found) {
                System.out.println(
                        "No vehicles found."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error retrieving vehicles."
            );

            e.printStackTrace();
        }
    }

    public void searchVehicles(String keyword) {

        String sql =
                "SELECT * FROM vehicles " +
                "WHERE vehicle_number LIKE ? " +
                "OR brand LIKE ? " +
                "OR model LIKE ? " +
                "OR type LIKE ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            String searchPattern =
                    "%" + keyword + "%";

            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            statement.setString(4, searchPattern);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                System.out.println();
                System.out.println(
                        "========== VEHICLE SEARCH RESULTS =========="
                );

                boolean found = false;

                while (resultSet.next()) {

                    found = true;

                    printVehicle(resultSet);
                }

                if (!found) {
                    System.out.println(
                            "No matching vehicles found."
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error searching vehicles."
            );

            e.printStackTrace();
        }
    }

    private void printVehicle(ResultSet resultSet)
            throws SQLException {

        System.out.println(
                "Vehicle ID        : " +
                resultSet.getInt("vehicle_id")
        );

        System.out.println(
                "Vehicle Number    : " +
                resultSet.getString("vehicle_number")
        );

        System.out.println(
                "Brand             : " +
                resultSet.getString("brand")
        );

        System.out.println(
                "Model             : " +
                resultSet.getString("model")
        );

        System.out.println(
                "Type              : " +
                resultSet.getString("type")
        );

        System.out.println(
                "Price Per Day     : ₹" +
                resultSet.getDouble(
                        "rental_price_per_day"
                )
        );

        System.out.println(
                "Status            : " +
                resultSet.getString("status")
        );

        System.out.println(
                "----------------------------------"
        );
    }

    public void updateVehicle(
            int vehicleId,
            String vehicleNumber,
            String brand,
            String model,
            String type,
            double price) {

        String sql =
                "UPDATE vehicles SET " +
                "vehicle_number = ?, " +
                "brand = ?, " +
                "model = ?, " +
                "type = ?, " +
                "rental_price_per_day = ? " +
                "WHERE vehicle_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, vehicleNumber);
            statement.setString(2, brand);
            statement.setString(3, model);
            statement.setString(4, type);
            statement.setDouble(5, price);
            statement.setInt(6, vehicleId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println(
                        "Vehicle updated successfully!"
                );
            } else {
                System.out.println(
                        "Vehicle not found."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error updating vehicle."
            );

            e.printStackTrace();
        }
    }

    public void deleteVehicle(int vehicleId) {

        String checkSql =
                "SELECT status FROM vehicles " +
                "WHERE vehicle_id = ?";

        String deleteSql =
                "DELETE FROM vehicles " +
                "WHERE vehicle_id = ?";

        try (Connection connection = DBConnection.getConnection()) {

            try (PreparedStatement checkStatement =
                         connection.prepareStatement(checkSql)) {

                checkStatement.setInt(1, vehicleId);

                try (ResultSet resultSet =
                             checkStatement.executeQuery()) {

                    if (!resultSet.next()) {

                        System.out.println(
                                "Vehicle not found."
                        );

                        return;
                    }

                    String status =
                            resultSet.getString("status");

                    if (status.equalsIgnoreCase("RENTED")) {

                        System.out.println(
                                "Cannot delete a rented vehicle."
                        );

                        return;
                    }
                }
            }

            try (PreparedStatement deleteStatement =
                         connection.prepareStatement(deleteSql)) {

                deleteStatement.setInt(1, vehicleId);

                int rows =
                        deleteStatement.executeUpdate();

                if (rows > 0) {

                    System.out.println(
                            "Vehicle deleted successfully!"
                    );

                } else {

                    System.out.println(
                            "Vehicle not found."
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Vehicle cannot be deleted because " +
                    "it may have existing rental records."
            );
        }
    }
}