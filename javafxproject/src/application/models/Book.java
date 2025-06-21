package application.models;

import java.time.LocalDate;

public class Book {
    private String id;
    private String sellerId;
    private String title;
    private String author;
    private String category;
    private String condition;
    private double sellingPrice;
    private int quantity;
    private LocalDate dateListed;

    /**
     * Constructs a Book object with all required fields.
     * @param id            unique book identifier
     * @param sellerId      ID of the seller
     * @param title         book title
     * @param author        book author
     * @param category      category name
     * @param condition     condition (New, Used, Old)
     * @param sellingPrice  price to sell
     * @param quantity      number of copies available
     * @param dateListed    date when listed
     */
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