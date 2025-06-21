package application.controllers;

import application.Main;
import application.models.User;
import application.services.AuthenticationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import application.services.Session;

import java.io.IOException;

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
        // route by role
        String fxml;
        switch (user.getRole()) {
            case "Buyer":  fxml = "/application/views/BuyerDashboard.fxml"; break;
            case "Seller": fxml = "/application/views/SellerDashboard.fxml"; break;
            case "Admin":  fxml = "/application/views/AdminDashboard.fxml"; break;
            default:       fxml = "/application/views/AccessDenied.fxml";
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage st = Main.primaryStage;
            st.setScene(new Scene(root));
            st.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Unable to load next page.");
        }
    }

    @FXML
    private void goToSignUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/application/views/SignUpPage.fxml"));
            Stage st = Main.primaryStage;
            st.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Unable to load Sign-Up page.");
        }
    }
}