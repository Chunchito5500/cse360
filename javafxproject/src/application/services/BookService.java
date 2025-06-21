package application.services;

import application.models.Book;
import application.utils.CSVUtils;
import application.utils.Constants;

import java.util.List;

public class BookService {
    public static List<Book> getAllBooks() {
        return CSVUtils.readBooks(Constants.BOOKS_CSV);
    }

    public static List<String> purchaseBooks(List<Book> cart) {
        return CSVUtils.purchase(cart,
                                 Constants.BOOKS_CSV,
                                 Constants.TXNS_CSV);
    }

    public static List<Book> getBooksBySeller(String sellerId) {
        return getAllBooks().stream()
            .filter(b -> b.getSellerId().equals(sellerId))
            .toList();
    }

    /** Append a new book row to books.csv */
    public static boolean addBook(Book b) {
        return CSVUtils.appendBook(b, Constants.BOOKS_CSV);
    }
}
