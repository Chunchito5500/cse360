package application.models;

import java.time.LocalDate;

public class Book {
    protected String id;
    protected String sellerId;
    protected String title;
    protected String author;
    protected String category;
    protected String condition;
    protected double sellingPrice;
    protected int quantity;
    protected LocalDate dateListed;


    public Book(String id,
                String sellerId,
                String title,
                String author,
                String category,
                String condition,
                double sellingPrice,
                int quantity,
                LocalDate dateListed) {
        this.id = id;
        this.sellerId = sellerId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.condition = condition;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.dateListed = dateListed;
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getCondition() {
        return condition;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getDateListed() {
        return dateListed;
    }

    @Override
    public String toString() {
        return title + " (" + condition + ")";
    }
}