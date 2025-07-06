package application.controllers;

import application.models.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.function.Consumer;

public class BookCardController {

    @FXML private VBox   cardRoot;
    @FXML private Label  titleLabel;
    @FXML private Button addButton;

    private Book book;
    private Consumer<Book> onAdd;

    public void init(Book book, Consumer<Book> onAdd) {
        this.book  = book;
        this.onAdd = onAdd;

        titleLabel.setText(book.getTitle());
        cardRoot.getStyleClass().add(book.getCondition().toLowerCase());


        addButton.setOnAction(evt -> handleAdd());
    }

    private void handleAdd() {
        if (onAdd != null) onAdd.accept(book);
    }
    
    public Button getAddButton() {
        return addButton;
    }
}