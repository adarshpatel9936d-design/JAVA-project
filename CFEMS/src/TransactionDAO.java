import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TransactionDAO {

    public void saveTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions " +
                "(type,amount,category,description,transaction_date,source,vendor) " +
                "VALUES (?,?,?,?,?,?,?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            s.setString(1, transaction.getType());
            s.setDouble(2, transaction.getAmount());
            s.setString(3, transaction.getCategory());
            s.setString(4, transaction.getDescription());
            s.setDate(5, Date.valueOf(transaction.getDate()));

            if (transaction instanceof Income) {
                s.setString(6, ((Income) transaction).getSource());
                s.setString(7, null);
            } else {
                s.setString(6, null);
                s.setString(7, ((Expense) transaction).getVendor());
            }

            s.executeUpdate();

            try (ResultSet keys = s.getGeneratedKeys()) {
                if (keys.next()) transaction.setId(keys.getInt(1));
            }

            System.out.println("Transaction saved to MySQL.");

        } catch (Exception e) {
            System.out.println("Unable to save transaction.");
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> list = new ArrayList<>();

        String sql = "SELECT * FROM transactions ORDER BY transaction_id";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql);
             ResultSet r = s.executeQuery()) {

            while (r.next()) {
                int id = r.getInt("transaction_id");
                double amount = r.getDouble("amount");
                String category = r.getString("category");
                String description = r.getString("description");
                LocalDate date = r.getDate("transaction_date").toLocalDate();

                if ("INCOME".equals(r.getString("type"))) {
                    list.add(new Income(
                            id, amount, category, description, date,
                            r.getString("source")));
                } else {
                    list.add(new Expense(
                            id, amount, category, description, date,
                            r.getString("vendor")));
                }
            }

        } catch (Exception e) {
            System.out.println("Unable to load transactions.");
            System.out.println(e.getMessage());
        }

        return list;
    }

    public void searchTransaction(int id) {
        String sql = "SELECT * FROM transactions WHERE transaction_id=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    System.out.println("ID: " + r.getInt("transaction_id"));
                    System.out.println("Type: " + r.getString("type"));
                    System.out.println("Amount: Rs. " + r.getDouble("amount"));
                    System.out.println("Category: " + r.getString("category"));
                    System.out.println("Description: " + r.getString("description"));
                    System.out.println("Date: " + r.getDate("transaction_date"));
                    System.out.println("Source: " + r.getString("source"));
                    System.out.println("Vendor: " + r.getString("vendor"));
                } else {
                    System.out.println("Transaction not found.");
                }
            }

        } catch (Exception e) {
            System.out.println("Unable to search transaction.");
            System.out.println(e.getMessage());
        }
    }
}
