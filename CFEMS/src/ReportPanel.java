import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ReportPanel extends JPanel
        implements DashboardGUI.Refreshable {

    private final DashboardGUI frame;
    private final FinanceGUIService service;

    private JLabel income;
    private JLabel expense;
    private JLabel balance;
    private JLabel count;
    private DefaultTableModel model;

    public ReportPanel(DashboardGUI frame) {
        this.frame = frame;
        this.service = frame.getService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createSummary(), BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"Expense Category", "Total Expense"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh Report");
        refresh.addActionListener(e -> refreshData());
        add(refresh, BorderLayout.SOUTH);

        refreshData();
    }

    private JPanel createSummary() {
        JPanel p = new JPanel(new GridLayout(1, 4, 10, 10));

        income = new JLabel("", SwingConstants.CENTER);
        expense = new JLabel("", SwingConstants.CENTER);
        balance = new JLabel("", SwingConstants.CENTER);
        count = new JLabel("", SwingConstants.CENTER);

        p.add(summaryCard("Total Income", income));
        p.add(summaryCard("Total Expense", expense));
        p.add(summaryCard("Balance", balance));
        p.add(summaryCard("Transactions", count));

        return p;
    }

    private JPanel summaryCard(String title, JLabel value) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(new Font("Arial", Font.BOLD, 13));
        value.setFont(new Font("Arial", Font.BOLD, 17));

        p.add(t);
        p.add(value);
        return p;
    }

    @Override
    public void refreshData() {
        try {
            double in = service.totalIncome();
            double out = service.totalExpense();

            income.setText("Rs. " + String.format("%.2f", in));
            expense.setText("Rs. " + String.format("%.2f", out));
            balance.setText("Rs. " + String.format("%.2f", in - out));
            count.setText(String.valueOf(service.countTransactions()));

            ArrayList<String[]> rows = service.categoryExpenses();
            model.setRowCount(0);

            for (String[] row : rows) {
                model.addRow(row);
            }

        } catch (Exception e) {
            frame.showError(e);
        }
    }
}
