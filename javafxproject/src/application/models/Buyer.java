import java.util.ArrayList;

public class Buyer extends User {

    private ArrayList<Book> cart;
    private double fee;

    public ArrayList<Book> filterBooks(String[] condition, String category) {
        ArrayList<Book> filteredCart = new ArrayList<>();
        for (Book book : cart) {
            if((book.getCategory()).equals(condition)) {
                filteredCart.add(book);
            }
        }
        return filteredCart;
    }

    public void addToCart(Book book) {
        cart.add(book);
    }

    public ArrayList<Book> viewCart() {
        return cart;
    }

    public void purchase(Book book) {
        addtoCart(book);
        fee += book.getSellingPrice();
    }
}
