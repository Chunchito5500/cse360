package application;

import java.net.URL;                    // ← Add this
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        try {
            primaryStage = stage;

            // 1) Load the URL and print it
            URL fxmlUrl = getClass().getResource("/application/views/LoginPage.fxml");
            System.out.println("LoginPage.fxml URL → " + fxmlUrl);

            // 2) Now load the FXML only once into 'root'
            Parent root = FXMLLoader.load(fxmlUrl);

            // 3) Create a scene with a fixed size so we can see it
            Scene scene = new Scene(root, 600, 400);

            // 4) Apply your stylesheet (if present)
            URL cssUrl = getClass().getResource("/css/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            stage.setTitle("ASU Books");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}