package application.controllers;

import application.Main;
import application.models.Book;
import application.services.BookService;
import application.services.Session;
import application.utils.CSVUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BuyerDashboardController {

    @FXML private Label            userLabel;
    @FXML private CheckBox         newCheck, usedCheck, oldCheck;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private TilePane         booksPane;
    @FXML private ListView<Book>   cartList;
    @FXML private Button           removeButton;

    private List<Book> allBooks;

    @FXML
    public void initialize() {
        // populate user
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


        removeButton.setDisable(true);
        cartList.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) ->
            removeButton.setDisable(sel == null)
        );
    }

    private void showBooks(List<Book> books) {
        booksPane.getChildren().clear();
        for (Book b : books) {
            if (b.getQuantity() <= 0) continue;
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
    private void applyFilters(ActionEvent e) {
        List<Book> filtered = allBooks.stream()
            .filter(this::filterByCondition)
            .filter(this::filterByCategory)
            .toList();
        showBooks(filtered);
    }

    private boolean filterByCondition(Book b) {
        if (newCheck.isSelected()  && "New".equals(b.getCondition()))  return true;
        if (usedCheck.isSelected() && "Used".equals(b.getCondition())) return true;
        if (oldCheck.isSelected()  && "Old".equals(b.getCondition()))  return true;

        return !newCheck.isSelected() && !usedCheck.isSelected() && !oldCheck.isSelected();
    }

    private boolean filterByCategory(Book b) {
        return "All".equals(categoryCombo.getValue())
            || categoryCombo.getValue().equals(b.getCategory());
    }

    @FXML
    private void handleRemove(ActionEvent e) {
        Book sel = cartList.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        cartList.getItems().remove(sel);

        allBooks.stream()
                .filter(b -> b.getId().equals(sel.getId()))
                .findFirst()
                .ifPresent(b -> b.setQuantity(b.getQuantity() + 1));

        applyFilters(null);

        if (!cartList.getItems().isEmpty()) {
            cartList.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void handlePurchase(ActionEvent e) {
        List<Book> cart = List.copyOf(cartList.getItems());
        var oos = BookService.purchaseBooks(cart);
        if (!oos.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,
                      "Out of stock: " + String.join(", ", oos),
                      ButtonType.OK)
            .showAndWait();
            return;
        }

        for (Book b : cart) {
            CSVUtils.appendLog(
              new application.models.AdminLog(
                  UUID.randomUUID().toString(),
                  Session.getCurrentUser().getUsername(),
                  b.getTitle(),
                  false, true,
                  b.getSellingPrice(),
                  LocalDateTime.now()),
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

    @FXML
    private void handleLogout(ActionEvent e) {
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