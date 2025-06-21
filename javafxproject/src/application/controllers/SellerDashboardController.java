package application.controllers;

import application.Main;
import application.models.Book;
import application.services.BookService;
import application.services.Session;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class SellerDashboardController {
    @FXML private Label userLabel;
    @FXML private Button listNewBtn;
    @FXML private VBox   formPane;
    @FXML private TextField titleField, authorField, yearField, priceField;
    @FXML private ComboBox<String> categoryCombo, conditionCombo;
    @FXML private Label suggestedPriceLabel;
    @FXML private TableView<Book> inventoryTable;
    @FXML private TableColumn<Book,String> titleCol, categoryCol, conditionCol, dateCol;
    @FXML private TableColumn<Book,Integer> quantityCol;
    @FXML private TableColumn<Book,Double> priceCol;

    private ObservableList<Book> inventory = FXCollections.observableArrayList();
    private Map<String, Double> multiplier = Map.of(
        "New", 1.0,
        "Used", 0.8,
        "Old", 0.5
    );

    @FXML
    public void initialize() {
        // Show logged-in username
        var user = Session.getCurrentUser();
        userLabel.setText(user.getUsername());

        // Hide form initially
        formPane.setVisible(false);
        formPane.setManaged(false);

        // Populate dropdowns
        categoryCombo.getItems().addAll(
          "Computer Science Books","Math Books","English Language Books","Others"
        );
        categoryCombo.getSelectionModel().selectFirst();
        conditionCombo.getItems().addAll("New","Used","Old");
        conditionCombo.getSelectionModel().selectFirst();

        // Set up table columns
        titleCol   .setCellValueFactory(new PropertyValueFactory<>("title"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        conditionCol.setCellValueFactory(new PropertyValueFactory<>("condition"));
        priceCol   .setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        dateCol    .setCellValueFactory(new PropertyValueFactory<>("dateListed"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        inventoryTable.setItems(inventory);

        // Load existing seller books
        refreshInventory();
    }

    private void refreshInventory() {
        inventory.setAll(
          BookService.getBooksBySeller(Session.getCurrentUser().getId())
        );
    }

    @FXML
    private void toggleForm(ActionEvent e) {
        boolean showing = formPane.isVisible();
        formPane.setVisible(!showing);
        formPane.setManaged(!showing);
        listNewBtn.setText(showing ? "List New Book" : "Hide Form");
    }

    @FXML
    private void calculatePrice(ActionEvent e) {
        try {
            double orig = Double.parseDouble(priceField.getText());
            double m   = multiplier.get(conditionCombo.getValue());
            suggestedPriceLabel.setText(String.format("$%.2f", orig * m));
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, 
              "Enter a valid number for original price", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void handleListBook(ActionEvent e) {
        try {
            Book b = new Book(
                UUID.randomUUID().toString(),
                Session.getCurrentUser().getId(),
                titleField.getText().trim(),
                authorField.getText().trim(),
                categoryCombo.getValue(),
                conditionCombo.getValue(),
                Double.parseDouble(suggestedPriceLabel.getText().substring(1)),
                1,
                LocalDate.now()
            );
            boolean ok = BookService.addBook(b);
            if (ok) {
                new Alert(Alert.AlertType.INFORMATION,
                          "Book listed successfully!", ButtonType.OK)
                    .showAndWait();
                // clear form
                titleField.clear(); authorField.clear();
                yearField.clear(); priceField.clear();
                suggestedPriceLabel.setText("$0.00");
                // refresh table
                refreshInventory();
            } else {
                new Alert(Alert.AlertType.ERROR,
                          "Failed to list book.", ButtonType.OK).showAndWait();
            }
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR,
              "Please fill all fields correctly.", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void handleLogout(ActionEvent e) {
        Session.clear();
        try {
            Parent root = FXMLLoader.load(
              getClass().getResource("/application/views/LoginPage.fxml")
            );
            Stage st = Main.primaryStage;
            st.setScene(new Scene(root, 600, 400));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}