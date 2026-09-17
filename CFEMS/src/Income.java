import java.time.LocalDate;

public class Income extends Transaction {
    private String source;

    public Income(int id, double amount, String category,
                  String description, LocalDate date, String source) {
        super(id, amount, category, description, date);
        this.source = source;
    }

    public String getSource() { return source; }

    @Override
    public String getType() { return "INCOME"; }

    @Override
    public void display() {
        super.display();
        System.out.println("Source         : " + source);
    }
}
