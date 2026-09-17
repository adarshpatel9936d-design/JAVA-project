import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class FinanceManager {
    private ArrayList<Transaction> transactions;
    private ArrayList<Budget> budgets;
    private int nextTransactionId;

    private TransactionDAO transactionDAO;
    private VendorDAO vendorDAO;
    private PaymentDAO paymentDAO;
    private BudgetDAO budgetDAO;

    public FinanceManager() {
        transactions = new ArrayList<>();
        budgets = new ArrayList<>();
        nextTransactionId = 1;

        transactionDAO = new TransactionDAO();
        vendorDAO = new VendorDAO();
        paymentDAO = new PaymentDAO();
        budgetDAO = new BudgetDAO();

        loadTransactions();
        loadBudgets();
    }

    public void loadTransactions() {
        transactions = transactionDAO.getAllTransactions();

        int highestId = 0;
        for (Transaction t : transactions) {
            if (t.getId() > highestId) highestId = t.getId();
        }
        nextTransactionId = highestId + 1;
    }

    public void loadBudgets() {
        budgets = budgetDAO.getAllBudgets();
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void processTransactionInThread(Transaction transaction) {
        Thread thread = new Thread(
                new TransactionProcessor(this, transaction));
        thread.setName("Transaction-Thread");
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void addIncome() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        try {
            if (amount <= 0)
                throw new InvalidAmountException("Amount must be greater than zero.");
        } catch (InvalidAmountException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Source: ");
        String source = scanner.nextLine();

        Income income = new Income(
                nextTransactionId++, amount, category, description,
                LocalDate.now(), source);

        transactions.add(income);
        transactionDAO.saveTransaction(income);
        processTransactionInThread(income);
    }

    public void addExpense() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        try {
            if (amount <= 0)
                throw new InvalidAmountException("Amount must be greater than zero.");
        } catch (InvalidAmountException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.print("Category: ");
        String category = scanner.nextLine();

        Budget budget = findBudget(category);
        if (budget == null) {
            System.out.println("Create a budget first.");
            return;
        }

        if (!budget.canSpend(amount)) {
            System.out.println("Budget exceeded.");
            return;
        }

        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();

        try {
            budget.addExpense(amount);
        } catch (BudgetExceededException e) {
            System.out.println(e.getMessage());
            return;
        }

        Expense expense = new Expense(
                nextTransactionId++, amount, category, description,
                LocalDate.now(), vendor);

        transactions.add(expense);
        transactionDAO.saveTransaction(expense);
        budgetDAO.updateSpent(category, amount);
        processTransactionInThread(expense);
    }

    public void createBudget() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        Budget budget = new Budget(category, amount);
        budgets.add(budget);
        budgetDAO.saveBudget(budget);
    }

    private Budget findBudget(String category) {
        for (Budget budget : budgets) {
            if (budget.getCategory().equalsIgnoreCase(category))
                return budget;
        }
        return null;
    }

    public void viewBudgets() {
        for (Budget b : budgets) b.showBudget();
    }

    public void viewTransactions() {
        for (Transaction t : transactions) t.display();
    }

    public void showSummary() {
        double income = 0, expense = 0;

        for (Transaction t : transactions) {
            if (t instanceof Income) income += t.getAmount();
            if (t instanceof Expense) expense += t.getAmount();
        }

        System.out.println("Total Income: Rs. " + income);
        System.out.println("Total Expense: Rs. " + expense);
        System.out.println("Balance: Rs. " + (income - expense));
    }

    public void searchTransaction() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Transaction ID: ");
        transactionDAO.searchTransaction(scanner.nextInt());
    }

    public void addVendor() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Contact: ");
        String contact = scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();

        vendorDAO.addVendor(new Vendor(0, name, contact, category));
    }

    public void viewVendors() {
        vendorDAO.viewVendors();
    }

    public void addPayment() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();

        if (amount > 0)
            paymentDAO.savePayment(new Payment(0, vendor, amount));
    }

    public void viewPayments() {
        paymentDAO.viewPayments();
    }

    public void makePayment() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Payment ID: ");
        int id = scanner.nextInt();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        paymentDAO.makePayment(id, amount);
    }

    public void cancelPayment() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Payment ID: ");
        int id = scanner.nextInt();
        paymentDAO.cancelPayment(id);
    }
}
