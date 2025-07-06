package application.utils;

import application.models.User;
import application.models.Book;
import application.models.AdminLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
                if (parts.length < 5) continue;
                users.add(new User(parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static boolean appendUser(User u, String path) {
        Path p = Paths.get(path);
        try {
            if (p.getParent() != null && !Files.exists(p.getParent()))
                Files.createDirectories(p.getParent());
            String row = String.join(",",
                u.getId(), u.getFullName(), u.getUsername(),
                u.getPasswordHash(), u.getRole());
            Files.write(p, List.of(row),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Book> readBooks(String path) {
        List<Book> books = new ArrayList<>();
        Path p = Paths.get(path);
        if (!Files.exists(p)) return books;
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 9);
                if (parts.length < 9) continue;
                books.add(new Book(
                    parts[0], parts[1], parts[2], parts[3], parts[4],
                    parts[5], Double.parseDouble(parts[6]),
                    Integer.parseInt(parts[7]),
                    LocalDate.parse(parts[8])
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    public static boolean appendBook(Book b, String path) {
        Path p = Paths.get(path);
        try {
            if (p.getParent() != null && !Files.exists(p.getParent()))
                Files.createDirectories(p.getParent());
            String row = String.join(",",
                b.getId(), b.getSellerId(), b.getTitle(), b.getAuthor(),
                b.getCategory(), b.getCondition(),
                String.valueOf(b.getSellingPrice()),
                String.valueOf(b.getQuantity()),
                b.getDateListed().toString());
            Files.write(p, List.of(row),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static final DateTimeFormatter LOG_FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static List<AdminLog> readLogs(String path) {
        List<AdminLog> logs = new ArrayList<>();
        Path p = Paths.get(path);
        if (!Files.exists(p)) return logs;
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 7);
                if (parts.length < 7) continue;
                logs.add(new AdminLog(
                    parts[0], parts[1], parts[2],
                    Boolean.parseBoolean(parts[3]),
                    Boolean.parseBoolean(parts[4]),
                    Double.parseDouble(parts[5]),
                    LocalDateTime.parse(parts[6], LOG_FMT)
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public static boolean appendLog(AdminLog log, String path) {
        Path p = Paths.get(path);
        try {
            if (p.getParent() != null && !Files.exists(p.getParent()))
                Files.createDirectories(p.getParent());
            String row = String.join(",",
                log.getId(),
                log.getUsername(),
                log.getBookTitle().replace(',', ';'),
                String.valueOf(log.isListed()),
                String.valueOf(log.isBought()),
                String.valueOf(log.getPrice()),
                log.getTimestamp().format(LOG_FMT)
            );
            Files.write(p, List.of(row),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeUsers(List<User> users, String path) {
        Path p = Paths.get(path);
        try {
            if (p.getParent() != null && !Files.exists(p.getParent()))
                Files.createDirectories(p.getParent());
            List<String> lines = users.stream()
                .map(u -> String.join(",",
                    u.getId(),
                    u.getFullName(),
                    u.getUsername(),
                    u.getPasswordHash(),
                    u.getRole()
                ))
                .toList();
            Files.write(p, lines,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.CREATE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> purchase(
        List<Book> cart, String booksCsv, String txnsCsv) {
        return new ArrayList<>();
    }
    
    public static boolean writeBooks(List<Book> books, String path) {
        Path p = Paths.get(path);
        try {
            if (p.getParent()!=null && !Files.exists(p.getParent()))
                Files.createDirectories(p.getParent());
            List<String> lines = books.stream().map(b -> String.join(",",
                b.getId(), b.getSellerId(), b.getTitle(), b.getAuthor(),
                b.getCategory(), b.getCondition(),
                String.valueOf(b.getSellingPrice()),
                String.valueOf(b.getQuantity()),
                b.getDateListed().toString()
            )).toList();
            Files.write(p, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException ex){ ex.printStackTrace(); return false; }
    }
}