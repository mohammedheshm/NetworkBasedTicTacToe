/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alert;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.MediaView;

/**
 * FXML Controller class
 *
 * @author user
 */
public class FXMLLoserController implements Initializable {

   @FXML
    private MediaView mediaView1;

    public MediaView getMediaView() {
        return mediaView1;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
