package SnappFood;

public class CartItem {
    private final Restaurant restaurant;
    private final Food food;
    private int number;

    public CartItem(Restaurant restaurant, Food food, int number) {
        this.restaurant = restaurant;
        this.food = food;
        this.number = number;
    }

    public void updateNumber(int number) {
        this.number += number;
    }

    // Getter Setter
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Food getFood() {
        return food;
    }

    public int getNumber() {
        return number;
    }
// Getter Setter End
}
