package application.models;

import java.time.LocalDateTime;


public class AdminLog {

    private final String         id;
    private final String         username;
    private final String         bookTitle;
    private final boolean        listed;
    private final boolean        bought;
    private final double         price;
    private final LocalDateTime  timestamp;

    public AdminLog(String id,
                    String username,
                    String bookTitle,
                    boolean listed,
                    boolean bought,
                    double price,
                    LocalDateTime ts) {
        this.id        = id;
        this.username  = username;
        this.bookTitle = bookTitle;
        this.listed    = listed;
        this.bought    = bought;
        this.price     = price;
        this.timestamp = ts;
    }

    // getters //

    public String        getId()        { return id; }
    public String        getUsername()  { return username; }
    public String        getBookTitle() { return bookTitle; }
    public boolean       isListed()     { return listed; }
    public boolean       isBought()     { return bought; }
    public double        getPrice()     { return price; }
    public LocalDateTime getTimestamp() { return timestamp; }
}