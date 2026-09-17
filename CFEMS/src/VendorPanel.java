import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VendorPanel extends JPanel
        implements DashboardGUI.Refreshable {

    private final DashboardGUI frame;
    private final FinanceGUIService service;

    private JTextField nameField;
    private JTextField contactField;
    private JTextField categoryField;
    private DefaultTableModel model;

    public VendorPanel(DashboardGUI frame) {
        this.frame = frame;
        this.service = frame.getService();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createTop(), BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Contact", "Category"}, 0) {
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

        nameField = new JTextField();
        contactField = new JTextField();
        categoryField = new JTextField();

        JButton add = new JButton("Add Vendor");
        JButton clear = new JButton("Clear");

        p.add(new JLabel("Vendor Name"));
        p.add(nameField);
        p.add(new JLabel("Contact"));
        p.add(contactField);
        p.add(new JLabel("Category"));
        p.add(categoryField);
        p.add(add);
        p.add(clear);

        add.addActionListener(e -> addVendor());
        clear.addActionListener(e -> clear());

        boolean viewer = "VIEWER".equals(frame.getUser().getRole());
        add.setEnabled(!viewer);
        nameField.setEnabled(!viewer);
        contactField.setEnabled(!viewer);
        categoryField.setEnabled(!viewer);

        return p;
    }

    private void addVendor() {
        try {
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String category = categoryField.getText().trim();

            if (name.isEmpty()) {
                throw new Exception("Vendor name is required.");
            }

            service.addVendor(name, contact, category);

            frame.showSuccess("Vendor added successfully.");
            clear();
            frame.refreshAll();

        } catch (Exception e) {
            frame.showError(e);
        }
    }

    private void clear() {
        nameField.setText("");
        contactField.setText("");
        categoryField.setText("");
    }

    @Override
    public void refreshData() {
        try {
            ArrayList<String[]> rows = service.vendors();
            model.setRowCount(0);

            for (String[] row : rows) {
                model.addRow(row);
            }
        } catch (Exception e) {
            frame.showError(e);
        }
    }
}
