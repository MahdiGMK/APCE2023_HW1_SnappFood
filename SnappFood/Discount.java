package SnappFood;

public class Discount {
    private final Customer customer;
    private final String code;
    private final int amount;

    public Discount(Customer customer, String code, int amount) {
        this.customer = customer;
        this.code = code;
        this.amount = amount;
    }

    // Getter Setter
    public Customer getCustomer() {
        return customer;
    }

    public String getCode() {
        return code;
    }

    public int getAmount() {
        return amount;
    }
// Getter Setter End
}
