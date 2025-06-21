package application.utils;

import application.models.User;
import application.models.Book;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;

public class CSVUtils {

    // Read all users from the given CSV file.

    public static List<User> readUsers(String path) {
        List<User> users = new ArrayList<>();
        Path p = Paths.get(path);
        if (!Files.exists(p)) return users;

        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 5) continue;
                User u = new User(
                    parts[0], // id
                    parts[1], // fullName
                    parts[2], // username
                    parts[3], // passwordHash
                    parts[4]  // role
                );
                users.add(u);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Append a new user record to the CSV file. Creates file and directories if needed.

    public static boolean appendUser(User u, String path) {
        Path p = Paths.get(path);
        try {
            if (p.getParent() != null && !Files.exists(p.getParent())) {
                Files.createDirectories(p.getParent());
            }
            String row = String.join(",",
                u.getId(),
                u.getFullName(),
                u.getUsername(),
                u.getPasswordHash(),
                u.getRole()
            );
            Files.write(
                p,
                Collections.singletonList(row),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read all books from the given CSV file.

    public static List<Book> readBooks(String path) {
        List<Book> books = new ArrayList<>();
        Path p = Paths.get(path);
        if (!Files.exists(p)) return books;

        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 9) continue;
                Book b = new Book(
                    parts[0],                         // id
                    parts[1],                         // sellerId
                    parts[2],                         // title
                    parts[3],                         // author
                    parts[4],                         // category
                    parts[5],                         // condition
                    Double.parseDouble(parts[6]),     // sellingPrice
                    Integer.parseInt(parts[7]),       // quantity
                    LocalDate.parse(parts[8])         // dateListed
                );
                books.add(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Append a new book record to the CSV file.
    public static boolean appendBook(Book b, String path) {
        Path p = Paths.get(path);
        try {
            if (p.getParent() != null && !Files.exists(p.getParent())) {
                Files.createDirectories(p.getParent());
            }
            String row = String.join(",",
                b.getId(),
                b.getSellerId(),
                b.getTitle(),
                b.getAuthor(),
                b.getCategory(),
                b.getCondition(),
                String.valueOf(b.getSellingPrice()),
                String.valueOf(b.getQuantity()),
                b.getDateListed().toString()
            );
            Files.write(
                p,
                Collections.singletonList(row),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static List<String> purchase(List<Book> cart,
                                        String booksCsv,
                                        String txnsCsv) {
        // Need to implement purchase logic
        return new ArrayList<>();
    }
}
