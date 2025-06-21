module javafxproject {
    requires javafx.controls;
    requires javafx.fxml;

    // For Launcher and FXMLLoader:
    opens application to javafx.graphics, javafx.fxml;
    // For controllers:
    opens application.controllers to javafx.fxml;
    // *** Add this line to expose your Book (and other model) getters to JavaFX: ***
    opens application.models to javafx.base, javafx.fxml;
}