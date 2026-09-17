public class Vendor {
    private int vendorId;
    private String name;
    private String contact;
    private String category;

    public Vendor(int vendorId, String name, String contact, String category) {
        this.vendorId = vendorId;
        this.name = name;
        this.contact = contact;
        this.category = category;
    }

    public int getVendorId() { return vendorId; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getCategory() { return category; }

    public void showVendor() {
        System.out.println("------------------------------");
        System.out.println("Vendor ID : " + vendorId);
        System.out.println("Name      : " + name);
        System.out.println("Contact   : " + contact);
        System.out.println("Category  : " + category);
    }
}
