import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    public void exportTransactions(FinanceManager manager) {
        try (FileWriter writer = new FileWriter("transactions.txt")) {
            writer.write("COLLEGE FINANCE MANAGEMENT SYSTEM\n");
            writer.write("================================\n\n");

            for (Transaction t : manager.getTransactions()) {
                writer.write("Transaction ID : " + t.getId() + "\n");
                writer.write("Type           : " + t.getType() + "\n");
                writer.write("Amount         : Rs. " + t.getAmount() + "\n");
                writer.write("Category       : " + t.getCategory() + "\n");
                writer.write("Description    : " + t.getDescription() + "\n");
                writer.write("Date           : " + t.getDate() + "\n");
                writer.write("--------------------------------\n");
            }

            System.out.println("Transactions exported successfully.");

        } catch (IOException e) {
            System.out.println("Unable to export transactions.");
        }
    }
}
