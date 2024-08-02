//package gamehistory;
//
//
//import dao.GameDao;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//import model.Game;
//
//public class FXMLGameHistoryController implements Initializable {
//
//    @FXML
//    private TableView<Game> tableView;
//
//    @FXML
//    private TableColumn<Game, Integer> gameId;
//
//    @FXML
//    private TableColumn<Game, String> player1;
//
//    @FXML
//    private TableColumn<Game, String> player2;
//
//    @FXML
//    private TableColumn<Game, String> winner;
//
//    @FXML
//    private TableColumn<Game, String> dateTime;
//
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        // Initialize the columns
//
//        gameId.setCellValueFactory(new PropertyValueFactory<>("gameId"));
//        player1.setCellValueFactory(new PropertyValueFactory<>("player1Id"));
//        player2.setCellValueFactory(new PropertyValueFactory<>("player2Id"));
//        winner.setCellValueFactory(new PropertyValueFactory<>("winnerId"));
//        dateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
//        
//
//        // Load data into the TableView
//        ObservableList<Game> gameHistory = FXCollections.observableArrayList(GameDao.getAllGames());
//        tableView.setItems(gameHistory);
//    }
//}
