import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TransactionPanel extends JPanel
        implements DashboardGUI.Refreshable {

    private final DashboardGUI frame;
    private final FinanceGUIService service;

    private JComboBox<String> typeBox;
    private JTextField amountField;
    private JTextField categoryField;
    private JTextField descriptionField;
    private JTextField sourceVendorField;
    private JTextField searchField;

    private DefaultTableModel model;

    public TransactionPanel(DashboardGUI frame) {
        this.frame = frame;
        this.service = frame.getService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createForm(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);

        refreshData();
    }

    private JPanel createForm() {
        JPanel outer = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(3, 4, 8, 8));

        typeBox = new JComboBox<>(new String[]{"INCOME", "EXPENSE"});
        amountField = new JTextField();
        categoryField = new JTextField();
        descriptionField = new JTextField();
        sourceVendorField = new JTextField();

        form.add(new JLabel("Type"));
        form.add(typeBox);
        form.add(new JLabel("Amount"));
        form.add(amountField);

        form.add(new JLabel("Category"));
        form.add(categoryField);
        form.add(new JLabel("Description"));
        form.add(descriptionField);

        form.add(new JLabel("Source / Vendor"));
        form.add(sourceVendorField);

        JButton add = new JButton("Add Transaction");
        JButton clear = new JButton("Clear");

        form.add(add);
        form.add(clear);

        add.addActionListener(e -> addTransaction());
        clear.addActionListener(e -> clearForm());

        boolean viewer = "VIEWER".equals(frame.getUser().getRole());
        add.setEnabled(!viewer);
        amountField.setEnabled(!viewer);
        categoryField.setEnabled(!viewer);
        descriptionField.setEnabled(!viewer);
        sourceVendorField.setEnabled(!viewer);
        typeBox.setEnabled(!viewer);

        JPanel search = new JPanel(new BorderLayout(8, 8));
        search.add(new JLabel("Search:"), BorderLayout.WEST);
        searchField = new JTextField();

        JButton searchButton = new JButton("Search");
        JButton allButton = new JButton("Show All");

        searchButton.addActionListener(e -> refreshData());
        allButton.addActionListener(e -> {
            searchField.setText("");
            refreshData();
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(searchButton);
        buttons.add(allButton);

        search.add(searchField, BorderLayout.CENTER);
        search.add(buttons, BorderLayout.EAST);

        outer.add(form, BorderLayout.NORTH);
        outer.add(search, BorderLayout.SOUTH);
        return outer;
    }

    private JScrollPane createTable() {
        model = new DefaultTableModel(
                new String[]{"ID", "Type", "Category", "Amount",
                        "Description", "Date", "Source", "Vendor"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        return new JScrollPane(table);
    }

    private void addTransaction() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            String type = typeBox.getSelectedItem().toString();
            String category = categoryField.getText().trim();
            String description = descriptionField.getText().trim();
            String sourceVendor = sourceVendorField.getText().trim();

            if (category.isEmpty()) {
                throw new Exception("Category is required.");
            }

            int id;

            if ("INCOME".equals(type)) {
                id = service.addIncome(
                        amount, category, description, sourceVendor);
            } else {
                id = service.addExpense(
                        amount, category, description, sourceVendor);
            }

            frame.showSuccess(
                    "Transaction added successfully.\nTransaction ID: " + id);

            clearForm();
            frame.refreshAll();

        } catch (NumberFormatException e) {
            frame.showError(new Exception("Enter a valid amount."));
        } catch (Exception e) {
            frame.showError(e);
        }
    }

    private void clearForm() {
        amountField.setText("");
        categoryField.setText("");
        descriptionField.setText("");
        sourceVendorField.setText("");
    }

    @Override
    public void refreshData() {
        try {
            ArrayList<String[]> rows =
                    service.transactions(searchField == null ? "" :
                            searchField.getText());

            model.setRowCount(0);

            for (String[] row : rows) {
                model.addRow(row);
            }

        } catch (Exception e) {
            frame.showError(e);
        }
    }
}
