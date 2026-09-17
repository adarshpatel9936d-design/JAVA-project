# College Finance & Expense Management System (CFEMS)

## 1. Problem Statement

Managing the financial activities of a college can involve a large number of income records, expenses, budgets, vendors, payments, and transactions. Handling these records manually can make it difficult to keep track of spending, monitor budgets, and maintain accurate financial information.

The **College Finance & Expense Management System (CFEMS)** is developed to provide a centralized desktop application for managing these financial activities. The system allows authorized users to record income and expenses, create budgets, manage vendors, track payments, search transactions, and view financial reports.

The application stores financial information in a MySQL database so that records can be maintained in an organized and persistent manner.

---

## 2. Scope of the Project

The scope of CFEMS covers the basic financial management activities required by a college or educational institution.

The project includes:

- User login and role-based access.
- Recording and managing income.
- Recording and managing expenses.
- Creating and monitoring category-wise budgets.
- Checking expenses against available budgets.
- Managing vendor information.
- Managing vendor payments.
- Tracking payment status.
- Searching financial transactions.
- Generating financial summaries and reports.
- Storing financial records in a MySQL database.
- Exporting transaction information to a file.
- Applying Java OOP, exception handling, multithreading, and synchronization concepts.

The current project is designed as a desktop-based application using Java Swing.

---

## 3. Target Users

The system is intended for users involved in managing or viewing college financial information.

### Admin

The Admin has the highest level of access and can:

- Create and manage budgets.
- Manage financial records.
- Manage vendors and payments.
- View transactions and reports.

### Accountant

The Accountant can perform regular financial operations such as:

- Adding income.
- Adding expenses.
- Managing vendors.
- Managing payments.
- Viewing financial information.

### Viewer

The Viewer has read-only access to financial information and can:

- View transactions.
- View budgets.
- View vendors.
- View payments.
- View financial reports.

---

## 4. High-Level Features

### 4.1 User Authentication

Provides login functionality and identifies the user's role before allowing access to the application.

### 4.2 Dashboard

Provides a quick overview of the financial status of the college, including income, expenses, balance, and transaction information.

### 4.3 Income Management

Allows authorized users to record income received from sources such as sponsorships, registrations, funding, and donations.

### 4.4 Expense Management

Allows authorized users to record expenses along with their category, description, vendor, and amount.

### 4.5 Budget Management

Allows administrators to create category-wise budgets and monitor spending against the allocated amount.

### 4.6 Budget Validation

Checks whether sufficient budget is available before an expense is added and prevents expenses from exceeding the available budget.

### 4.7 Vendor Management

Allows users to add and view vendor information such as vendor name, contact details, and category.

### 4.8 Payment Management

Allows users to create payments and track their status as:

- Pending
- Partial
- Paid
- Cancelled

The system also validates payment amounts to prevent overpayment.

### 4.9 Transaction Management

Maintains records of income and expense transactions with relevant financial details.

### 4.10 Transaction Search

Allows users to search for transactions using transaction-related information such as ID, type, category, or vendor.

### 4.11 Financial Reports

Provides financial summaries including:

- Total income
- Total expenses
- Balance
- Number of transactions
- Category-wise expenses

### 4.12 Database Storage

Uses MySQL to store users, budgets, vendors, transactions, and payment records.

### 4.13 File Export

Allows transaction information to be exported to a text file for simple record keeping.

---

## 5. Project Goal

The main goal of CFEMS is to provide a simple and organized way to manage college financial information while demonstrating practical Java programming concepts and database integration.
