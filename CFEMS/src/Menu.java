import java.util.Scanner;

public class Menu {
    public void show(User user, FinanceManager manager) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== CFEMS MENU =====");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. Create Budget");
            System.out.println("4. View Budgets");
            System.out.println("5. View Transactions");
            System.out.println("6. Financial Summary");
            System.out.println("7. Search Transaction");
            System.out.println("8. Add Vendor");
            System.out.println("9. View Vendors");
            System.out.println("10. Add Payment");
            System.out.println("11. Make Payment");
            System.out.println("12. View Payments");
            System.out.println("13. Cancel Payment");
            System.out.println("14. Logout");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> manager.addIncome();
                case 2 -> manager.addExpense();
                case 3 -> manager.createBudget();
                case 4 -> manager.viewBudgets();
                case 5 -> manager.viewTransactions();
                case 6 -> manager.showSummary();
                case 7 -> manager.searchTransaction();
                case 8 -> manager.addVendor();
                case 9 -> manager.viewVendors();
                case 10 -> manager.addPayment();
                case 11 -> manager.makePayment();
                case 12 -> manager.viewPayments();
                case 13 -> manager.cancelPayment();
                case 14 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
