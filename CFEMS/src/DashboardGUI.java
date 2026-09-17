import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardGUI extends JFrame {

    private final User user;
    private final FinanceGUIService service;
    private final JTabbedPane tabs;

    private JLabel incomeValue;
    private JLabel expenseValue;
    private JLabel balanceValue;
    private JLabel transactionValue;
    private JLabel budgetValue;
    private JLabel vendorValue;

    public DashboardGUI(User user) {
        this.user = user;
        this.service = new FinanceGUIService();

        setTitle("College Finance Management System - " + user.getRole());
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(10, 12, 10, 12));

        JPanel header = new JPanel(new BorderLayout());

        JLabel logged = new JLabel(
                "Logged in as: " + user.getUsername() +
                " (" + user.getRole() + ")");
        logged.setFont(new Font("Arial", Font.BOLD, 15));

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> logout());

        header.add(logged, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);

        tabs = new JTabbedPane();
        tabs.addTab("Dashboard", createDashboard());
        tabs.addTab("Transactions", new TransactionPanel(this));
        tabs.addTab("Budgets", new BudgetPanel(this));
        tabs.addTab("Vendors", new VendorPanel(this));
        tabs.addTab("Payments", new PaymentPanel(this));
        tabs.addTab("Reports", new ReportPanel(this));

        root.add(header, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);

        setContentPane(root);
        refreshDashboard();
    }

    public User getUser() {
        return user;
    }

    public FinanceGUIService getService() {
        return service;
    }

    private JPanel createDashboard() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel(
                "FINANCIAL DASHBOARD",
                SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 25));

        JPanel cards = new JPanel(new GridLayout(2, 3, 15, 15));

        incomeValue = cardValue();
        expenseValue = cardValue();
        balanceValue = cardValue();
        transactionValue = cardValue();
        budgetValue = cardValue();
        vendorValue = cardValue();

        cards.add(card("Total Income", incomeValue));
        cards.add(card("Total Expense", expenseValue));
        cards.add(card("Balance", balanceValue));
        cards.add(card("Transactions", transactionValue));
        cards.add(card("Budgets", budgetValue));
        cards.add(card("Vendors", vendorValue));

        JButton refresh = new JButton("Refresh Dashboard");
        refresh.addActionListener(e -> refreshAll());

        panel.add(title, BorderLayout.NORTH);
        panel.add(cards, BorderLayout.CENTER);
        panel.add(refresh, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel cardValue() {
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        return label;
    }

    private JPanel card(String title, JLabel value) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(18, 10, 18, 10)));

        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(new Font("Arial", Font.BOLD, 14));

        p.add(t, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    public void refreshDashboard() {
        try {
            double income = service.totalIncome();
            double expense = service.totalExpense();

            incomeValue.setText("Rs. " + String.format("%.2f", income));
            expenseValue.setText("Rs. " + String.format("%.2f", expense));
            balanceValue.setText("Rs. " +
                    String.format("%.2f", income - expense));
            transactionValue.setText(
                    String.valueOf(service.countTransactions()));
            budgetValue.setText(
                    String.valueOf(service.countBudgets()));
            vendorValue.setText(
                    String.valueOf(service.countVendors()));

        } catch (Exception e) {
            showError(e);
        }
    }

    public void refreshAll() {
        refreshDashboard();

        for (int i = 1; i < tabs.getTabCount(); i++) {
            Component c = tabs.getComponentAt(i);

            if (c instanceof Refreshable) {
                ((Refreshable) c).refreshData();
            }
        }
    }

    private void logout() {
        dispose();
        new LoginGUI().setVisible(true);
    }

    public void showError(Exception e) {
        JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public interface Refreshable {
        void refreshData();
    }
}
