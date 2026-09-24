package com.vehiclerental;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        CustomerDAO customerDAO = new CustomerDAO();
        VehicleDAO vehicleDAO = new VehicleDAO();
        RentalDAO rentalDAO = new RentalDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        ReportDAO reportDAO = new ReportDAO();

        System.out.println("======================================");
        System.out.println("   VEHICLE RENTAL MANAGEMENT SYSTEM");
        System.out.println("======================================");

        try (Connection connection = DBConnection.getConnection()) {

            System.out.println("Database connected successfully!");

            int choice;

            do {

                System.out.println();
                System.out.println("------------- MAIN MENU -------------");
                System.out.println("1. Customer Management");
                System.out.println("2. Vehicle Management");
                System.out.println("3. Rental Management");
                System.out.println("4. Payment Management");
                System.out.println("5. Reports / Dashboard");
                System.out.println("6. Exit");
                System.out.println("-------------------------------------");

                choice = readInt(scanner, "Enter your choice: ");

                switch (choice) {

                    case 1:
                        customerMenu(scanner, customerDAO);
                        break;

                    case 2:
                        vehicleMenu(scanner, vehicleDAO);
                        break;

                    case 3:
                        rentalMenu(scanner, rentalDAO);
                        break;

                    case 4:
                        paymentMenu(scanner, paymentDAO);
                        break;

                    case 5:
                        reportDAO.showDashboard();
                        break;

                    case 6:
                        System.out.println();
                        System.out.println(
                                "Thank you for using Vehicle Rental Management System."
                        );
                        break;

                    default:
                        System.out.println(
                                "Invalid choice. Please enter 1-6."
                        );
                }

            } while (choice != 6);

        } catch (SQLException e) {

            System.out.println("Database connection failed!");
            e.printStackTrace();

        } finally {

            scanner.close();
        }
    }

    // =====================================================
    // CUSTOMER MENU
    // =====================================================

    private static void customerMenu(
            Scanner scanner,
            CustomerDAO customerDAO) {

        int choice;

        do {

            System.out.println();
            System.out.println("-------- CUSTOMER MANAGEMENT --------");
            System.out.println("1. Add Customer");
            System.out.println("2. View Customers");
            System.out.println("3. Search Customers");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("6. Back to Main Menu");
            System.out.println("-------------------------------------");

            choice = readInt(
                    scanner,
                    "Enter your choice: "
            );

            switch (choice) {

                case 1:

                    System.out.println();
                    System.out.println("-------- ADD CUSTOMER --------");

                    String name =
                            readName(
                                    scanner,
                                    "Enter name: "
                            );

                    String email =
                            readEmail(
                                    scanner,
                                    "Enter email: "
                            );

                    String phone =
                            readPhone(
                                    scanner,
                                    "Enter 10-digit phone number: "
                            );

                    String licenseNumber =
                            readNonEmptyString(
                                    scanner,
                                    "Enter license number: "
                            );

                    Customer customer =
                            new Customer(
                                    name,
                                    email,
                                    phone,
                                    licenseNumber
                            );

                    customerDAO.addCustomer(customer);

                    break;

                case 2:

                    customerDAO.viewCustomers();

                    break;

                case 3:

                    System.out.println();
                    System.out.println(
                            "-------- SEARCH CUSTOMER --------"
                    );

                    String customerKeyword =
                            readNonEmptyString(
                                    scanner,
                                    "Enter name/email/phone/license to search: "
                            );

                    customerDAO.searchCustomers(
                            customerKeyword
                    );

                    break;

                case 4:

                    System.out.println();
                    System.out.println(
                            "------- UPDATE CUSTOMER -------"
                    );

                    int customerId =
                            readPositiveInt(
                                    scanner,
                                    "Enter customer ID: "
                            );

                    String newName =
                            readName(
                                    scanner,
                                    "Enter new name: "
                            );

                    String newEmail =
                            readEmail(
                                    scanner,
                                    "Enter new email: "
                            );

                    String newPhone =
                            readPhone(
                                    scanner,
                                    "Enter new 10-digit phone number: "
                            );

                    String newLicense =
                            readNonEmptyString(
                                    scanner,
                                    "Enter new license number: "
                            );

                    customerDAO.updateCustomer(
                            customerId,
                            newName,
                            newEmail,
                            newPhone,
                            newLicense
                    );

                    break;

                case 5:

                    System.out.println();
                    System.out.println(
                            "------- DELETE CUSTOMER -------"
                    );

                    int deleteCustomerId =
                            readPositiveInt(
                                    scanner,
                                    "Enter customer ID: "
                            );

                    customerDAO.deleteCustomer(
                            deleteCustomerId
                    );

                    break;

                case 6:

                    System.out.println(
                            "Returning to main menu..."
                    );

                    break;

                default:

                    System.out.println(
                            "Invalid choice. Please enter 1-6."
                    );
            }

        } while (choice != 6);
    }

    // =====================================================
    // VEHICLE MENU
    // =====================================================

    private static void vehicleMenu(
            Scanner scanner,
            VehicleDAO vehicleDAO) {

        int choice;

        do {

            System.out.println();
            System.out.println(
                    "--------- VEHICLE MANAGEMENT --------"
            );

            System.out.println("1. Add Vehicle");
            System.out.println("2. View Vehicles");
            System.out.println("3. Search Vehicles");
            System.out.println("4. Update Vehicle");
            System.out.println("5. Delete Vehicle");
            System.out.println("6. Back to Main Menu");
            System.out.println("-------------------------------------");

            choice = readInt(
                    scanner,
                    "Enter your choice: "
            );

            switch (choice) {

                case 1:

                    System.out.println();
                    System.out.println(
                            "--------- ADD VEHICLE ---------"
                    );

                    String vehicleNumber =
                            readNonEmptyString(
                                    scanner,
                                    "Enter vehicle number: "
                            );

                    String brand =
                            readName(
                                    scanner,
                                    "Enter brand: "
                            );

                    String model =
                            readNonEmptyString(
                                    scanner,
                                    "Enter model: "
                            );

                    String type =
                            readNonEmptyString(
                                    scanner,
                                    "Enter type: "
                            );

                    double price =
                            readPositiveDouble(
                                    scanner,
                                    "Enter rental price per day: "
                            );

                    Vehicle vehicle =
                            new Vehicle(
                                    vehicleNumber,
                                    brand,
                                    model,
                                    type,
                                    price
                            );

                    vehicleDAO.addVehicle(vehicle);

                    break;

                case 2:

                    vehicleDAO.viewVehicles();

                    break;

                case 3:

                    System.out.println();
                    System.out.println(
                            "--------- SEARCH VEHICLE ---------"
                    );

                    String vehicleKeyword =
                            readNonEmptyString(
                                    scanner,
                                    "Enter vehicle number/brand/model/type to search: "
                            );

                    vehicleDAO.searchVehicles(
                            vehicleKeyword
                    );

                    break;

                case 4:

                    System.out.println();
                    System.out.println(
                            "-------- UPDATE VEHICLE --------"
                    );

                    int vehicleId =
                            readPositiveInt(
                                    scanner,
                                    "Enter vehicle ID: "
                            );

                    String newVehicleNumber =
                            readNonEmptyString(
                                    scanner,
                                    "Enter new vehicle number: "
                            );

                    String newBrand =
                            readName(
                                    scanner,
                                    "Enter new brand: "
                            );

                    String newModel =
                            readNonEmptyString(
                                    scanner,
                                    "Enter new model: "
                            );

                    String newType =
                            readNonEmptyString(
                                    scanner,
                                    "Enter new type: "
                            );

                    double newPrice =
                            readPositiveDouble(
                                    scanner,
                                    "Enter new rental price per day: "
                            );

                    vehicleDAO.updateVehicle(
                            vehicleId,
                            newVehicleNumber,
                            newBrand,
                            newModel,
                            newType,
                            newPrice
                    );

                    break;

                case 5:

                    System.out.println();
                    System.out.println(
                            "-------- DELETE VEHICLE --------"
                    );

                    int deleteVehicleId =
                            readPositiveInt(
                                    scanner,
                                    "Enter vehicle ID: "
                            );

                    vehicleDAO.deleteVehicle(
                            deleteVehicleId
                    );

                    break;

                case 6:

                    System.out.println(
                            "Returning to main menu..."
                    );

                    break;

                default:

                    System.out.println(
                            "Invalid choice. Please enter 1-6."
                    );
            }

        } while (choice != 6);
    }

    // =====================================================
    // RENTAL MENU
    // =====================================================

    private static void rentalMenu(
            Scanner scanner,
            RentalDAO rentalDAO) {

        int choice;

        do {

            System.out.println();
            System.out.println(
                    "---------- RENTAL MANAGEMENT ----------"
            );

            System.out.println("1. Create Rental");
            System.out.println("2. View Rentals");
            System.out.println("3. Search Rentals");
            System.out.println("4. Return Vehicle");
            System.out.println("5. Back to Main Menu");
            System.out.println("---------------------------------------");

            choice = readInt(
                    scanner,
                    "Enter your choice: "
            );

            switch (choice) {

                case 1:

                    System.out.println();
                    System.out.println(
                            "---------- CREATE RENTAL ----------"
                    );

                    int customerId =
                            readPositiveInt(
                                    scanner,
                                    "Enter customer ID: "
                            );

                    int vehicleId =
                            readPositiveInt(
                                    scanner,
                                    "Enter vehicle ID: "
                            );

                    LocalDate startDate =
                            readDate(
                                    scanner,
                                    "Enter start date (YYYY-MM-DD): "
                            );

                    LocalDate endDate =
                            readDate(
                                    scanner,
                                    "Enter end date (YYYY-MM-DD): "
                            );

                    if (endDate.isBefore(startDate)) {

                        System.out.println(
                                "End date cannot be before start date."
                        );

                        break;
                    }

                    rentalDAO.createRental(
                            customerId,
                            vehicleId,
                            startDate,
                            endDate
                    );

                    break;

                case 2:

                    rentalDAO.viewRentals();

                    break;

                case 3:

                    System.out.println();
                    System.out.println(
                            "--------- SEARCH RENTALS ---------"
                    );

                    String rentalKeyword =
                            readNonEmptyString(
                                    scanner,
                                    "Enter customer/vehicle/status to search: "
                            );

                    rentalDAO.searchRentals(
                            rentalKeyword
                    );

                    break;

                case 4:

                    System.out.println();
                    System.out.println(
                            "---------- RETURN VEHICLE ----------"
                    );

                    int rentalId =
                            readPositiveInt(
                                    scanner,
                                    "Enter rental ID: "
                            );

                    rentalDAO.returnVehicle(
                            rentalId
                    );

                    break;

                case 5:

                    System.out.println(
                            "Returning to main menu..."
                    );

                    break;

                default:

                    System.out.println(
                            "Invalid choice. Please enter 1-5."
                    );
            }

        } while (choice != 5);
    }

    // =====================================================
    // PAYMENT MENU
    // =====================================================

    private static void paymentMenu(
            Scanner scanner,
            PaymentDAO paymentDAO) {

        int choice;

        do {

            System.out.println();
            System.out.println(
                    "--------- PAYMENT MANAGEMENT ---------"
            );

            System.out.println("1. Make Payment");
            System.out.println("2. View Payments");
            System.out.println("3. Check Rental Balance");
            System.out.println("4. Back to Main Menu");
            System.out.println("--------------------------------------");

            choice = readInt(
                    scanner,
                    "Enter your choice: "
            );

            switch (choice) {

                case 1:

                    System.out.println();
                    System.out.println(
                            "----------- MAKE PAYMENT -----------"
                    );

                    int rentalId =
                            readPositiveInt(
                                    scanner,
                                    "Enter rental ID: "
                            );

                    double amount =
                            readPositiveDouble(
                                    scanner,
                                    "Enter payment amount: "
                            );

                    LocalDate paymentDate =
                            readDate(
                                    scanner,
                                    "Enter payment date (YYYY-MM-DD): "
                            );

                    String paymentMethod =
                            readPaymentMethod(scanner);

                    paymentDAO.makePayment(
                            rentalId,
                            amount,
                            paymentDate,
                            paymentMethod
                    );

                    break;

                case 2:

                    paymentDAO.viewPayments();

                    break;

                case 3:

                    System.out.println();
                    System.out.println(
                            "--------- CHECK RENTAL BALANCE ---------"
                    );

                    int balanceRentalId =
                            readPositiveInt(
                                    scanner,
                                    "Enter rental ID: "
                            );

                    paymentDAO.viewRentalBalance(
                            balanceRentalId
                    );

                    break;

                case 4:

                    System.out.println(
                            "Returning to main menu..."
                    );

                    break;

                default:

                    System.out.println(
                            "Invalid choice. Please enter 1-4."
                    );
            }

        } while (choice != 4);
    }

    // =====================================================
    // INTEGER INPUT
    // =====================================================

    private static int readInt(
            Scanner scanner,
            String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a number."
                );
            }
        }
    }

    // =====================================================
    // POSITIVE INTEGER
    // =====================================================

    private static int readPositiveInt(
            Scanner scanner,
            String message) {

        while (true) {

            int value =
                    readInt(
                            scanner,
                            message
                    );

            if (value > 0) {

                return value;
            }

            System.out.println(
                    "Value must be greater than 0."
            );
        }
    }

    // =====================================================
    // POSITIVE DOUBLE
    // =====================================================

    private static double readPositiveDouble(
            Scanner scanner,
            String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            try {

                double value =
                        Double.parseDouble(input);

                if (value > 0) {

                    return value;
                }

                System.out.println(
                        "Value must be greater than 0."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid amount. Please enter a number."
                );
            }
        }
    }

    // =====================================================
    // NON-EMPTY STRING
    // =====================================================

    private static String readNonEmptyString(
            Scanner scanner,
            String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            if (!input.isEmpty()) {

                return input;
            }

            System.out.println(
                    "This field cannot be empty."
            );
        }
    }

    // =====================================================
    // NAME VALIDATION
    // =====================================================

    private static String readName(
            Scanner scanner,
            String message) {

        while (true) {

            String input =
                    readNonEmptyString(
                            scanner,
                            message
                    );

            if (input.matches(
                    "[a-zA-Z][a-zA-Z .'-]*")) {

                return input;
            }

            System.out.println(
                    "Invalid name. Use letters, spaces, " +
                    "dots, apostrophes or hyphens only."
            );
        }
    }

    // =====================================================
    // EMAIL VALIDATION
    // =====================================================

    private static String readEmail(
            Scanner scanner,
            String message) {

        while (true) {

            String email =
                    readNonEmptyString(
                            scanner,
                            message
                    );

            if (email.matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

                return email;
            }

            System.out.println(
                    "Invalid email format."
            );
        }
    }

    // =====================================================
    // PHONE VALIDATION
    // =====================================================

    private static String readPhone(
            Scanner scanner,
            String message) {

        while (true) {

            System.out.print(message);

            String phone =
                    scanner.nextLine().trim();

            if (phone.matches("[6-9][0-9]{9}")) {

                return phone;
            }

            System.out.println(
                    "Invalid phone number. " +
                    "Enter a valid 10-digit Indian mobile number."
            );
        }
    }

    // =====================================================
    // PAYMENT METHOD
    // =====================================================

    private static String readPaymentMethod(
            Scanner scanner) {

        while (true) {

            System.out.print(
                    "Enter payment method (Cash/Card/UPI): "
            );

            String method =
                    scanner.nextLine().trim();

            if (method.equalsIgnoreCase("Cash")) {

                return "Cash";

            } else if (method.equalsIgnoreCase("Card")) {

                return "Card";

            } else if (method.equalsIgnoreCase("UPI")) {

                return "UPI";
            }

            System.out.println(
                    "Invalid payment method. " +
                    "Choose Cash, Card, or UPI."
            );
        }
    }

    // =====================================================
    // DATE VALIDATION
    // =====================================================

    private static LocalDate readDate(
            Scanner scanner,
            String message) {

        while (true) {

            System.out.print(message);

            String input =
                    scanner.nextLine().trim();

            try {

                return LocalDate.parse(input);

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid date. Use YYYY-MM-DD."
                );
            }
        }
    }
}