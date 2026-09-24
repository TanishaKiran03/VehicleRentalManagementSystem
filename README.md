# Vehicle Rental Management System

A console-based Vehicle Rental Management System developed using Java, JDBC, MySQL, and Maven. The application manages customers, vehicles, rentals, payments, and provides a dashboard for viewing system statistics.

## Features

- Add, view, search, update, and delete customers
- Add, view, search, update, and delete vehicles
- Create and manage vehicle rentals
- Automatic rental amount calculation
- Vehicle availability tracking
- Return vehicle and update rental status
- Make and track rental payments
- Partial payment and remaining balance calculation
- Payment validation and overpayment prevention
- Rental and payment search
- Reports and dashboard with system statistics

## Technologies Used

- Java
- JDBC
- MySQL
- Maven
- SQL
- Git & GitHub

## Concepts Used

- Object-Oriented Programming
- Encapsulation
- DAO Pattern
- JDBC Database Connectivity
- Prepared Statements
- CRUD Operations
- SQL JOINs
- Database Transactions
- Commit and Rollback
- Exception Handling
- Input Validation

## Project Structure

```text
VehicleRentalManagementSystem/
├── pom.xml
├── .gitignore
└── src/
    └── main/
        └── java/
            └── com/
                └── vehiclerental/
                    ├── Main.java
                    ├── DBConnection.java
                    ├── Customer.java
                    ├── CustomerDAO.java
                    ├── Vehicle.java
                    ├── VehicleDAO.java
                    ├── Rental.java
                    ├── RentalDAO.java
                    ├── Payment.java
                    ├── PaymentDAO.java
                    └── ReportDAO.java

Database

The application uses MySQL with four main tables:

customers – stores customer details
vehicles – stores vehicle details and availability
rentals – stores rental information
payments – stores payment records
How to Run
1. Clone the repository
git clone https://github.com/TanishaKiran03/VehicleRentalManagementSystem.git
cd VehicleRentalManagementSystem
2. Configure MySQL

Create the vehicle_rental database and the required tables.

Set your MySQL password as an environment variable:

export DB_PASSWORD='YOUR_MYSQL_PASSWORD'
3. Compile the project
mvn clean compile
4. Run the application
mvn exec:java -Dexec.mainClass="com.vehiclerental.Main"
Application Modules

Customer Management → Vehicle Management → Rental Management → Payment Management → Reports & Dashboard

Future Enhancements
Graphical user interface
User authentication
Admin and customer roles
Online booking
Invoice generation
Email notifications
Vehicle maintenance management
Author

Tanisha Kiran
