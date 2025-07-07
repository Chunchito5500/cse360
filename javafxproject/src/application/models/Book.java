package application.models;

import java.time.LocalDate;

public class Book {
    private String  id;
    private String  sellerId;
    private String  title;
    private String  author;
    private String  category;
    private String  condition;
    private double  sellingPrice;
    private int     quantity;
    private String  description;
    private LocalDate dateListed;

    public Book(String id,
                String sellerId,
                String title,
                String author,
                String category,
                String condition,
                double sellingPrice,
                int quantity,
                String description,
                LocalDate dateListed) {

        this.id           = id;
        this.sellerId     = sellerId;
        this.title        = title;
        this.author       = author;
        this.category     = category;
        this.condition    = condition;
        this.sellingPrice = sellingPrice;
        this.quantity     = quantity;
        this.description  = description;
        this.dateListed   = dateListed;
    }

    public String  getId()           { return id; }
    public String  getSellerId()     { return sellerId; }
    public String  getTitle()        { return title; }
    public String  getAuthor()       { return author; }
    public String  getCategory()     { return category; }
    public String  getCondition()    { return condition; }
    public double  getSellingPrice() { return sellingPrice; }
    public int     getQuantity()     { return quantity; }
    public String  getDescription()  { return description; }
    public LocalDate getDateListed() { return dateListed; }

    public void setQuantity(int q)   { this.quantity = q; }

    @Override public String toString() { return title + " (" + condition + ")"; }
}