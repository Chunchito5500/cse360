package application.controllers;

import application.Main;
import application.models.User;
import application.services.AuthenticationService;
import application.services.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML private TextField    usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label         errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String u = usernameField.getText().trim();
        String p = passwordField.getText().trim();
        if (u.isEmpty() || p.isEmpty()) {
            errorLabel.setText("Please enter both username and password.");
            return;
        }

        User user = AuthenticationService.login(u, p);
        if (user == null) {
            errorLabel.setText("Invalid credentials.");
            return;
        }

        Session.setCurrentUser(user);

        String fxml;
        String cssPath = "/application/css/style.css";

        switch (user.getRole()) {
            case "Buyer":
                fxml    = "/application/views/BuyerDashboard.fxml";
                cssPath = "/application/css/buyer.css";
                break;
            case "Seller":
                fxml    = "/application/views/SellerDashboard.fxml";
                cssPath = "/application/css/seller.css";
                break;
            case "Admin":
                fxml    = "/application/views/AdminDashboard.fxml";
                cssPath = "/application/css/admin.css";
                break;
            default:
                fxml = "/application/views/AccessDenied.fxml";
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root, 900, 600);

            URL cssUrl = getClass().getResource(cssPath);
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

            Main.primaryStage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
            errorLabel.setText("Unable to load next page.");
        }
    }

    @FXML
    private void goToSignUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/application/views/SignUpPage.fxml")
            );
            Scene scene = new Scene(root, 900, 600);
            URL css = getClass().getResource("/css/signup.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());
            Main.primaryStage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}