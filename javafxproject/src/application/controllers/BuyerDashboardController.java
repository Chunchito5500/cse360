package application.controllers;

import application.Main;
import application.models.Book;
import application.services.BookService;
import application.services.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuyerDashboardController {
    @FXML private Label userLabel;
    @FXML private CheckBox newCheck, usedCheck, oldCheck;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private TilePane booksPane;
    @FXML private ListView<Book> cartList;

    private List<Book> allBooks = new ArrayList<>();

    @FXML
    public void initialize() {
        // Show who’s logged in
        userLabel.setText(Session.getCurrentUser().getUsername());

        // Populate categories
        categoryCombo.getItems().addAll(
            "All",
            "Computer Science Books",
            "Math Books",
            "English Language Books",
            "Others"
        );
        categoryCombo.getSelectionModel().selectFirst();

        // Load & display all books
        allBooks = BookService.getAllBooks();
        showBooks(allBooks);
    }

    private void showBooks(List<Book> books) {
        booksPane.getChildren().clear();
        for (Book b : books) {
            booksPane.getChildren().add(createBookCard(b));
        }
    }

    private VBox createBookCard(Book b) {
        Label title = new Label(b.getTitle());
        Button addBtn = new Button("Add to Cart");
        addBtn.setOnAction(e -> cartList.getItems().add(b));

        VBox box = new VBox(5, title, addBtn);
        box.setPadding(new Insets(5));
        box.setStyle("-fx-border-color: gray; -fx-border-width: 1;");
        return box;
    }

    @FXML
    private void applyFilters(ActionEvent event) {
        List<Book> filtered = allBooks.stream()
            .filter(book -> {
                // condition filter
                boolean condOK =
                    (newCheck.isSelected()  && "New".equals(book.getCondition()))  ||
                    (usedCheck.isSelected() && "Used".equals(book.getCondition())) ||
                    (oldCheck.isSelected()  && "Old".equals(book.getCondition()))  ||
                    (!newCheck.isSelected() && !usedCheck.isSelected() && !oldCheck.isSelected());
                // category filter
                boolean catOK =
                    "All".equals(categoryCombo.getValue())
                 || categoryCombo.getValue().equals(book.getCategory());
                return condOK && catOK;
            })
            .collect(Collectors.toList());

        showBooks(filtered);
    }

    @FXML
    private void handlePurchase(ActionEvent event) {
        List<Book> cart = new ArrayList<>(cartList.getItems());
        List<String> outOfStock = BookService.purchaseBooks(cart);

        if (!outOfStock.isEmpty()) {
            showAlert(Alert.AlertType.ERROR,
                "Out of stock: " + String.join(", ", outOfStock));
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Purchase successful!");
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
            Stage st = Main.primaryStage;
            st.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }
}