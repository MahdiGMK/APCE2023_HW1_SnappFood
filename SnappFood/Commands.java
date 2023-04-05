package SnappFood;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    WhiteLine(""),

    ShowCurrentMenu("show current menu"),

    Register("register (?<username>\\S+) (?<password>\\S+)"),
    Login("login (?<username>\\S+) (?<password>\\S+)"),
    ChangePassword("change password (?<username>\\S+) (?<oldPassword>\\S+) (?<newPassword>\\S+)"),
    RemoveAccount("remove account (?<username>\\S+) (?<password>\\S+)"),
    Exit("exit"),

    Enter("enter (?<menuName>.+)"),
    MenuNameFormat("((?<c>customer menu)|(?<r>restaurant admin menu)|(?<s>Snappfood admin menu))"),
    Logout("logout"),
    AddRestaurant("add restaurant (?<name>\\S+) (?<password>\\S+) (?<type>\\S+)"),
    ShowRestaurant("show restaurant( -t (?<type>\\S+))?"),
    RemoveRestaurant("remove restaurant (?<name>\\S+)"),
    SetDiscount("set discount (?<username>\\S+) (?<amount>{z}) (?<code>\\S+)"),
    ShowDiscounts("show discounts"),

    ChargeAccount("charge account (?<amount>{z})"),
    ShowBalance("show balance"),
    AddFood("add food (?<name>\\S+) (?<category>\\S+) (?<price>{z}) (?<cost>{z})"),
    RemoveFood("remove food (?<name>\\S+)"),

    ShowMenu("show menu (?<restaurantName>\\S+)( -c (?<category>\\S+))?"),
    AddToCart("add to cart (?<restaurantName>\\S+) (?<foodName>\\S+)( -n (?<number>{z}))?"),
    RemoveFromCart("remove from cart (?<restaurantName>\\S+) (?<foodName>\\S+)( -n (?<number>{z}))?"),
    ShowCart("show cart"),
    PurchaseCart("purchase cart( -d (?<discountCode>\\S+))?");


    final Pattern pattern;

    private Commands(String regex) {
        regex = "{}" + regex + "{}";
        regex = postProcess(regex);
        pattern = Pattern.compile(regex);
    }

    String postProcess(String str) {
        str = str.replace(" ", "\\s+");
        str = str.replace("{}", "\\s*");
        str = str.replace("{n}", "\\d+");
        str = str.replace("{z}", "-?\\d+");
        return str;
    }

    public Matcher getMatcher(String line) {
        return pattern.matcher(line);
    }

    public boolean matches(String line) {
        return getMatcher(line).matches();
    }
}
