package application.controllers;

import application.Main;
import application.models.AdminLog;
import application.models.Book;
import application.models.User;
import application.services.BookService;
import application.services.Session;
import application.utils.CSVUtils;
import application.utils.Constants;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import application.controllers.BookCardController;

public class AdminDashboardController {

    @FXML private Label userLabel;

    @FXML private Button txnBtn, usersBtn, listingBtn, reportsBtn;

    @FXML private VBox transactionsPane, userMgmtPane, reportsPane;
    @FXML private ScrollPane listingPane;
    @FXML private TilePane listingsGrid;

    @FXML private TableView<AdminTxnRow> txnTable;
    @FXML private TableColumn<AdminTxnRow,String> userCol, bookCol, listedCol, boughtCol, timeCol;
    @FXML private TableColumn<AdminTxnRow,Double> priceCol;
    @FXML private TextField txnSearchField;
    private final ObservableList<AdminTxnRow> txnRows = FXCollections.observableArrayList();
    private List<AdminLog> allLogs;

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User,String> uUsernameCol, uNameCol, uRoleCol;
    @FXML private TextField searchUserField;
    private List<User> allUsers;

    @FXML private HBox userModifyPane;
    @FXML private Label   selectedUserLabel;
    @FXML private Button  deleteUserBtn, toggleRoleBtn, clearSelectionBtn;

    @FXML private BarChart<String,Number> salesChart;

