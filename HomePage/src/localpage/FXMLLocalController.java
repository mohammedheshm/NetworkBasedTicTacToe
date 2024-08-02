package localpage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pagemanager.Navigation;

public class FXMLLocalController implements Initializable {

    private static String difficulty = "easy";
    public static boolean isTwoPlayers = false;

    public static String getDifficulty() {
        return difficulty;
    }

    @FXML
    private void handleEasyModeButton(ActionEvent event) {
        difficulty = "easy";
        isTwoPlayers = false;
        Navigation.nextPage("/gamewindow/FXMLGameWindow.fxml");
    }

    @FXML
    private void handleMediumModeButton(ActionEvent event) {
        difficulty = "medium";
        isTwoPlayers = false;
        Navigation.nextPage("/gamewindow/FXMLGameWindow.fxml");
    }

    @FXML
    private void handleHardModeButton(ActionEvent event) {
        difficulty = "hard";
        isTwoPlayers = false;
        Navigation.nextPage("/gamewindow/FXMLGameWindow.fxml");
    }

    @FXML
    private void handleTwoPlayersButton(ActionEvent event) {
        isTwoPlayers = true;
        Navigation.nextPage("/gamewindow/FXMLGameWindow.fxml");
//        Navigation.nextPage(event, "/alert/FXMLWinner.fxml");
    }

    @FXML
    private void handleBackHButton(ActionEvent event) {
        Navigation.nextPage("/homepage/FXMLHome.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
