import java.sql.*;

public class PaymentDAO {

    public void savePayment(Payment payment) {
        String sql = "INSERT INTO payments(vendor,amount,paid_amount,status) " +
                     "VALUES(?,?,?,?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setString(1, payment.getVendor());
            s.setDouble(2, payment.getAmount());
            s.setDouble(3, payment.getPaidAmount());
            s.setString(4, payment.getStatus().toString());
            s.executeUpdate();

        } catch (Exception e) {
            System.out.println("Unable to save payment.");
            System.out.println(e.getMessage());
        }
    }

    public void viewPayments() {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "SELECT * FROM payments ORDER BY payment_id");
             ResultSet r = s.executeQuery()) {

            while (r.next()) {
                System.out.println(
                        r.getInt("payment_id") + " | " +
                        r.getString("vendor") + " | Rs. " +
                        r.getDouble("amount") + " | Paid: Rs. " +
                        r.getDouble("paid_amount") + " | " +
                        r.getString("status"));
            }

        } catch (Exception e) {
            System.out.println("Unable to read payments.");
            System.out.println(e.getMessage());
        }
    }

    public void makePayment(int id, double amount) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "SELECT amount,paid_amount,status FROM payments " +
                     "WHERE payment_id=?")) {

            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (!r.next()) {
                    System.out.println("Payment not found.");
                    return;
                }

                double total = r.getDouble("amount");
                double paid = r.getDouble("paid_amount");
                String status = r.getString("status");

                if ("CANCELLED".equals(status)) {
                    System.out.println("Payment is cancelled.");
                    return;
                }

                if (amount <= 0 || paid + amount > total) {
                    System.out.println("Invalid payment amount.");
                    return;
                }

                double newPaid = paid + amount;
                String newStatus =
                        newPaid == total ? "PAID" : "PARTIAL";

                try (PreparedStatement u = c.prepareStatement(
                        "UPDATE payments SET paid_amount=?,status=? " +
                        "WHERE payment_id=?")) {

                    u.setDouble(1, newPaid);
                    u.setString(2, newStatus);
                    u.setInt(3, id);
                    u.executeUpdate();
                }
            }

        } catch (Exception e) {
            System.out.println("Unable to update payment.");
            System.out.println(e.getMessage());
        }
    }

    public void cancelPayment(int id) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "SELECT status FROM payments WHERE payment_id=?")) {

            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (!r.next()) {
                    System.out.println("Payment not found.");
                    return;
                }

                if ("PAID".equals(r.getString("status"))) {
                    System.out.println("Paid payment cannot be cancelled.");
                    return;
                }

                try (PreparedStatement u = c.prepareStatement(
                        "UPDATE payments SET status='CANCELLED' " +
                        "WHERE payment_id=?")) {
                    u.setInt(1, id);
                    u.executeUpdate();
                }
            }

        } catch (Exception e) {
            System.out.println("Unable to cancel payment.");
            System.out.println(e.getMessage());
        }
    }
}
