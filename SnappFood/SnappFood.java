package SnappFood;

import java.util.ArrayList;
import java.util.HashMap;

public class SnappFood extends User {
    private final HashMap<String, User> users = new HashMap<String, User>();
    private final ArrayList<Customer> customers = new ArrayList<Customer>();
    private final ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

    public SnappFood(String username, String password) {
        super(username, password);
    }

    public void addDiscount(Discount discount) {
        getDiscounts().add(discount);
        discount.getCustomer().addDiscount(discount);
    }

    public void removeDiscount(Discount discount) {
        super.removeDiscount(discount);
        discount.getCustomer().removeDiscount(discount);
    }

    public void addUser(User user) {
        if (user instanceof Customer)
            addCustomer((Customer) user);
        else if (user instanceof Restaurant)
            addRestaurant((Restaurant) user);
        else
            users.put(user.getUsername(), user);
    }

    public void removeUser(User user) {
        if (user instanceof Customer)
            removeCustomer((Customer) user);
        else if (user instanceof Restaurant)
            removeRestaurant((Restaurant) user);
    }

    private void addCustomer(Customer customer) {
        users.put(customer.getUsername(), customer);
        customers.add(customer);
    }

    private void addRestaurant(Restaurant restaurant) {
        users.put(restaurant.getUsername(), restaurant);
        restaurants.add(restaurant);
    }

    private void removeCustomer(Customer customer) {
        users.remove(customer.getUsername(), customer);
        customers.remove(customer);
    }

    private void removeRestaurant(Restaurant restaurant) {
        users.remove(restaurant.getUsername(), restaurant);
        restaurants.remove(restaurant);
    }

    public User getUserByUsername(String username) {
        return users.get(username);
    }

    public Restaurant getRestaurantByUsername(String username) {
        User user = getUserByUsername(username);
        if (user instanceof Restaurant) return (Restaurant) user;
        return null;
    }

    public Customer getCustomerByUsername(String username) {
        User user = getUserByUsername(username);
        if (user instanceof Customer) return (Customer) user;
        return null;
    }

    // Getter Setter
    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    // Getter Setter End
    // Singleton
    public static SnappFood singleton;
    // Singleton End
}
