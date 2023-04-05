package SnappFood;

import java.util.ArrayList;

public abstract class User {
    protected int balance;
    private String username, password;
    private ArrayList<Discount> discounts;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        discounts = new ArrayList<Discount>();
        balance = 0;
    }

    public void addDiscount(Discount discount) {
        discounts.add(discount);
    }

    public Discount getDiscountByCode(String code) {
        for (Discount discount : discounts)
            if (discount.getCode().equals(code))
                return discount;
        return null;
    }

    public void updateBalance(int diff) {
        balance += diff;
    }

    public void removeDiscount(Discount discount) {
        discounts.remove(discount);
    }

    // Getter Setter
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Discount> getDiscounts() {
        return discounts;
    }

    public int getBalance() {
        return balance;
    }
    // Getter Setter End
}
