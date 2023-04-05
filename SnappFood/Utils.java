package SnappFood;

import javax.rmi.CORBA.Util;
import java.util.regex.Pattern;

public class Utils {

    // Singleton
    public static final String invalidCommand = "invalid command!";

    public static boolean matchesWith(String str, String pattern) {
        return Pattern.compile(pattern).matcher(str).matches();
    }

    public static boolean exsistsIn(String str, String pattern) {
        return Pattern.compile(pattern).matcher(str).find();
    }

    public static String validateUsername(String username) {
        if (!matchesWith(username, "[a-zA-Z0-9_]*")) return "invalid username format";
        if (!exsistsIn(username, "[a-zA-Z]")) return "invalid username format";
        return null;
    }

    public static String validatePassword(String password) {
        if (!Utils.matchesWith(password, "[a-zA-Z0-9_]*")) return "invalid password format";
        if (password.length() < 5 ||
                !Utils.exsistsIn(password, "[a-z]") ||
                !Utils.exsistsIn(password, "[A-Z]") ||
                !Utils.exsistsIn(password, "\\d"))
            return "weak password";
        return null;
    }
// Singleton End
}