    @FXML
    public void initialize() {

        userLabel.setText(Session.getCurrentUser().getUsername());

        Platform.runLater(() -> {
            Scene sc = userLabel.getScene();
            URL css = getClass().getResource("/application/css/admin.css");
            if (css != null && !sc.getStylesheets().contains(css.toExternalForm()))
                sc.getStylesheets().add(css.toExternalForm());
        });

        userCol  .setCellValueFactory(new PropertyValueFactory<>("username"));
        bookCol  .setCellValueFactory(new PropertyValueFactory<>("book"));
        listedCol.setCellValueFactory(new PropertyValueFactory<>("listed"));
        boughtCol.setCellValueFactory(new PropertyValueFactory<>("bought"));
        priceCol .setCellValueFactory(new PropertyValueFactory<>("price"));
        timeCol  .setCellValueFactory(new PropertyValueFactory<>("time"));
        txnTable.setItems(txnRows);

        uUsernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        uNameCol    .setCellValueFactory(new PropertyValueFactory<>("fullName"));
        uRoleCol    .setCellValueFactory(new PropertyValueFactory<>("role"));

        loadLogs();
        loadUsers();
        showTransactions(null);

        userModifyPane.setVisible(false);
        userModifyPane.setManaged(false);

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldU, selU) -> {
            if (selU == null) {
                userModifyPane.setVisible(false);
                userModifyPane.setManaged(false);
            } else {
                selectedUserLabel.setText(selU.getUsername() + " (" + selU.getRole() + ")");
                toggleRoleBtn.setDisable("Admin".equals(selU.getRole()));
                deleteUserBtn.setDisable(false);
                userModifyPane.setVisible(true);
                userModifyPane.setManaged(true);
            }
        });

        reportsPane.setVisible(false);
    }

    @FXML private void showTransactions(ActionEvent e) {
        selectMode(txnBtn);
        transactionsPane.setVisible(true);    transactionsPane.setManaged(true);
        userMgmtPane  .setVisible(false);     userMgmtPane  .setManaged(false);
        listingPane   .setVisible(false);     listingPane   .setManaged(false);
        reportsPane   .setVisible(false);     reportsPane   .setManaged(false);
    }

    @FXML private void showUsers(ActionEvent e) {
        selectMode(usersBtn);
        transactionsPane.setVisible(false);   transactionsPane.setManaged(false);
        userMgmtPane  .setVisible(true);      userMgmtPane  .setManaged(true);
        listingPane   .setVisible(false);     listingPane   .setManaged(false);
        reportsPane   .setVisible(false);     reportsPane   .setManaged(false);
    }

    @FXML private void showListings(ActionEvent e) {
        selectMode(listingBtn);
        transactionsPane.setVisible(false);   transactionsPane.setManaged(false);
        userMgmtPane  .setVisible(false);     userMgmtPane  .setManaged(false);
        listingPane   .setVisible(true);      listingPane   .setManaged(true);
        reportsPane   .setVisible(false);     reportsPane   .setManaged(false);
        loadListingGrid();
    }

    @FXML private void showReports(ActionEvent e) {
        selectMode(reportsBtn);
        transactionsPane.setVisible(false);   transactionsPane.setManaged(false);
        userMgmtPane  .setVisible(false);     userMgmtPane  .setManaged(false);
        listingPane   .setVisible(false);     listingPane   .setManaged(false);
        reportsPane   .setVisible(true);      reportsPane   .setManaged(true);
        loadReports();
    }

    private void selectMode(Button sel) {
        txnBtn    .getStyleClass().remove("mode-selected");
        usersBtn  .getStyleClass().remove("mode-selected");
        listingBtn.getStyleClass().remove("mode-selected");
        reportsBtn.getStyleClass().remove("mode-selected");
        sel.getStyleClass().add("mode-selected");
        userModifyPane.setVisible(false);
        userModifyPane.setManaged(false);
    }

    private void loadLogs() {
        allLogs = CSVUtils.readLogs("data/logs.csv");
        txnRows.setAll(allLogs.stream().map(AdminTxnRow::new).toList());
    }

    @FXML private void handleTxnSearch(ActionEvent e) {
        String q = txnSearchField.getText().trim().toLowerCase();
        var filtered = q.isEmpty()
            ? allLogs
            : allLogs.stream()
                     .filter(l -> l.getUsername().toLowerCase().contains(q)
                               || l.getBookTitle().toLowerCase().contains(q))
                     .toList();
        txnRows.setAll(filtered.stream().map(AdminTxnRow::new).toList());
    }

    private void loadUsers() {
        allUsers = CSVUtils.readUsers(Constants.USERS_CSV);
        userTable.setItems(FXCollections.observableArrayList(allUsers));
    }

    @FXML private void handleUserSearch(ActionEvent e) {
        String q = searchUserField.getText().trim().toLowerCase();
        var filtered = q.isEmpty()
            ? allUsers
            : allUsers.stream()
                      .filter(u -> u.getUsername().toLowerCase().contains(q)
                                || u.getFullName().toLowerCase().contains(q))
                      .toList();
        userTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML private void handleDeleteUser(ActionEvent e) {
        User sel = userTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Deleting user “" + sel.getUsername() + "” will remove ALL their book listings.\nContinue?",
            ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Confirm Delete");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.orElse(ButtonType.NO) != ButtonType.YES) return;

        BookService.getBooksBySeller(sel.getId())
                   .forEach(b -> BookService.deleteBook(b.getId()));

        allUsers.removeIf(u -> u.getId().equals(sel.getId()));
        CSVUtils.writeUsers(allUsers, Constants.USERS_CSV);

        userTable.setItems(FXCollections.observableArrayList(allUsers));
        handleClearSelection(null);
    }

    @FXML private void handleToggleRole(ActionEvent e) {
        User sel = userTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        String newRole = sel.getRole().equals("Buyer") ? "Seller" : "Buyer";
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Switch “" + sel.getUsername() + "” to " + newRole
            + "?\n(Note: Their existing listings will be removed.)",
            ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Confirm Role Change");
        if (confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) return;

        if ("Seller".equals(sel.getRole())) {
            BookService.getBooksBySeller(sel.getId())
                       .forEach(b -> BookService.deleteBook(b.getId()));
        }

        sel.setRole(newRole);
        CSVUtils.writeUsers(allUsers, Constants.USERS_CSV);
        userTable.refresh();

        selectedUserLabel.setText(sel.getUsername() + " (" + sel.getRole() + ")");
        toggleRoleBtn.setDisable("Admin".equals(sel.getRole()));
    }

    @FXML private void handleClearSelection(ActionEvent e) {
        userTable.getSelectionModel().clearSelection();
        userModifyPane.setVisible(false);
        userModifyPane.setManaged(false);
    }

    private void loadListingGrid() {
        listingsGrid.getChildren().clear();
        var books = CSVUtils.readBooks(Constants.BOOKS_CSV);

        for (Book b : books) {
            try {
                FXMLLoader fx = new FXMLLoader(
                    getClass().getResource("/application/views/BookCard.fxml"));
                VBox card = fx.load();

                BookCardController bc = fx.getController();
                bc.init(b, book -> {
                    if (BookService.deleteBook(book.getId())) {
                        CSVUtils.appendLog(new AdminLog(
                                UUID.randomUUID().toString(),
                                userLabel.getText(),
                                book.getTitle(),
                                false, false,
                                book.getSellingPrice(),
                                LocalDateTime.now()), Constants.TXNS_CSV);
                        loadListingGrid();
                    }
                });
                bc.getAddButton().setText("Delete");
                listingsGrid.getChildren().add(card);

            } catch (IOException ex) { ex.printStackTrace(); }
        }
    }

    private void loadReports() {
        salesChart.getData().clear();

        var series = new XYChart.Series<String,Number>();
        allLogs.stream()
               .filter(AdminLog::isBought)
               .map(AdminLog::getBookTitle)
               .distinct()
               .forEach(title -> {
                   long count =
                       allLogs.stream().filter(l -> l.isBought() && l.getBookTitle().equals(title)).count();
                   series.getData().add(new XYChart.Data<>(title, count));
               });
        salesChart.getData().add(series);
    }

    @FXML
    private void handleLogout(ActionEvent e) {
        Session.clear();
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/application/views/LoginPage.fxml"));
            Scene scene = new Scene(root, 900, 600);
            URL css = getClass().getResource("/application/css/style.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());
            Main.primaryStage.setScene(scene);
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    public static class AdminTxnRow {
        private final AdminLog log;
        public AdminTxnRow(AdminLog log){ this.log = log; }
        public String getUsername(){ return log.getUsername(); }
        public String getBook(){ return log.getBookTitle(); }
        public String getListed(){ return log.isListed() ? "✔" : ""; }
        public String getBought(){ return log.isBought() ? "✔" : ""; }
        public double getPrice(){ return log.getPrice(); }
        public String getTime(){ return log.getTimestamp().toString(); }
    }
}