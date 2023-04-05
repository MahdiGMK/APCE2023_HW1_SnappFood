package SnappFood.Menu;

import SnappFood.*;

import javax.rmi.CORBA.Util;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

public class LoginMenu implements IMenu {
    private IMenu nextMenu;

    @Override
    public IMenu run(String line) {
        nextMenu = this;
        Matcher registerMatcher = Commands.Register.getMatcher(line);
        Matcher loginMatcher = Commands.Login.getMatcher(line);
        Matcher changePasswordMatcher = Commands.ChangePassword.getMatcher(line);
        Matcher removeAccountMatcher = Commands.RemoveAccount.getMatcher(line);
        Matcher exitMatcher = Commands.Exit.getMatcher(line);

        String msg = "";
        if (registerMatcher.matches()) msg = register(registerMatcher);
        else if (loginMatcher.matches()) msg = login(loginMatcher);
        else if (changePasswordMatcher.matches()) msg = changePassword(changePasswordMatcher);
        else if (removeAccountMatcher.matches()) msg = removeAccount(removeAccountMatcher);
        else if (exitMatcher.matches()) nextMenu = null;
        else msg = Utils.invalidCommand;

        if (!msg.isEmpty())
            System.out.println(msg);
        return nextMenu;
    }

    @Override
    public String toString() {
        return "login menu";
    }


    private String register(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String usernameStatus = Utils.validateUsername(username);
        String passwordStatus = Utils.validatePassword(password);
        if (usernameStatus != null) return "register failed: " + usernameStatus;
        if (SnappFood.singleton.getUserByUsername(username) != null) return "register failed: username already exists";
        if (passwordStatus != null) return "register failed: " + passwordStatus;

        Customer customer = new Customer(username, password);
        SnappFood.singleton.addUser(customer);
        return "register successful";
    }

    private String login(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        User user = SnappFood.singleton.getUserByUsername(username);
        if (user == null) return "login failed: username not found";
        if (!user.getPassword().equals(password)) return "login failed: incorrect password";

        nextMenu = new MainMenu(user);
        return "login successful";
    }

    private String changePassword(Matcher matcher) {
        String username = matcher.group("username");
        String oldPassword = matcher.group("oldPassword");
        String newPassword = matcher.group("newPassword");
        User user = SnappFood.singleton.getUserByUsername(username);
        if (user == null) return "password change failed: username not found";
        if (!user.getPassword().equals(oldPassword)) return "password change failed: incorrect password";
        String passwordStatus = Utils.validatePassword(newPassword);
        if (passwordStatus != null) {
            if (passwordStatus.equals("invalid password format"))
                return "password change failed: invalid new password";
            else
                return "password change failed: weak new password";
        }

        user.setPassword(newPassword);
        return "password change successful";
    }

    private String removeAccount(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        User user = SnappFood.singleton.getUserByUsername(username);
        if (user == null) return "remove account failed: username not found";
        if (!user.getPassword().equals(password)) return "remove account failed: incorrect password";
        SnappFood.singleton.removeUser(user);
        return "remove account successful";
    }
}
