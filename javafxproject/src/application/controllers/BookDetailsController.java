package application.controllers;

import application.models.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class BookDetailsController {

    @FXML private Label titleLbl, authorLbl, categoryLbl,
                        conditionLbl, priceLbl, qtyLbl;
    @FXML private TextArea descArea;

    public void init(Book b) {
        titleLbl.setText(b.getTitle());
        authorLbl.setText("Author: " + b.getAuthor());
        categoryLbl.setText("Category: " + b.getCategory());
        conditionLbl.setText("Condition: " + b.getCondition());
        priceLbl.setText(String.format("Price: $%.2f", b.getSellingPrice()));
        qtyLbl.setText("Quantity: " + b.getQuantity());
        descArea.setText(b.getDescription());
    }
}