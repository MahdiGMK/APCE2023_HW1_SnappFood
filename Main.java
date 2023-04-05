import SnappFood.Commands;
import SnappFood.Menu.IMenu;
import SnappFood.Menu.LoginMenu;
import SnappFood.Restaurant;
import SnappFood.SnappFood;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    // Singleton
    public static void main(String[] args) {
        IMenu menu = new LoginMenu();
        Scanner scanner = new Scanner(System.in);
        String sfUsername, sfPassword;
        sfUsername = scanner.nextLine();
        sfPassword = scanner.nextLine();
        SnappFood.singleton = new SnappFood(sfUsername, sfPassword);
        SnappFood.singleton.addUser(SnappFood.singleton);

        while (menu != null && scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (Commands.WhiteLine.matches(line)) continue;

            if (Commands.ShowCurrentMenu.matches(line)) System.out.println(menu);
            else if (menu.getClass() != LoginMenu.class && Commands.Logout.matches(line)) menu = new LoginMenu();
            else menu = menu.run(line);
        }
    }
// Singleton End
}
