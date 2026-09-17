import java.time.LocalDate;

public class Expense extends Transaction {
    private String vendor;

    public Expense(int id, double amount, String category,
                   String description, LocalDate date, String vendor) {
        super(id, amount, category, description, date);
        this.vendor = vendor;
    }

    public String getVendor() { return vendor; }

    @Override
    public String getType() { return "EXPENSE"; }

    @Override
    public void display() {
        super.display();
        System.out.println("Vendor         : " + vendor);
    }
}
