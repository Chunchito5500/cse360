package application.controllers;

import application.Main;
import application.services.AuthenticationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    @FXML private TextField    fullNameField, usernameField;
    @FXML private PasswordField passwordField, confirmField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private Label         errorLabel;

    @FXML
    public void initialize() {
        roleCombo.getItems().addAll("Buyer", "Seller");
        roleCombo.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        String name = fullNameField.getText().trim();
        String u    = usernameField.getText().trim();
        String p    = passwordField.getText();
        String c    = confirmField.getText();
        String role = roleCombo.getValue();

        if (name.isEmpty()||u.isEmpty()||p.isEmpty()||c.isEmpty()) {
            errorLabel.setText("All fields are required.");
            return;
        }
        if (!p.equals(c)) {
            errorLabel.setText("Passwords don’t match.");
            return;
        }

        boolean ok = AuthenticationService.signUp(name, u, p, role);
        if (!ok) {
            errorLabel.setText("Username already taken.");
            return;
        }

        // back to login
        goToLogin(event);
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
              getClass().getResource("/application/views/LoginPage.fxml"));
            Stage st = Main.primaryStage;
            st.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Cannot return to login.");
        }
    }
}