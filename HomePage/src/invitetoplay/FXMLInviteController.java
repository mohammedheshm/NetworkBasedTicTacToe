package invitetoplay;

import auth.FXMLLoginController;
import gamewindowonline.FXMLGameWindowOnlineController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import serverconnection.ServerConnect;
import models.User;
import pagemanager.Navigation;

public class FXMLInviteController {

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> playerColumn;

    @FXML
    private TableColumn<User, Integer> scoreColumn;

    @FXML
    private TableColumn<User, String> statusColumn;

    private String userEmail = FXMLLoginController.userEmail;
    private String user2Email;
    private static ScheduledExecutorService scheduler;

    
    public FXMLInviteController()
    {
        ServerConnect.fXMLInviteController = this;
        try {
            loadData();
        } catch (IOException ex) {
            Logger.getLogger(FXMLInviteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
   
    }
    @FXML
    public void initialize() {
     
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        statusColumn.setCellValueFactory(cellData
                -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus() ? "Online" : "Offline"));

        tableView.setRowFactory(tv -> new TableRow<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    setStyle(item.getStatus() ? "-fx-background-color: #D0B8A8;" : "-fx-background-color: #E0E0E0;");
                }
            }
        });

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> {
            try {
                loadData();
            } catch (IOException e) {
                showAlert("Error", "Unable to load user data.");
            }
        }), 0, 5, TimeUnit.SECONDS);
    }

    private void loadData() throws IOException {
    
        ServerConnect.sendMessage("GETPLAYERSSTATUS:" + FXMLLoginController.userEmail);

    }

    public void updateView(String[] resArr) {
        ArrayList<User> usersObj = new ArrayList<>();
        FXCollections.observableArrayList().clear();
        for (int i = 1; i < resArr.length; i++) {
            User user = new User(resArr[i]);
            usersObj.add(user);
        }

        ObservableList<User> users = FXCollections.observableArrayList(usersObj);
        tableView.setItems(users);
    }

    @FXML
    private void handleInviteButton(ActionEvent event) throws IOException {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null && selectedUser.getStatus()) {

            String message = "INVITE:" + selectedUser.getEmail() + ":" + userEmail;
            ServerConnect.sendMessage(message);
          
        } else {
            showAlert("No User Selected or User Offline", "Please select an online user from the table.");
        }
    }

    public void showAlert(String title, String opponentName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Invitation");
        alert.setContentText(opponentName + " wants to play with you.");
        ButtonType acceptButton = new ButtonType("Accept");
        ButtonType refuseButton = new ButtonType("Refuse");
        alert.getButtonTypes().setAll(acceptButton, refuseButton);
        alert.showAndWait().ifPresent(response -> {
            if (response == acceptButton) {
                String msg1 = "ACCEPT_INVITE:" + userEmail + ":" + opponentName;
                ServerConnect.sendMessage(msg1);
                navigateToBoard();
                FXMLGameWindowOnlineController.opponnent = opponentName;
                FXMLGameWindowOnlineController.isUserTurn = false;
                FXMLGameWindowOnlineController.playerSymbol = "O";
            } else{
                String msg2 = "DECLINE_INVITE:" + userEmail + ":" + opponentName;
                ServerConnect.sendMessage(msg2);
            }
        });
    }
    
    
    public void navigateToBoard()
    {
         Navigation.nextPage("/gamewindowonline/FXMLGameWindowOnline.fxml");
    }

    public static void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

}
