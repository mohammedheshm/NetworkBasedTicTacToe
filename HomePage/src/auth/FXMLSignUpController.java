package auth;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pagemanager.Navigation;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import serverconnection.ServerConnect;

public class FXMLSignUpController implements Initializable {
    
 Image noWinner = new Image(getClass().getResourceAsStream("/resources/response.png"));


    public FXMLSignUpController() {
        ServerConnect.fXMLSignUpController = this;
    }
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailtxtField;

    @FXML
    private PasswordField passwordtxtField;

    @FXML
    private PasswordField rePasswordField;

    @FXML
    private void handleBackLoginButton(ActionEvent event) {
        Navigation.nextPage("/auth/FXMLLogin.fxml");
    }

    @FXML
    private void handleSignUpButton(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailtxtField.getText();
        String password = passwordtxtField.getText();
        String rePassword = rePasswordField.getText();

        // Validate inputs
        if (username == null || username.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()
                || rePassword == null || rePassword.trim().isEmpty()) {

            Navigation.showAlert(null, "/alert/FXMLSingleButtonAlert.fxml", "Please fill in all fields.", "Input Error", noWinner);

            //showAlert("Input Error", "Please fill in all fields.");
            return;
        }

        if (!password.equals(rePassword)) {
//            showAlert("", "");
            
            Navigation.showAlert(null, "/alert/FXMLSingleButtonAlert.fxml", "Passwords do not match.", "Password Error", noWinner);

            return;
        }
        ServerConnect.sendMessage("ISEXIST:" + username + ":" + password + ":" + email);

        // Navigate to login page
        Navigation.nextPage("/auth/FXMLLogin.fxml");
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize if needed
    }
}
