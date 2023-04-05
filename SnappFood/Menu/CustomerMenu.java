package SnappFood.Menu;

import SnappFood.SnappFood;
import SnappFood.*;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIterNodeList;
import sun.awt.SunGraphicsCallback;

import java.awt.*;
import java.util.InputMismatchException;
import java.util.regex.Matcher;

public class CustomerMenu implements IMenu {
    Customer currentCustomer;

    public CustomerMenu(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    @Override
    public IMenu run(String line) {
        Matcher chargeAccountMatcher = Commands.ChargeAccount.getMatcher(line);
        Matcher showBalanceMatcher = Commands.ShowBalance.getMatcher(line);
        Matcher showRestaurantMatcher = Commands.ShowRestaurant.getMatcher(line);
        Matcher showMenuMatcher = Commands.ShowMenu.getMatcher(line);
        Matcher addToCartMatcher = Commands.AddToCart.getMatcher(line);
        Matcher removeFromCartMatcher = Commands.RemoveFromCart.getMatcher(line);
        Matcher showCartMatcher = Commands.ShowCart.getMatcher(line);
        Matcher showDiscountsMatcher = Commands.ShowDiscounts.getMatcher(line);
        Matcher purchaseCartMatcher = Commands.PurchaseCart.getMatcher(line);
        String msg;
        if (chargeAccountMatcher.matches())
            msg = chargeAccount(chargeAccountMatcher);
        else if (showBalanceMatcher.matches())
            msg = showBalance();
        else if (showRestaurantMatcher.matches())
            msg = showRestaurant(showRestaurantMatcher);
        else if (showMenuMatcher.matches())
            msg = showMenu(showMenuMatcher);
        else if (addToCartMatcher.matches())
            msg = addToCart(addToCartMatcher);
        else if (removeFromCartMatcher.matches())
            msg = removeFromCart(removeFromCartMatcher);
        else if (showCartMatcher.matches())
            msg = showCart();
        else if (showDiscountsMatcher.matches())
            msg = showDiscounts();
        else if (purchaseCartMatcher.matches())
            msg = purchaseCart(purchaseCartMatcher);
        else
            msg = Utils.invalidCommand;
        if (!msg.isEmpty())
            System.out.println(msg);
        return this;
    }

    @Override
    public String toString() {
        return "customer menu";
    }


    private String chargeAccount(Matcher matcher) {
        int amount = Integer.parseInt(matcher.group("amount"));
        if (amount <= 0) return "charge account failed: invalid cost or price";
        currentCustomer.updateBalance(amount);
        return "charge account successful";
    }

    private String showBalance() {
        return "" + currentCustomer.getBalance();
    }

    private String showRestaurant(Matcher matcher) {
        //[index]) [name]: type=[type] balance=[balance]
        String type = matcher.group("type");
        int idx = 1;
        for (Restaurant restaurant : SnappFood.singleton.getRestaurants()) {
            if (type != null && !restaurant.getType().equals(type)) continue;
            System.out.printf("%d) %s: type=%s\n",
                    idx++,
                    restaurant.getUsername(),
                    restaurant.getType());
        }
        return "";
    }

    private String showMenu(Matcher matcher) {
        String restaurantName = matcher.group("restaurantName");
        String category = matcher.group("category");
        Restaurant restaurant = SnappFood.singleton.getRestaurantByUsername(restaurantName);

        if (restaurant == null) return "show menu failed: restaurant not found";
        if (category != null && !Utils.matchesWith(category, "starter|entree|dessert"))
            return "show menu failed: invalid category";

        for (String cat : new String[]{"starter", "entree", "dessert"}) {
            if (category != null && !category.equals(cat)) continue;

            if (category == null)
                System.out.printf("<< %s >>\n", cat.toUpperCase());
            for (Food food : restaurant.getFoods())
                if (food.getCategory().equals(cat))
                    System.out.printf("%s | price=%d\n", food.getName(), food.getPrice());
        }
        return "";
    }

    private String addToCart(Matcher matcher) {
        String restaurantName = matcher.group("restaurantName");
        String foodName = matcher.group("foodName");
        String numStr = matcher.group("number");
        int number = numStr == null ? 1 : Integer.parseInt(numStr);

        Restaurant restaurant = SnappFood.singleton.getRestaurantByUsername(restaurantName);
        if (restaurant == null) return "add to cart failed: restaurant not found";
        Food food = restaurant.getFoodByName(foodName);
        if (food == null) return "add to cart failed: food not found";
        if (number <= 0) return "add to cart failed: invalid number";
        CartItem item = currentCustomer.getCartItem(restaurant, food);
        if (item == null)
            currentCustomer.addToCart(new CartItem(restaurant, food, number));
        else
            item.updateNumber(number);
        return "add to cart successful";
    }

    private String removeFromCart(Matcher matcher) {
        String restaurantName = matcher.group("restaurantName");
        String foodName = matcher.group("foodName");
        String numStr = matcher.group("number");
        int number = numStr == null ? 1 : Integer.parseInt(numStr);

        Restaurant restaurant = SnappFood.singleton.getRestaurantByUsername(restaurantName);
        if (restaurant == null) return "remove from cart failed: not in cart";
        Food food = restaurant.getFoodByName(foodName);
        if (food == null) return "remove from cart failed: not in cart";
        CartItem item = currentCustomer.getCartItem(restaurant, food);
        if (item == null) return "remove from cart failed: not in cart";

        if (number <= 0) return "remove from cart failed: invalid number";

        if (number > item.getNumber()) return "remove from cart failed: not enough food in cart";

        item.updateNumber(-number);
        if (item.getNumber() == 0) currentCustomer.removeFromCart(item);
        return "remove from cart successful";
    }

    private String showCart() {
        //[index]) [food name] | restaurant=[restaurant name] price=[price]
        int idx = 1, price = 0;
        for (CartItem item : currentCustomer.getCart()) {
            System.out.printf("%d) %s | restaurant=%s price=%d\n", idx++,
                    item.getFood().getName(), item.getRestaurant().getUsername(), item.getFood().getPrice() * item.getNumber());
            price += item.getFood().getPrice() * item.getNumber();
        }
        System.out.printf("Total: %d\n", price);
        return "";
    }

    private String showDiscounts() {
        //[index]) [code] | amount=[amount]
        int idx = 1;
        for (Discount discount : currentCustomer.getDiscounts()) {
            System.out.printf("%d) %s | amount=%d\n", idx++,
                    discount.getCode(), discount.getAmount());
        }
        return "";
    }

    private String purchaseCart(Matcher matcher) {
        String discountCode = matcher.group("discountCode");
        Discount discount = currentCustomer.getDiscountByCode(discountCode);
        if (discountCode != null && discount == null) return "purchase failed: invalid discount code";
        int off = discount == null ? 0 : discount.getAmount();
        int price = 0;
        for (CartItem item : currentCustomer.getCart())
            price += item.getFood().getPrice() * item.getNumber();
        if (price - off > currentCustomer.getBalance())
            return "purchase failed: inadequate money";

        currentCustomer.updateBalance(-price + off);
        for (CartItem item : currentCustomer.getCart()) {
            int profit = item.getFood().getPrice() - item.getFood().getCost();
            item.getRestaurant().updateBalance(profit * item.getNumber());
        }
        currentCustomer.clearCart();
        if (discount != null)
            SnappFood.singleton.removeDiscount(discount);
        return "purchase successful";
    }
}
