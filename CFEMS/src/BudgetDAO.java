import java.sql.*;
import java.util.ArrayList;

public class BudgetDAO {

    public void saveBudget(Budget budget) {
        String sql = "INSERT INTO budgets(category,amount,spent) VALUES(?,?,?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setString(1, budget.getCategory());
            s.setDouble(2, budget.getAmount());
            s.setDouble(3, budget.getSpent());
            s.executeUpdate();

        } catch (Exception e) {
            System.out.println("Unable to save budget.");
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Budget> getAllBudgets() {
        ArrayList<Budget> list = new ArrayList<>();

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "SELECT category,amount,spent FROM budgets");
             ResultSet r = s.executeQuery()) {

            while (r.next()) {
                Budget b = new Budget(
                        r.getString("category"),
                        r.getDouble("amount"));

                double spent = r.getDouble("spent");

                if (spent > 0) {
                    try {
                        b.addExpense(spent);
                    } catch (BudgetExceededException ignored) {
                    }
                }

                list.add(b);
            }

        } catch (Exception e) {
            System.out.println("Unable to load budgets.");
            System.out.println(e.getMessage());
        }

        return list;
    }

    public void updateSpent(String category, double expense) {
        String sql = "UPDATE budgets SET spent=spent+? WHERE category=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setDouble(1, expense);
            s.setString(2, category);
            s.executeUpdate();

        } catch (Exception e) {
            System.out.println("Unable to update budget.");
            System.out.println(e.getMessage());
        }
    }
}
