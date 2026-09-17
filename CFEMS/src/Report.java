import java.sql.*;

public class Report {
    public void showReport(FinanceManager manager) {
        String sql =
                "SELECT " +
                "SUM(CASE WHEN type='INCOME' THEN amount ELSE 0 END) income, " +
                "SUM(CASE WHEN type='EXPENSE' THEN amount ELSE 0 END) expense, " +
                "COUNT(*) total FROM transactions";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql);
             ResultSet r = s.executeQuery()) {

            if (r.next()) {
                double income = r.getDouble("income");
                double expense = r.getDouble("expense");

                System.out.println("\n===== FINANCIAL REPORT =====");
                System.out.println("Total Income: Rs. " + income);
                System.out.println("Total Expenses: Rs. " + expense);
                System.out.println("Balance: Rs. " + (income - expense));
                System.out.println("Transactions: " + r.getInt("total"));
            }

        } catch (Exception e) {
            System.out.println("Unable to generate report.");
            System.out.println(e.getMessage());
        }
    }
}
