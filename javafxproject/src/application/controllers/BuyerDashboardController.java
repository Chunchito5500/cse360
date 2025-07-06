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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

public class BuyerDashboardController {
    @FXML private Label userLabel;
    @FXML private CheckBox newCheck, usedCheck, oldCheck;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private TilePane booksPane;
    @FXML private ListView<Book> cartList;

    private List<Book> allBooks;

    @FXML
    public void initialize() {
        userLabel.setText(Session.getCurrentUser().getUsername());

        categoryCombo.getItems().setAll(
            "All",
            "Computer Science Books",
            "Math Books",
            "English Language Books",
            "Others"
        );
        categoryCombo.getSelectionModel().selectFirst();

        allBooks = BookService.getAllBooks();
        showBooks(allBooks);
    }

    private void showBooks(List<Book> books) {
        booksPane.getChildren().clear();
        for (Book b : books) {
            try {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/application/views/BookCard.fxml")
                );
                VBox card = loader.load();
                BookCardController bc = loader.getController();

                bc.init(b, cartList.getItems()::add);
                booksPane.getChildren().add(card);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void applyFilters(ActionEvent event) {
        List<Book> filtered = allBooks.stream()
            .filter(book -> {
                boolean condOK =
                    (newCheck.isSelected()  && "New".equals(book.getCondition()))  ||
                    (usedCheck.isSelected() && "Used".equals(book.getCondition())) ||
                    (oldCheck.isSelected()  && "Old".equals(book.getCondition()))  ||
                    (!newCheck.isSelected() && !usedCheck.isSelected() && !oldCheck.isSelected());

                boolean catOK =
                    "All".equals(categoryCombo.getValue()) ||
                    categoryCombo.getValue().equals(book.getCategory());
                return condOK && catOK;
            })
            .toList();

        showBooks(filtered);
    }

    @FXML
    private void handlePurchase(ActionEvent event) {
        List<Book> cart = List.copyOf(cartList.getItems());
        List<String> outOfStock = BookService.purchaseBooks(cart);
        if (!outOfStock.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,
                      "Out of stock: " + String.join(", ", outOfStock),
                      ButtonType.OK).showAndWait();
        } else {
            for (Book b : cart) {
                CSVUtils.appendLog(
                    new AdminLog(
                        java.util.UUID.randomUUID().toString(),
                        Session.getCurrentUser().getUsername(),
                        b.getTitle(),
                        false,
                        true,
                        b.getSellingPrice(),
                        LocalDateTime.now()
                    ),
                    "data/logs.csv"
                );
            }
            new Alert(Alert.AlertType.INFORMATION,
                      "Purchase successful!",
                      ButtonType.OK).showAndWait();
            cartList.getItems().clear();
            allBooks = BookService.getAllBooks();
            applyFilters(null);
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Session.clear();
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
        }
    }
}