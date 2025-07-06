package application.models;

import java.util.ArrayList;
import java.util.List;


public class Buyer extends User {
    private final List<Book> cart = new ArrayList<>();


    public Buyer(String id,
                 String fullName,
                 String username,
                 String passwordHash,
                 String role) {
        super(id, fullName, username, passwordHash, role);
    }


    public void addToCart(Book book) {
        cart.add(book);
    }

    public List<Book> getCart() {
        return cart;
    }

    public void clearCart() {
        cart.clear();
    }
}