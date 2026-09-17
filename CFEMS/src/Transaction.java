import java.time.LocalDate;

public abstract class Transaction {
    private int id;
    private double amount;
    private String category;
    private String description;
    private LocalDate date;

    public Transaction(int id, double amount, String category,
                       String description, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }

    public abstract String getType();

    public void display() {
        System.out.println("------------------------------");
        System.out.println("Transaction ID : " + id);
        System.out.println("Type           : " + getType());
        System.out.println("Amount         : Rs. " + amount);
        System.out.println("Category       : " + category);
        System.out.println("Description    : " + description);
        System.out.println("Date           : " + date);
    }
}
