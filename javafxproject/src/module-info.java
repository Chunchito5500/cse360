module javafxproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens application.controllers to javafx.fxml, javafx.base;
    opens application.models       to javafx.fxml, javafx.base;

    exports application;
}