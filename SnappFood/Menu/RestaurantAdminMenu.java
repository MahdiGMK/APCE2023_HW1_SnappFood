package SnappFood.Menu;

import SnappFood.Commands;
import SnappFood.Food;
import SnappFood.Restaurant;
import SnappFood.Utils;

import java.util.regex.Matcher;

public class RestaurantAdminMenu implements IMenu {
    private Restaurant currentRestaurant;
    private IMenu nextMenu;

    public RestaurantAdminMenu(Restaurant currentRestaurant) {
        this.currentRestaurant = currentRestaurant;
    }

    @Override
    public IMenu run(String line) {
        nextMenu = this;
        String msg = "";

        Matcher chargeAccountMatcher = Commands.ChargeAccount.getMatcher(line);
        Matcher showBalanceMatcher = Commands.ShowBalance.getMatcher(line);
        Matcher addFoodMatcher = Commands.AddFood.getMatcher(line);
        Matcher removeFoodMatcher = Commands.RemoveFood.getMatcher(line);

        if (chargeAccountMatcher.matches())
            msg = chargeAccount(chargeAccountMatcher);
        else if (showBalanceMatcher.matches())
            msg = showBalance();
        else if (addFoodMatcher.matches())
            msg = addFood(addFoodMatcher);
        else if (removeFoodMatcher.matches())
            msg = removeFood(removeFoodMatcher);
        else
            msg = Utils.invalidCommand;

        if (!msg.isEmpty())
            System.out.println(msg);
        return nextMenu;
    }

    @Override
    public String toString() {
        return "restaurant admin menu";
    }

    private String chargeAccount(Matcher matcher) {
        int amount = Integer.parseInt(matcher.group("amount"));
        if (amount <= 0) return "charge account failed: invalid cost or price";
        currentRestaurant.updateBalance(amount);
        return "charge account successful";
    }

    private String showBalance() {
        return "" + currentRestaurant.getBalance();
    }

    private String addFood(Matcher matcher) {
        String name = matcher.group("name");
        String category = matcher.group("category");
        int price = Integer.parseInt(matcher.group("price"));
        int cost = Integer.parseInt(matcher.group("cost"));

        if (!Utils.matchesWith(category, "starter|entree|dessert"))
            return "add food failed: invalid category";
        if (!Utils.matchesWith(name, "[a-z-]*"))
            return "add food failed: invalid food name";
        if (currentRestaurant.getFoodByName(name) != null)
            return "add food failed: food already exists";
        if (cost <= 0 || price <= 0)
            return "add food failed: invalid cost or price";
        currentRestaurant.addFood(new Food(name, category, price, cost));
        return "add food successful";
    }

    private String removeFood(Matcher matcher) {
        String name = matcher.group("name");
        Food food = currentRestaurant.getFoodByName(name);
        if (food == null) return "remove food failed: food not found";
        currentRestaurant.removeFood(food);
        return "";
    }
}
