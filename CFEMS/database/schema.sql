CREATE DATABASE IF NOT EXISTS cfems;
USE cfems;

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

INSERT IGNORE INTO users (username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('accountant', 'acc123', 'ACCOUNTANT'),
('viewer', 'view123', 'VIEWER');

CREATE TABLE IF NOT EXISTS budgets (
    budget_id INT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(50) NOT NULL,
    amount DOUBLE NOT NULL,
    spent DOUBLE DEFAULT 0
);

CREATE TABLE IF NOT EXISTS vendors (
    vendor_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(20),
    category VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(20) NOT NULL,
    amount DOUBLE NOT NULL,
    category VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    transaction_date DATE,
    source VARCHAR(100),
    vendor VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    vendor VARCHAR(100) NOT NULL,
    amount DOUBLE NOT NULL,
    paid_amount DOUBLE DEFAULT 0,
    status VARCHAR(20) NOT NULL
);
