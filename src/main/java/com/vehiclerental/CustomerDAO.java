package com.vehiclerental;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    public void addCustomer(Customer customer) {

        String sql = "INSERT INTO customers " +
                "(name, email, phone, license_number) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getLicenseNumber());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Customer added successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error adding customer.");
            e.printStackTrace();
        }
    }

    public void viewCustomers() {

        String sql = "SELECT * FROM customers";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            System.out.println();
            System.out.println("========== CUSTOMER LIST ==========");

            boolean found = false;

            while (resultSet.next()) {

                found = true;

                printCustomer(resultSet);
            }

            if (!found) {
                System.out.println("No customers found.");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving customers.");
            e.printStackTrace();
        }
    }

    public void searchCustomers(String keyword) {

        String sql =
                "SELECT * FROM customers " +
                "WHERE name LIKE ? " +
                "OR email LIKE ? " +
                "OR phone LIKE ? " +
                "OR license_number LIKE ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";

            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            statement.setString(4, searchPattern);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                System.out.println();
                System.out.println(
                        "========== CUSTOMER SEARCH RESULTS =========="
                );

                boolean found = false;

                while (resultSet.next()) {

                    found = true;

                    printCustomer(resultSet);
                }

                if (!found) {
                    System.out.println(
                            "No matching customers found."
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error searching customers."
            );

            e.printStackTrace();
        }
    }

    private void printCustomer(ResultSet resultSet)
            throws SQLException {

        System.out.println(
                "Customer ID    : " +
                resultSet.getInt("customer_id")
        );

        System.out.println(
                "Name           : " +
                resultSet.getString("name")
        );

        System.out.println(
                "Email          : " +
                resultSet.getString("email")
        );

        System.out.println(
                "Phone          : " +
                resultSet.getString("phone")
        );

        System.out.println(
                "License Number : " +
                resultSet.getString("license_number")
        );

        System.out.println("-----------------------------------");
    }

    public void updateCustomer(
            int customerId,
            String name,
            String email,
            String phone,
            String licenseNumber) {

        String sql =
                "UPDATE customers SET " +
                "name = ?, email = ?, phone = ?, license_number = ? " +
                "WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);
            statement.setString(4, licenseNumber);
            statement.setInt(5, customerId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println(
                        "Customer updated successfully!"
                );
            } else {
                System.out.println(
                        "Customer not found."
                );
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error updating customer."
            );
            e.printStackTrace();
        }
    }

    public void deleteCustomer(int customerId) {

        String sql =
                "DELETE FROM customers WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println(
                        "Customer deleted successfully!"
                );
            } else {
                System.out.println(
                        "Customer not found."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Customer cannot be deleted."
            );

            System.out.println(
                    "The customer may have existing rental records."
            );
        }
    }
}