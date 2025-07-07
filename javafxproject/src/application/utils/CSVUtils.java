package application.utils;

import application.models.AdminLog;
import application.models.Book;
import application.models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {



    public static List<User> readUsers(String path) {
        List<User> users = new ArrayList<>();
        Path p = Paths.get(path);
        if (!Files.exists(p)) return users;
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length == 5)
                    users.add(new User(parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return users;
    }

    public static boolean writeUsers(List<User> users, String path) {
        Path p = Paths.get(path);
        try {
            if (p.getParent()!=null && !Files.exists(p.getParent()))
                Files.createDirectories(p.getParent());
            List<String> lines = users.stream().map(u -> String.join(",",
                    u.getId(), u.getFullName(), u.getUsername(),
                    u.getPasswordHash(), u.getRole())).toList();
            Files.write(p, lines, StandardOpenOption.CREATE,
                               StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e){ e.printStackTrace(); return false; }
    }



    private static final int BOOK_COLS = 10;

    public static List<Book> readBooks(String path) {
        List<Book> books = new ArrayList<>();
        Path p = Paths.get(path);
        if (!Files.exists(p)) return books;
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] s = line.split(",", BOOK_COLS);
                if (s.length < BOOK_COLS) continue;
                books.add(new Book(
                        s[0], s[1], s[2], s[3], s[4], s[5],
                        Double.parseDouble(s[6]),
                        Integer.parseInt(s[7]),
                        s[8],
                        LocalDate.parse(s[9])));
            }
        } catch (IOException e){ e.printStackTrace(); }
        return books;
    }

    public static boolean writeBooks(List<Book> books, String path) {
        Path p = Paths.get(path);
        try {
            if (p.getParent()!=null && !Files.exists(p.getParent()))
                Files.createDirectories(p.getParent());
            List<String> rows = books.stream().map(b -> String.join(",",
                    b.getId(), b.getSellerId(), b.getTitle(), b.getAuthor(),
                    b.getCategory(), b.getCondition(),
                    String.valueOf(b.getSellingPrice()),
                    String.valueOf(b.getQuantity()),
                    b.getDescription().replace(",", ";"),
                    b.getDateListed().toString()
            )).toList();
            Files.write(p, rows, StandardOpenOption.CREATE,
                               StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e){ e.printStackTrace(); return false; }
    }

    public static boolean appendBook(Book b, String path) {
        List<Book> list = readBooks(path);
        list.add(b);
        return writeBooks(list, path);
    }


    public static List<String> purchase(List<Book> cart, String booksCsv) {
        List<Book> inventory = readBooks(booksCsv);
        List<String> oos = new ArrayList<>();

        for (Book c : cart) {
            Book store = inventory.stream()
                                  .filter(b -> b.getId().equals(c.getId()))
                                  .findFirst().orElse(null);
            if (store == null || store.getQuantity() <= 0) {
                oos.add(c.getTitle()); continue;
            }
            store.setQuantity(store.getQuantity() - 1);
        }
        inventory.removeIf(b -> b.getQuantity() <= 0);
        writeBooks(inventory, booksCsv);
        return oos;
    }



    private static final DateTimeFormatter LOG_FMT =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static List<AdminLog> readLogs(String path) {
        List<AdminLog> logs = new ArrayList<>();
        Path p = Paths.get(path);
        if (!Files.exists(p)) return logs;
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] s = line.split(",", 7);
                if (s.length == 7)
                    logs.add(new AdminLog(s[0], s[1], s[2],
                            Boolean.parseBoolean(s[3]),
                            Boolean.parseBoolean(s[4]),
                            Double.parseDouble(s[5]),
                            LocalDateTime.parse(s[6], LOG_FMT)));
            }
        } catch (IOException e){ e.printStackTrace(); }
        return logs;
    }

    public static boolean appendLog(AdminLog log, String path) {
        Path p = Paths.get(path);
        try {
            if (p.getParent()!=null && !Files.exists(p.getParent()))
                Files.createDirectories(p.getParent());
            String row = String.join(",",
                    log.getId(), log.getUsername(),
                    log.getBookTitle().replace(",", ";"),
                    String.valueOf(log.isListed()),
                    String.valueOf(log.isBought()),
                    String.valueOf(log.getPrice()),
                    log.getTimestamp().format(LOG_FMT));
            Files.write(p, List.of(row), StandardOpenOption.CREATE,
                                    StandardOpenOption.APPEND);
            return true;
        } catch (IOException e){ e.printStackTrace(); return false; }
    }
    public static boolean deleteUser(String userId) {

    	  List<User> users = readUsers(Constants.USERS_CSV);
    	  users.removeIf(u -> u.getId().equals(userId));
    	  boolean uOk = writeUsers(users, Constants.USERS_CSV);

    	  List<Book> books = readBooks(Constants.BOOKS_CSV);
    	  books.removeIf(b -> b.getSellerId().equals(userId));
    	  boolean bOk = writeBooks(books, Constants.BOOKS_CSV);
    	  return uOk && bOk;
    	}
    public static boolean updateUserRole(String userId, String newRole) {
    	  var users = readUsers(Constants.USERS_CSV);
    	  for (User u: users)
    	    if (u.getId().equals(userId)) u.setRole(newRole);
    	  return writeUsers(users, Constants.USERS_CSV);
    	}
    public static boolean deleteBooksBySeller(String sellerId) {
    	  var books = readBooks(Constants.BOOKS_CSV);
    	  books.removeIf(b -> b.getSellerId().equals(sellerId));
    	  return writeBooks(books, Constants.BOOKS_CSV);
    	}
}