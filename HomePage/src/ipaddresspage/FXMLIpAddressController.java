package ipaddresspage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pagemanager.Navigation;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import serverconnection.ServerConnect;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLIpAddressController implements Initializable {

    @FXML
    private TextField txtFieldipAdress;

    @FXML
    private Label lblConnectionStatus;

    
    
    @FXML
    public void HandleBtnIpAddressConnect(ActionEvent event) {
        if (txtFieldipAdress == null || lblConnectionStatus == null) {
            Logger.getLogger(FXMLIpAddressController.class.getName()).log(Level.SEVERE, "FXML fields are not initialized");
            return;
        }

        String ipAddress = txtFieldipAdress.getText();

        try {
            ServerConnect.setSERVER_ADDRESS(ipAddress);
            ServerConnect.makeConnectionWithServer();
            if (ServerConnect.isConnection()) {
                lblConnectionStatus.setText("Connected to server at " + ipAddress);
                Navigation.nextPage("/auth/FXMLLogin.fxml");
            } else {
                lblConnectionStatus.setText("Failed to connect to server.");
            }
        } catch (Exception ex) {
            Logger.getLogger(FXMLIpAddressController.class.getName()).log(Level.SEVERE, null, ex);
            lblConnectionStatus.setText("Failed to connect to server.");
        }
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code, if needed
    }
}
