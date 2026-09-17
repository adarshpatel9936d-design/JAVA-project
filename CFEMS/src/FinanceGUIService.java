import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class FinanceGUIService {

    public int countTransactions() {
        return getInt("SELECT COUNT(*) FROM transactions");
    }

    public double totalIncome() {
        return getDouble(
                "SELECT COALESCE(SUM(amount),0) FROM transactions WHERE type='INCOME'");
    }

    public double totalExpense() {
        return getDouble(
                "SELECT COALESCE(SUM(amount),0) FROM transactions WHERE type='EXPENSE'");
    }

    public int countBudgets() {
        return getInt("SELECT COUNT(*) FROM budgets");
    }

    public int countVendors() {
        return getInt("SELECT COUNT(*) FROM vendors");
    }

    private int getInt(String sql) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql);
             ResultSet r = s.executeQuery()) {
            return r.next() ? r.getInt(1) : 0;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private double getDouble(String sql) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql);
             ResultSet r = s.executeQuery()) {
            return r.next() ? r.getDouble(1) : 0;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int addIncome(double amount, String category,
                         String description, String source) throws Exception {
        validateAmount(amount);

        String sql = "INSERT INTO transactions " +
                "(type, amount, category, description, transaction_date, source, vendor) " +
                "VALUES ('INCOME',?,?,?,?,?,NULL)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            s.setDouble(1, amount);
            s.setString(2, category);
            s.setString(3, description);
            s.setDate(4, Date.valueOf(LocalDate.now()));
            s.setString(5, source);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                return r.next() ? r.getInt(1) : 0;
            }
        }
    }

    public int addExpense(double amount, String category,
                          String description, String vendor) throws Exception {
        validateAmount(amount);

        String budgetSql =
                "SELECT amount, spent FROM budgets " +
                "WHERE LOWER(category)=LOWER(?) LIMIT 1";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement b = c.prepareStatement(budgetSql)) {

            b.setString(1, category);

            try (ResultSet r = b.executeQuery()) {
                if (!r.next()) {
                    throw new Exception("No budget found for this category.");
                }

                double budget = r.getDouble("amount");
                double spent = r.getDouble("spent");

                if (spent + amount > budget) {
                    throw new Exception(
                            "Budget exceeded. Remaining: Rs. " +
                            String.format("%.2f", budget - spent));
                }
            }
        }

        String transactionSql = "INSERT INTO transactions " +
                "(type, amount, category, description, transaction_date, source, vendor) " +
                "VALUES ('EXPENSE',?,?,?,?,NULL,?)";

        Connection c = DatabaseConnection.getConnection();

        try {
            c.setAutoCommit(false);

            try (PreparedStatement s = c.prepareStatement(
                    transactionSql, Statement.RETURN_GENERATED_KEYS)) {

                s.setDouble(1, amount);
                s.setString(2, category);
                s.setString(3, description);
                s.setDate(4, Date.valueOf(LocalDate.now()));
                s.setString(5, vendor);
                s.executeUpdate();

                int id = 0;
                try (ResultSet r = s.getGeneratedKeys()) {
                    if (r.next()) id = r.getInt(1);
                }

                try (PreparedStatement b = c.prepareStatement(
                        "UPDATE budgets SET spent=spent+? " +
                        "WHERE LOWER(category)=LOWER(?)")) {
                    b.setDouble(1, amount);
                    b.setString(2, category);
                    b.executeUpdate();
                }

                c.commit();
                return id;
            }

        } catch (Exception e) {
            c.rollback();
            throw e;
        } finally {
            c.close();
        }
    }

    private void validateAmount(double amount)
            throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Amount must be greater than zero.");
        }
    }

    public void addBudget(String category, double amount) throws Exception {
        validateAmount(amount);

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "INSERT INTO budgets(category,amount,spent) VALUES(?,?,0)")) {
            s.setString(1, category);
            s.setDouble(2, amount);
            s.executeUpdate();
        }
    }

    public void addVendor(String name, String contact, String category)
            throws Exception {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "INSERT INTO vendors(name,contact,category) VALUES(?,?,?)")) {
            s.setString(1, name);
            s.setString(2, contact);
            s.setString(3, category);
            s.executeUpdate();
        }
    }

    public void addPayment(String vendor, double amount) throws Exception {
        validateAmount(amount);

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "INSERT INTO payments(vendor,amount,paid_amount,status) " +
                     "VALUES(?,?,0,'PENDING')")) {
            s.setString(1, vendor);
            s.setDouble(2, amount);
            s.executeUpdate();
        }
    }

    public void makePayment(int id, double amount) throws Exception {
        validateAmount(amount);

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "SELECT amount,paid_amount,status FROM payments WHERE payment_id=?")) {

            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (!r.next()) throw new Exception("Payment not found.");

                double total = r.getDouble("amount");
                double paid = r.getDouble("paid_amount");
                String status = r.getString("status");

                if ("CANCELLED".equals(status))
                    throw new Exception("Payment is cancelled.");

                if (paid + amount > total)
                    throw new Exception("Payment exceeds remaining amount.");

                double newPaid = paid + amount;
                String newStatus = newPaid == total ? "PAID" : "PARTIAL";

                try (PreparedStatement u = c.prepareStatement(
                        "UPDATE payments SET paid_amount=?,status=? " +
                        "WHERE payment_id=?")) {
                    u.setDouble(1, newPaid);
                    u.setString(2, newStatus);
                    u.setInt(3, id);
                    u.executeUpdate();
                }
            }
        }
    }

    public void cancelPayment(int id) throws Exception {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "SELECT status FROM payments WHERE payment_id=?")) {

            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (!r.next()) throw new Exception("Payment not found.");

                if ("PAID".equals(r.getString("status")))
                    throw new Exception("Paid payment cannot be cancelled.");

                try (PreparedStatement u = c.prepareStatement(
                        "UPDATE payments SET status='CANCELLED' " +
                        "WHERE payment_id=?")) {
                    u.setInt(1, id);
                    u.executeUpdate();
                }
            }
        }
    }

    public ArrayList<String[]> transactions(String search) throws Exception {
        ArrayList<String[]> rows = new ArrayList<>();

        String sql = "SELECT transaction_id,type,category,amount,description," +
                "transaction_date,source,vendor FROM transactions " +
                "WHERE CAST(transaction_id AS CHAR) LIKE ? " +
                "OR LOWER(type) LIKE LOWER(?) " +
                "OR LOWER(category) LIKE LOWER(?) " +
                "OR LOWER(COALESCE(vendor,'')) LIKE LOWER(?) " +
                "ORDER BY transaction_id DESC";

        String q = "%" + (search == null ? "" : search.trim()) + "%";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            for (int i = 1; i <= 4; i++) s.setString(i, q);

            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    rows.add(new String[]{
                            String.valueOf(r.getInt("transaction_id")),
                            r.getString("type"),
                            r.getString("category"),
                            String.format("%.2f", r.getDouble("amount")),
                            r.getString("description"),
                            String.valueOf(r.getDate("transaction_date")),
                            r.getString("source"),
                            r.getString("vendor")
                    });
                }
            }
        }
        return rows;
    }

    public ArrayList<String[]> budgets() throws Exception {
        ArrayList<String[]> rows = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "SELECT budget_id,category,amount,spent,(amount-spent) remaining " +
                     "FROM budgets ORDER BY budget_id");
             ResultSet r = s.executeQuery()) {

            while (r.next()) {
                double amount = r.getDouble("amount");
                double spent = r.getDouble("spent");

                rows.add(new String[]{
                        String.valueOf(r.getInt("budget_id")),
                        r.getString("category"),
                        String.format("%.2f", amount),
                        String.format("%.2f", spent),
                        String.format("%.2f", r.getDouble("remaining")),
                        String.format("%.2f",
                                amount == 0 ? 0 : spent * 100 / amount) + "%"
                });
            }
        }
        return rows;
    }

    public ArrayList<String[]> vendors() throws Exception {
        ArrayList<String[]> rows = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "SELECT vendor_id,name,contact,category " +
                     "FROM vendors ORDER BY vendor_id");
             ResultSet r = s.executeQuery()) {

            while (r.next()) {
                rows.add(new String[]{
                        String.valueOf(r.getInt("vendor_id")),
                        r.getString("name"),
                        r.getString("contact"),
                        r.getString("category")
                });
            }
        }
        return rows;
    }

    public ArrayList<String[]> payments() throws Exception {
        ArrayList<String[]> rows = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "SELECT payment_id,vendor,amount,paid_amount,status " +
                     "FROM payments ORDER BY payment_id DESC");
             ResultSet r = s.executeQuery()) {

            while (r.next()) {
                rows.add(new String[]{
                        String.valueOf(r.getInt("payment_id")),
                        r.getString("vendor"),
                        String.format("%.2f", r.getDouble("amount")),
                        String.format("%.2f", r.getDouble("paid_amount")),
                        r.getString("status")
                });
            }
        }
        return rows;
    }

    public ArrayList<String[]> categoryExpenses() throws Exception {
        ArrayList<String[]> rows = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "SELECT category,SUM(amount) total " +
                     "FROM transactions WHERE type='EXPENSE' " +
                     "GROUP BY category ORDER BY total DESC");
             ResultSet r = s.executeQuery()) {

            while (r.next()) {
                rows.add(new String[]{
                        r.getString("category"),
                        String.format("%.2f", r.getDouble("total"))
                });
            }
        }
        return rows;
    }
}
