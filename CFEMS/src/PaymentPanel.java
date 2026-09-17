import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PaymentPanel extends JPanel
        implements DashboardGUI.Refreshable {

    private final DashboardGUI frame;
    private final FinanceGUIService service;

    private JTextField vendorField;
    private JTextField amountField;
    private JTextField paymentIdField;
    private JTextField paymentAmountField;
    private DefaultTableModel model;

    public PaymentPanel(DashboardGUI frame) {
        this.frame = frame;
        this.service = frame.getService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createTop(), BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Vendor", "Total",
                        "Paid", "Status"}, 0) {
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
        JPanel outer = new JPanel(new BorderLayout(8, 8));

        JPanel addPanel = new JPanel(new GridLayout(1, 5, 8, 8));
        vendorField = new JTextField();
        amountField = new JTextField();
        JButton add = new JButton("Add Payment");

        addPanel.add(new JLabel("Vendor"));
        addPanel.add(vendorField);
        addPanel.add(new JLabel("Total Amount"));
        addPanel.add(amountField);
        addPanel.add(add);

        JPanel updatePanel = new JPanel(new GridLayout(1, 6, 8, 8));
        paymentIdField = new JTextField();
        paymentAmountField = new JTextField();
        JButton make = new JButton("Make Payment");
        JButton cancel = new JButton("Cancel Payment");

        updatePanel.add(new JLabel("Payment ID"));
        updatePanel.add(paymentIdField);
        updatePanel.add(new JLabel("Amount"));
        updatePanel.add(paymentAmountField);
        updatePanel.add(make);
        updatePanel.add(cancel);

        add.addActionListener(e -> addPayment());
        make.addActionListener(e -> makePayment());
        cancel.addActionListener(e -> cancelPayment());

        boolean viewer = "VIEWER".equals(frame.getUser().getRole());

        add.setEnabled(!viewer);
        make.setEnabled(!viewer);
        cancel.setEnabled(!viewer);
        vendorField.setEnabled(!viewer);
        amountField.setEnabled(!viewer);
        paymentIdField.setEnabled(!viewer);
        paymentAmountField.setEnabled(!viewer);

        outer.add(addPanel, BorderLayout.NORTH);
        outer.add(updatePanel, BorderLayout.SOUTH);

        return outer;
    }

    private void addPayment() {
        try {
            String vendor = vendorField.getText().trim();
            double amount = Double.parseDouble(amountField.getText().trim());

            if (vendor.isEmpty()) {
                throw new Exception("Vendor is required.");
            }

            service.addPayment(vendor, amount);

            frame.showSuccess("Payment added successfully.");
            vendorField.setText("");
            amountField.setText("");
            frame.refreshAll();

        } catch (NumberFormatException e) {
            frame.showError(new Exception("Enter a valid amount."));
        } catch (Exception e) {
            frame.showError(e);
        }
    }

    private void makePayment() {
        try {
            int id = Integer.parseInt(paymentIdField.getText().trim());
            double amount =
                    Double.parseDouble(paymentAmountField.getText().trim());

            service.makePayment(id, amount);

            frame.showSuccess("Payment updated successfully.");
            frame.refreshAll();

        } catch (NumberFormatException e) {
            frame.showError(
                    new Exception("Enter valid payment ID and amount."));
        } catch (Exception e) {
            frame.showError(e);
        }
    }

    private void cancelPayment() {
        try {
            int id = Integer.parseInt(paymentIdField.getText().trim());

            service.cancelPayment(id);

            frame.showSuccess("Payment cancelled successfully.");
            frame.refreshAll();

        } catch (NumberFormatException e) {
            frame.showError(new Exception("Enter a valid payment ID."));
        } catch (Exception e) {
            frame.showError(e);
        }
    }

    @Override
    public void refreshData() {
        try {
            ArrayList<String[]> rows = service.payments();
            model.setRowCount(0);

            for (String[] row : rows) {
                model.addRow(row);
            }
        } catch (Exception e) {
            frame.showError(e);
        }
    }
}
