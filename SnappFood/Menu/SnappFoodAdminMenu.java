package SnappFood.Menu;

import SnappFood.*;
import SnappFood.Menu.*;

import java.awt.*;
import java.util.Locale;
import java.util.regex.Matcher;

public class SnappFoodAdminMenu implements IMenu {
    private SnappFood admin = SnappFood.singleton;
    private IMenu nextMenu;

    @Override
    public IMenu run(String line) {
        nextMenu = this;
        Matcher addRestaurantMatcher = Commands.AddRestaurant.getMatcher(line);
        Matcher showRestaurantMatcher = Commands.ShowRestaurant.getMatcher(line);
        Matcher removeRestaurantMatcher = Commands.RemoveRestaurant.getMatcher(line);
        Matcher setDiscountMatcher = Commands.SetDiscount.getMatcher(line);
        Matcher showDiscountsMatcher = Commands.ShowDiscounts.getMatcher(line);

        String msg = "";
        if (addRestaurantMatcher.matches())
            msg = addRestaurant(addRestaurantMatcher);
        else if (showRestaurantMatcher.matches())
            msg = showRestaurant(showRestaurantMatcher);
        else if (removeRestaurantMatcher.matches())
            msg = removeRestaurant(removeRestaurantMatcher);
        else if (setDiscountMatcher.matches())
            msg = setDiscount(setDiscountMatcher);
        else if (showDiscountsMatcher.matches())
            msg = showDiscounts();
        else
            msg = Utils.invalidCommand;

        if (!msg.isEmpty())
            System.out.println(msg);

        return nextMenu;
    }

    @Override
    public String toString() {
        return "Snappfood admin menu";
    }

    private String addRestaurant(Matcher matcher) {
        String name = matcher.group("name");
        String password = matcher.group("password");
        String type = matcher.group("type");
        String nameStatus = Utils.validateUsername(name);
        String passwordStatus = Utils.validatePassword(password);
        if (nameStatus != null) return "add restaurant failed: " + nameStatus;
        if (admin.getUserByUsername(name) != null) return "add restaurant failed: username already exists";
        if (passwordStatus != null) return "add restaurant failed: " + passwordStatus;
        if (!Utils.matchesWith(type, "[a-z-]*")) return "add restaurant failed: invalid type format";

        Restaurant restaurant = new Restaurant(name, password, type);
        SnappFood.singleton.addUser(restaurant);
        return "add restaurant successful";
    }

    private String showRestaurant(Matcher matcher) {
        //[index]) [name]: type=[type] balance=[balance]
        String type = matcher.group("type");
        int idx = 1;
        for (Restaurant restaurant : SnappFood.singleton.getRestaurants()) {
            if (type != null && !restaurant.getType().equals(type)) continue;
            System.out.printf("%d) %s: type=%s balance=%d\n",
                    idx++, restaurant.getUsername(), restaurant.getType(), restaurant.getBalance());
        }
        return "";
    }

    private String removeRestaurant(Matcher matcher) {
        String name = matcher.group("name");
        Restaurant user = SnappFood.singleton.getRestaurantByUsername(name);
        if (user == null)
            return "remove restaurant failed: restaurant not found";
        SnappFood.singleton.removeUser(user);
        return "";
    }

    private String setDiscount(Matcher matcher) {
        String username = matcher.group("username");
        int amount = Integer.parseInt(matcher.group("amount"));
        String code = matcher.group("code");
        Customer customer = SnappFood.singleton.getCustomerByUsername(username);
        if (customer == null) return "set discount failed: username not found";
        if (amount <= 0) return "set discount failed: invalid amount";
        if (!Utils.matchesWith(code, "[a-zA-Z0-9]*")) return "set discount failed: invalid code format";

        SnappFood.singleton.addDiscount(new Discount(customer, code, amount));
        return "set discount successful";
    }

    private String showDiscounts() {
        //[index]) [code] | amount=[amount]
        int idx = 1;
        for (Discount discount : SnappFood.singleton.getDiscounts()) {
            System.out.printf("%d) %s | amount=%d --> user=%s\n", idx++,
                    discount.getCode(), discount.getAmount(), discount.getCustomer().getUsername());
        }
        return "";
    }
}
