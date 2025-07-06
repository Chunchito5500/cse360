package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        URL fxml = getClass().getResource("/application/views/LoginPage.fxml");
        if (fxml == null) throw new IllegalStateException("LoginPage.fxml not found");
        Parent root = FXMLLoader.load(fxml);

        Scene scene = new Scene(root, 900, 600);

        URL css = getClass().getResource("/application/css/style.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setTitle("ASU Books");
        stage.setScene(scene);
        stage.show();

        primaryStage = stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}