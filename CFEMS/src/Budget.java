public class Budget {
    private String category;
    private double amount;
    private double spent;

    public Budget(String category, double amount) {
        this.category = category;
        this.amount = amount;
        this.spent = 0;
    }

    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public double getSpent() { return spent; }
    public double getRemaining() { return amount - spent; }

    public boolean canSpend(double expense) {
        return spent + expense <= amount;
    }

    public void addExpense(double expense)
            throws BudgetExceededException {
        if (!canSpend(expense)) {
            throw new BudgetExceededException(
                    "Budget exceeded for category: " + category);
        }
        spent += expense;
    }

    public void showBudget() {
        System.out.println("------------------------------");
        System.out.println("Category : " + category);
        System.out.println("Budget   : Rs. " + amount);
        System.out.println("Spent    : Rs. " + spent);
        System.out.println("Remaining: Rs. " + getRemaining());
    }
}
