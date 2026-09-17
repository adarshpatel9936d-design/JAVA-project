import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class BudgetPanel extends JPanel
        implements DashboardGUI.Refreshable {

    private final DashboardGUI frame;
    private final FinanceGUIService service;

    private JTextField categoryField;
    private JTextField amountField;
    private DefaultTableModel model;

    public BudgetPanel(DashboardGUI frame) {
        this.frame = frame;
        this.service = frame.getService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createTop(), BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Category", "Budget",
                        "Spent", "Remaining", "Utilization"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshData();
    }

    private JPanel createTop() {
        JPanel p = new JPanel(new GridLayout(2, 4, 8, 8));

        categoryField = new JTextField();
        amountField = new JTextField();

        JButton add = new JButton("Create Budget");
        JButton clear = new JButton("Clear");

        p.add(new JLabel("Category"));
        p.add(categoryField);
        p.add(new JLabel("Budget Amount"));
        p.add(amountField);
        p.add(new JLabel(""));
        p.add(add);
        p.add(new JLabel(""));
        p.add(clear);

        add.addActionListener(e -> addBudget());
        clear.addActionListener(e -> {
            categoryField.setText("");
            amountField.setText("");
        });

        boolean admin = "ADMIN".equals(frame.getUser().getRole());
        add.setEnabled(admin);
        categoryField.setEnabled(admin);
        amountField.setEnabled(admin);

        return p;
    }

    private void addBudget() {
        try {
            String category = categoryField.getText().trim();
            double amount = Double.parseDouble(amountField.getText().trim());

            if (category.isEmpty()) {
                throw new Exception("Category is required.");
            }

            service.addBudget(category, amount);

            frame.showSuccess("Budget created successfully.");
            categoryField.setText("");
            amountField.setText("");
            frame.refreshAll();

        } catch (NumberFormatException e) {
            frame.showError(new Exception("Enter a valid budget amount."));
        } catch (Exception e) {
            frame.showError(e);
        }
    }

    @Override
    public void refreshData() {
        try {
            ArrayList<String[]> rows = service.budgets();
            model.setRowCount(0);

            for (String[] row : rows) {
                model.addRow(row);
            }
        } catch (Exception e) {
            frame.showError(e);
        }
    }
}
