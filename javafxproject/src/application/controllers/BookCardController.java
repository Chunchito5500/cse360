package application.controllers;

import application.models.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class BookCardController {

    @FXML private VBox    cardRoot;
    @FXML private Label   titleLabel;
    @FXML private Label   qtyLabel;
    @FXML private Button  addButton;
    @FXML private Button  detailsBtn;

    private Book book;
    private Consumer<Book> onAdd;


    public void init(Book book, Consumer<Book> onAdd) {
        this.book  = book;
        this.onAdd = onAdd;


        titleLabel.setText(book.getTitle());
        cardRoot.getStyleClass().add(book.getCondition().toLowerCase());


        updateAddState();


        addButton.setOnAction(this::handleAdd);
        detailsBtn.setOnAction(this::showDetails);
    }

    private void updateAddState() {
        boolean inStock = book.getQuantity() > 0;
        addButton.setDisable(!inStock);
        qtyLabel.setText("Qty: " + book.getQuantity());
    }

    private void handleAdd(ActionEvent e) {
        if (onAdd != null && book.getQuantity() > 0) {
            onAdd.accept(book);
            book.setQuantity(book.getQuantity() - 1);
            updateAddState();
        }
    }

    @FXML
    private void showDetails(ActionEvent e) {
        try {
            FXMLLoader fx = new FXMLLoader(
                getClass().getResource("/application/views/BookDetails.fxml")
            );
            Parent p = fx.load();

            application.controllers.BookDetailsController ctl = fx.getController();
            ctl.init(book);

            Stage s = new Stage();
            s.setTitle(book.getTitle());
            s.setScene(new Scene(p));
            s.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Button getAddButton() {
        return addButton;
    }
}