package serverhomepage;

import server.TicTacToeServer;
import Dao.UserManager;
import java.io.InputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.User;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;

public class ServerHomePageController {

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> playerColumn;

    @FXML
    private TableColumn<User, Integer> scoreColumn;

    @FXML
    private TableColumn<User, String> statusColumn;

    @FXML
    private Button btnstartstop;

    @FXML
    private Text serverstatus;

    @FXML
    private ImageView offonimg;

    @FXML
    private Image onImg;
    private Image offImg;
    
    private TicTacToeServer server;
    private Thread serverThread;
    private static ScheduledExecutorService scheduler;


    @FXML
    private void initialize() {
        // Load images for server status
        loadServerStatusImages();
        
        // Set up TableView columns
        setupTableViewColumns();
        
        //Make All user offline
        UserManager.setAllUsersOffline();
        
        schedulePlayerDataReload();
        
        // Initialize server status image to offline
        offonimg.setImage(offImg);
        
    }

    private void loadServerStatusImages() {
        try (InputStream onImgStream = getClass().getResourceAsStream("/resources/onlineeee.png");
             InputStream offImgStream = getClass().getResourceAsStream("/resources/offlineee.png")) {

            if (onImgStream != null) {
                onImg = new Image(onImgStream);
            } else {
                System.out.println("Error: onlineeee.png not found");
            }

            if (offImgStream != null) {
                offImg = new Image(offImgStream);
            } else {
                System.out.println("Error: offlineee.png not found");
            }
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }
    }

    private void setupTableViewColumns() {
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        statusColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(user.getStatus());
        });

        // Customize TableRow factory
        tableView.setRowFactory(tv -> new TableRow<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    setStyle("Online".equals(item.getStatus())? "-fx-background-color: #D0B8A8;" : "-fx-background-color: #E0E0E0;");
                }
            }
        });
    }
    
        public void loadPlayerData() {
        try {
            ObservableList<User> users = FXCollections.observableArrayList(UserManager.getAllUsers());
            Platform.runLater(() -> tableView.setItems(users));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading player data: " + e.getMessage());
        }
    }

    private void schedulePlayerDataReload() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> loadPlayerData(), 0, 10, TimeUnit.SECONDS);
    }

    
    
    @FXML
    private void handleBtnStartStopServer(ActionEvent event) {
        if ("Server is Off".equals(serverstatus.getText())) {
            startServer();
            btnstartstop.setText("Stop");
            serverstatus.setText("Server is On");
            offonimg.setImage(onImg); // Update image to indicate server is online
            loadPlayerData();
        } else {
            stopServer();
            btnstartstop.setText("Start");
            serverstatus.setText("Server is Off");
            offonimg.setImage(offImg); // Update image to indicate server is offline
        }
    }

    private void startServer() {
        if (server == null) {
            server = new TicTacToeServer();
        }
        serverThread =  new Thread(() -> {
            try {
                server.start();
               
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error starting server: " + e.getMessage());
            }
        });
        serverThread.start();
    }

    private void stopServer() {
        if (server != null) {
            try {
                
                if(serverThread != null && serverThread.isAlive())
                {
                    serverThread.interrupt();
                }
                UserManager.setAllUsersOffline();
                server.stop();
                scheduler.shutdown();
                
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error stopping server: " + e.getMessage());
            }
        }
    }
}
