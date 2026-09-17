import java.sql.*;

public class VendorDAO {

    public void addVendor(Vendor vendor) {
        String sql = "INSERT INTO vendors(name,contact,category) VALUES(?,?,?)";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setString(1, vendor.getName());
            s.setString(2, vendor.getContact());
            s.setString(3, vendor.getCategory());
            s.executeUpdate();

        } catch (Exception e) {
            System.out.println("Unable to save vendor.");
            System.out.println(e.getMessage());
        }
    }

    public void viewVendors() {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(
                     "SELECT * FROM vendors");
             ResultSet r = s.executeQuery()) {

            while (r.next()) {
                System.out.println(
                        r.getInt("vendor_id") + " | " +
                        r.getString("name") + " | " +
                        r.getString("contact") + " | " +
                        r.getString("category"));
            }

        } catch (Exception e) {
            System.out.println("Unable to read vendors.");
            System.out.println(e.getMessage());
        }
    }
}
