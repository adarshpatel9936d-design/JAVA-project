import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {
    public User checkLogin(String username, String password) {
        String sql = "SELECT username,password,role FROM users " +
                     "WHERE username=? AND password=?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setString(1, username);
            s.setString(2, password);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    return new User(
                            r.getString("username"),
                            r.getString("password"),
                            r.getString("role"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Database login failed: " + e.getMessage());
        }
        return null;
    }
}
