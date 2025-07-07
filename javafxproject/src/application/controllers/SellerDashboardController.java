package application.controllers;

import application.Main;
import application.models.AdminLog;
import application.models.Book;
import application.services.BookService;
import application.services.Session;
import application.utils.CSVUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class SellerDashboardController {

    @FXML private Label   userLabel;
    @FXML private Button  listNewBtn;
    @FXML private VBox    formPane;

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField yearField;
    @FXML private TextField priceField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private ComboBox<String> conditionCombo;
    @FXML private Label   suggestedPriceLabel;

    @FXML private Spinner<Integer> qtySpinner;
    @FXML private TextArea         descArea;

    @FXML private TableView<Book>            inventoryTable;
    @FXML private TableColumn<Book,String>   titleCol;
    @FXML private TableColumn<Book,String>   categoryCol;
    @FXML private TableColumn<Book,String>   conditionCol;
    @FXML private TableColumn<Book,Double>   priceCol;
    @FXML private TableColumn<Book,Integer>  quantityCol;
    @FXML private TableColumn<Book,LocalDate> dateCol;

    private final ObservableList<Book> inventory = FXCollections.observableArrayList();
    private final Map<String,Double> multiplier = Map.of(
        "New", 1.0,
        "Used", 0.8,
        "Old", 0.5
    );

    @FXML
    public void initialize() {

        Platform.runLater(() -> {
            URL css = getClass().getResource("/application/css/seller.css");
            if (css != null) {
                var sc = userLabel.getScene();
                if (!sc.getStylesheets().contains(css.toExternalForm()))
                    sc.getStylesheets().add(css.toExternalForm());
            }
        });

        userLabel.setText(Session.getCurrentUser().getUsername());
        formPane.setVisible(false);
        formPane.setManaged(false);

        categoryCombo.getItems().setAll(
            "Computer Science Books",
            "Math Books",
            "English Language Books",
            "Others"
        );
        categoryCombo.getSelectionModel().selectFirst();

        conditionCombo.getItems().setAll("New","Used","Old");
        conditionCombo.getSelectionModel().selectFirst();


        qtySpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1)
        );

        titleCol   .setCellValueFactory(new PropertyValueFactory<>("title"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        conditionCol.setCellValueFactory(new PropertyValueFactory<>("condition"));
        priceCol   .setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateCol    .setCellValueFactory(new PropertyValueFactory<>("dateListed"));

        inventoryTable.setItems(inventory);
        refreshInventory();
    }

    private void refreshInventory() {
        var books = BookService.getBooksBySeller(Session.getCurrentUser().getId());
        inventory.setAll(books);
    }

    @FXML private void toggleForm(ActionEvent e) {
        boolean show = !formPane.isVisible();
        formPane.setVisible(show);
        formPane.setManaged(show);
        listNewBtn.setText(show ? "Hide Form" : "List New Book");
    }

    @FXML private void calculatePrice(ActionEvent e) {
        try {
            double orig = Double.parseDouble(priceField.getText());
            double m = multiplier.get(conditionCombo.getValue());
            suggestedPriceLabel.setText(String.format("$%.2f", orig * m));
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR,
                      "Enter a valid number for original price",
                      ButtonType.OK).showAndWait();
        }
    }

    @FXML private void handleListBook(ActionEvent e) {
        try {
            int qty = qtySpinner.getValue();
            double sellPrice = Double.parseDouble(
                suggestedPriceLabel.getText().replace("$","")
            );

            Book b = new Book(
                UUID.randomUUID().toString(),
                Session.getCurrentUser().getId(),
                titleField.getText().trim(),
                authorField.getText().trim(),
                categoryCombo.getValue(),
                conditionCombo.getValue(),
                sellPrice,
                qty,
                descArea.getText().trim(),
                LocalDate.now()
            );

            if (BookService.addBook(b)) {
                CSVUtils.appendLog(
                    new AdminLog(
                        UUID.randomUUID().toString(),
                        Session.getCurrentUser().getUsername(),
                        b.getTitle(),
                        true, false,
                        b.getSellingPrice(),
                        LocalDateTime.now()
                    ),
                    "data/logs.csv"
                );
                new Alert(Alert.AlertType.INFORMATION,
                          "Book listed successfully!",
                          ButtonType.OK).showAndWait();

                titleField.clear();
                authorField.clear();
                yearField.clear();
                priceField.clear();
                qtySpinner.getValueFactory().setValue(1);
                descArea.clear();
                suggestedPriceLabel.setText("$0.00");
                refreshInventory();
            } else {
                new Alert(Alert.AlertType.ERROR,
                          "Failed to list book.",
                          ButtonType.OK).showAndWait();
            }
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR,
                      "Please fill all fields correctly.",
                      ButtonType.OK).showAndWait();
        }
    }

    @FXML private void handleLogout(ActionEvent e) {
        Session.clear();
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/application/views/LoginPage.fxml")
            );
            Scene scene = new Scene(root, 900, 600);
            URL css = getClass().getResource("/application/css/style.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());
            Main.primaryStage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}