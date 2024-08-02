/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homepage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import pagemanager.Navigation;

/**
 *
 * @author user
 */
public class FXMLHomeController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private void handleLocalModeButton(ActionEvent event) {
              
        Navigation.nextPage("/local_modes/Local_Modes.fxml");

    }
    
        @FXML
    private void handleLeaderBoardButton(ActionEvent event) {
             
          Navigation.nextPage("/leaderboardpage/FXMLLeaderBoard.fxml");

    }
        @FXML
    private void handleOnlineModeButton(ActionEvent event) {
       Navigation.nextPage("/ipaddresspage/FXMLIpAddress.fxml");
 
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
