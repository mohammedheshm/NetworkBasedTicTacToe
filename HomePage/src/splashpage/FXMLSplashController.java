/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splashpage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pagemanager.Navigation;



/**
 * FXML Controller class
 *
 * @author user
 */
public class FXMLSplashController implements Initializable {
        
    @FXML
    public void handleNextButton(ActionEvent  event) {
            
       
       Navigation.nextPage("/homepage/FXMLHome.fxml");
       
       
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
