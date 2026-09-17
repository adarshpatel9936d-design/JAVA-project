public class TransactionProcessor implements Runnable {

    private FinanceManager manager;
    private Transaction transaction;

    public TransactionProcessor(FinanceManager manager,
                                Transaction transaction) {
        this.manager = manager;
        this.transaction = transaction;
    }

    @Override
    public void run() {
        synchronized (manager) {
            System.out.println(
                    Thread.currentThread().getName() +
                    " is processing transaction...");

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(
                    "Transaction processed: " + transaction.getId());
        }
    }
}
