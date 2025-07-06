package application.controllers;

import application.Main;
import application.services.AuthenticationService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;



public class SignUpController {
    @FXML private TextField        fullNameField, usernameField;
    @FXML private PasswordField    passwordField, confirmField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private Label            errorLabel;

    @FXML
    public void initialize() {
        roleCombo.getItems().addAll("Buyer", "Seller");
        roleCombo.getSelectionModel().selectFirst();

        Platform.runLater(() -> {
            Scene scene = fullNameField.getScene();
            if (scene != null) {
            	URL cssUrl = getClass().getResource("/application/css/signup.css");
                if (cssUrl != null) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                }
            }
        });
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        String name = fullNameField.getText().trim();
        String u    = usernameField.getText().trim();
        String p    = passwordField.getText();
        String c    = confirmField.getText();
        String role = roleCombo.getValue();

        if (name.isEmpty() || u.isEmpty() || p.isEmpty() || c.isEmpty()) {
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

        goToLogin(event);
    }

    @FXML
    private void goToLogin(ActionEvent event) {
    	try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/application/views/LoginPage.fxml")
            );
            Scene scene = new Scene(root, 900, 600);

            URL css = getClass().getResource("/application/css/style.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());

            Main.primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Cannot return to login.");
        }
    }
}