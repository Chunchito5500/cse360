package application.services;

import application.models.User;
import application.utils.CSVUtils;
import application.utils.Constants;
import application.utils.PasswordUtils;

import java.util.List;
import java.util.UUID;

public class AuthenticationService {

    public static User login(String username, String password) {
        String hash = PasswordUtils.hash(password);
        List<User> users = CSVUtils.readUsers(Constants.USERS_CSV);
        return users.stream()
                .filter(u -> u.getUsername().equals(username)
                          && u.getPasswordHash().equals(hash))
                .findFirst().orElse(null);
    }

    public static boolean signUp(String fullName,
                                 String username,
                                 String password,
                                 String role) {

        List<User> users = CSVUtils.readUsers(Constants.USERS_CSV);
        if (users.stream().anyMatch(u -> u.getUsername().equals(username)))
            return false;

        User u = new User(UUID.randomUUID().toString(),
                          fullName, username,
                          PasswordUtils.hash(password), role);
        return CSVUtils.writeUsers(
                new java.util.ArrayList<>(users){{ add(u); }},
                Constants.USERS_CSV);
    }
}