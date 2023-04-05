package SnappFood;

import java.util.ArrayList;

public class Customer extends User {
    private ArrayList<CartItem> cart;

    public Customer(String username, String password) {
        super(username, password);
        cart = new ArrayList<CartItem>();
    }

    public void addToCart(CartItem cartItem) {
        cart.add(cartItem);
    }

    public CartItem getCartItem(Restaurant restaurant, Food food) {
        for (CartItem cartItem : cart)
            if (cartItem.getRestaurant().equals(restaurant) && cartItem.getFood().equals(food))
                return cartItem;
        return null;
    }

    public void removeFromCart(CartItem cartItem) {
        cart.remove(cartItem);
    }

    public void clearCart() {
        cart.clear();
    }


    // Getter Setter
    public ArrayList<CartItem> getCart() {
        return cart;
    }
// Getter Setter End
}
