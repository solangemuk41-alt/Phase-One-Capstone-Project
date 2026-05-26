-- IgirePay Database Schema
-- Run this file in PostgreSQL after creating the database:
-- CREATE DATABASE igirepay;
-- \c igirepay
-- \i sql/schema.sql

-- CUSTOMERS TABLE
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    pin VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ACCOUNTS TABLE
CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    customer_id INT REFERENCES customers(id) ON DELETE CASCADE,
    account_type VARCHAR(20) NOT NULL CHECK (account_type IN ('WALLET', 'SAVINGS')),
    balance DECIMAL(15,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TRANSACTIONS TABLE
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    account_id INT REFERENCES accounts(id),
    reference_id VARCHAR(100) UNIQUE NOT NULL,
    transaction_type VARCHAR(20) NOT NULL CHECK (transaction_type IN ('DEPOSIT', 'WITHDRAW', 'TRANSFER')),
    amount DECIMAL(15,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PROCESSED REQUESTS TABLE (for duplicate transaction prevention)
CREATE TABLE processed_requests (
    id SERIAL PRIMARY KEY,
    reference_id VARCHAR(100) UNIQUE NOT NULL,
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
