public class Payment {
    public enum Status {
        PENDING, PARTIAL, PAID, CANCELLED
    }

    private int paymentId;
    private String vendor;
    private double amount;
    private double paidAmount;
    private Status status;

    public Payment(int paymentId, String vendor, double amount) {
        this.paymentId = paymentId;
        this.vendor = vendor;
        this.amount = amount;
        this.paidAmount = 0;
        this.status = Status.PENDING;
    }

    public int getPaymentId() { return paymentId; }
    public String getVendor() { return vendor; }
    public double getAmount() { return amount; }
    public double getPaidAmount() { return paidAmount; }
    public Status getStatus() { return status; }

    public void makePayment(double amount) {
        if (status == Status.CANCELLED || amount <= 0 ||
                paidAmount + amount > this.amount) return;

        paidAmount += amount;
        status = paidAmount == this.amount ? Status.PAID : Status.PARTIAL;
    }

    public void cancelPayment() {
        if (status != Status.PAID) status = Status.CANCELLED;
    }

    public void showPayment() {
        System.out.println("------------------------------");
        System.out.println("Payment ID : " + paymentId);
        System.out.println("Vendor     : " + vendor);
        System.out.println("Total      : Rs. " + amount);
        System.out.println("Paid       : Rs. " + paidAmount);
        System.out.println("Status     : " + status);
    }
}
