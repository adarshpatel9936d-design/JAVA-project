# College Finance & Expense Management System

## About the Project

The **College Finance & Expense Management System (CFEMS)** is a Java-based desktop application developed to make it easier to manage the financial activities of a college.

The system provides a single place to record income and expenses, create budgets, manage vendors and payments, search transactions, and view financial reports.

The application has a graphical user interface built using Java Swing, while MySQL is used to store the data. JDBC is used to connect the Java application with the database.

---

## Features

### User Login and Roles

The system supports three types of users:

- **Admin** – Can manage budgets and perform financial operations.
- **Accountant** – Can add and manage financial records.
- **Viewer** – Can view financial information without making changes.

### Dashboard

The dashboard provides a quick overview of the financial data, including:

- Total income
- Total expenses
- Current balance
- Number of transactions
- Number of budgets
- Number of vendors

### Income Management

Users can record different sources of income such as:

- Sponsorships
- Registrations
- Funding
- Donations
- Other income sources

### Expense Management

Expenses can be recorded with details such as:

- Amount
- Category
- Description
- Date
- Vendor

The system checks the available budget before accepting an expense.

### Budget Management

Administrators can create category-wise budgets and monitor:

- Allocated budget
- Amount spent
- Remaining budget
- Budget utilization

An expense cannot be added if it exceeds the available budget.

### Vendor Management

Vendor information can be stored and viewed through the application.

Vendor records contain:

- Vendor ID
- Name
- Contact
- Category

### Payment Management

The system keeps track of payments made to vendors.

Payment statuses include:

- `PENDING`
- `PARTIAL`
- `PAID`
- `CANCELLED`

The application also prevents payments from exceeding the remaining amount.

### Transaction Management

All income and expense records are stored as transactions.

Transactions include:

- Transaction ID
- Type
- Amount
- Category
- Description
- Date
- Source
- Vendor

### Transaction Search

Transactions can be searched using details such as transaction ID, type, category, and vendor.

### Financial Reports

The reports section provides:

- Total income
- Total expenses
- Balance
- Number of transactions
- Category-wise expenses
- Budget utilization

---

## Technologies Used

- **Java** – Main programming language
- **Java Swing** – Graphical user interface
- **JDBC** – Database connectivity
- **MySQL** – Database
- **MySQL Connector/J** – Java-MySQL connection
- **VS Code** – Development environment
- **MySQL Workbench** – Database management

---

## Project Structure

```text
CFEMS/
│
├── src/
│   ├── Main.java
│   ├── Login.java
│   ├── LoginGUI.java
│   ├── DashboardGUI.java
│   ├── TransactionPanel.java
│   ├── BudgetPanel.java
│   ├── VendorPanel.java
│   ├── PaymentPanel.java
│   ├── ReportPanel.java
│   ├── FinanceGUIService.java
│   │
│   ├── User.java
│   ├── Transaction.java
│   ├── Income.java
│   ├── Expense.java
│   ├── Budget.java
│   ├── Vendor.java
│   ├── Payment.java
│   │
│   ├── TransactionDAO.java
│   ├── BudgetDAO.java
│   ├── VendorDAO.java
│   ├── PaymentDAO.java
│   ├── DatabaseConnection.java
│   │
│   ├── TransactionProcessor.java
│   ├── FileManager.java
│   ├── Report.java
│   │
│   ├── InvalidAmountException.java
│   ├── BudgetExceededException.java
│   └── TransactionNotFoundException.java
│
├── database/
│   └── schema.sql
│
├── lib/
│   └── mysql-connector-j-*.jar
│
├── out/
│
├── transactions.txt
│
└── README.md