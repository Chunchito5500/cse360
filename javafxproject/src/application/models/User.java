package application.models;

public class User {
    private String id;
    private String fullName;
    private String username;
    private String passwordHash;
    private String role;

    public User(String id,
                String fullName,
                String username,
                String passwordHash,
                String role) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getId() {
        return id;
    }
    public String getFullName() {
        return fullName;
    }
    public String getUsername() {
        return username;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public String getRole() {
        return role;
    }
}