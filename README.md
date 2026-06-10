# Phase-One-Capstone-Project
# IgirePay

## Overview

IgirePay is a Java-based digital payment application that allows users to manage accounts and perform financial transactions such as deposits, withdrawals, transfers, and viewing transaction history.

## Features

* User Login and Authentication
* Account Management
* Deposit Money
* Withdraw Money
* Transfer Money Between Accounts
* View Transaction History
* PostgreSQL Database Integration

## Technologies Used

* Java
* JavaFX
* JDBC
* PostgreSQL
* pgAdmin
* GitHub

## Project Structure

* **Model** – Contains data classes such as Customer, Account, and Transaction.
* **DAO** – Handles database operations.
* **Service** – Contains business logic.
* **UI** – Provides the user interface.

## Database

Database Name: `igirepay`

Main Tables:

* customers
* accounts
* transactions
* processed_requests

## How to Run

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Configure PostgreSQL and create the `igirepay` database.
4. Update database credentials in `DBConnection.java`.
5. Run the application.

## Author

Solange Mukeshimana
