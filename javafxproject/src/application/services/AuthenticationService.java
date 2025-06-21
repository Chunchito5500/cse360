package application.services;

import application.models.User;
import application.utils.CSVUtils;
import application.utils.Constants;
import application.utils.Constants;
import application.utils.CSVUtils;
import application.utils.PasswordUtils;

import java.util.List;
import java.util.UUID;

public class AuthenticationService {
  public static User login(String username, String password) {
    String hash = PasswordUtils.hash(password);
    List<User> users = CSVUtils.readUsers(Constants.USERS_CSV);
    for (User u : users) {
      if (u.getUsername().equals(username)
       && u.getPasswordHash().equals(hash)) {
        return u;
      }
    }
    return null;
  }

  public static boolean signUp(String fullName,
                               String username,
                               String password,
                               String role) {
    List<User> users = CSVUtils.readUsers(Constants.USERS_CSV);
    for (User u : users) {
      if (u.getUsername().equals(username)) return false;
    }
    String id = UUID.randomUUID().toString();
    String hash = PasswordUtils.hash(password);
    User newU = new User(id, fullName, username, hash, role);
    return CSVUtils.appendUser(newU, Constants.USERS_CSV);
  }
}