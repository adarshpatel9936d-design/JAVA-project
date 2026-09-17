import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginGUI() {
        setTitle("CFEMS - Login");
        setSize(520, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JLabel title = new JLabel(
                "COLLEGE FINANCE MANAGEMENT SYSTEM",
                SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel subtitle = new JLabel(
                "Secure Finance Management Portal",
                SwingConstants.CENTER);

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.add(title);
        top.add(subtitle);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(18);
        passwordField = new JPasswordField(18);

        g.gridx = 0; g.gridy = 0;
        form.add(new JLabel("Username:"), g);
        g.gridx = 1;
        form.add(usernameField, g);

        g.gridx = 0; g.gridy = 1;
        form.add(new JLabel("Password:"), g);
        g.gridx = 1;
        form.add(passwordField, g);

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(120, 35));
        loginButton.addActionListener(e -> login());

        g.gridx = 1; g.gridy = 2;
        form.add(loginButton, g);

        JLabel hint = new JLabel(
                "Demo: admin/admin123 | accountant/acc123 | viewer/view123",
                SwingConstants.CENTER);
        hint.setFont(new Font("Arial", Font.PLAIN, 11));

        root.add(top, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(hint, BorderLayout.SOUTH);

        setContentPane(root);
        getRootPane().setDefaultButton(loginButton);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Enter username and password.");
            return;
        }

        try {
            User user = new Login().checkLogin(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();
            new DashboardGUI(user).setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
