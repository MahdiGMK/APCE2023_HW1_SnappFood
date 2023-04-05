package SnappFood.Menu;

import SnappFood.*;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIterNodeList;

import javax.rmi.CORBA.Util;
import java.awt.*;
import java.text.BreakIterator;
import java.util.regex.Matcher;

public class MainMenu implements IMenu {
    private IMenu nextMenu;
    private User currentUser;

    public MainMenu(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public IMenu run(String line) {
        nextMenu = this;
        Matcher enterMatcher = Commands.Enter.getMatcher(line);

        String msg = "";
        if (enterMatcher.matches())
            msg = enter(enterMatcher);
        else
            msg = Utils.invalidCommand;
        if (!msg.isEmpty())
            System.out.println(msg);
        return nextMenu;
    }

    @Override
    public String toString() {
        return "main menu";
    }

    public String enter(Matcher matcher) {
        String menuName = matcher.group("menuName");
        Matcher matcher1 = Commands.MenuNameFormat.getMatcher(menuName);
        if (!matcher1.matches())
            return "enter menu failed: invalid menu name";

        int idx = -1;
        if (matcher1.group("c") != null) idx = 0;
        else if (matcher1.group("r") != null) idx = 1;
        else if (matcher1.group("s") != null) idx = 2;

        if (idx == 0) {
            menuName = "customer menu";
            if (currentUser.getClass() != Customer.class) return "enter menu failed: access denied";
            nextMenu = new CustomerMenu((Customer) currentUser);
        } else if (idx == 1) {
            menuName = "restaurant admin menu";
            if (currentUser.getClass() != Restaurant.class) return "enter menu failed: access denied";
            nextMenu = new RestaurantAdminMenu((Restaurant) currentUser);
        } else {
            menuName = "Snappfood admin menu";
            if (currentUser.getClass() != SnappFood.class) return "enter menu failed: access denied";
            nextMenu = new SnappFoodAdminMenu();
        }

        return "enter menu successful: You are in the " + menuName + "!";
    }
}
