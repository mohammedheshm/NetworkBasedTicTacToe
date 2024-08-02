package auth;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import pagemanager.Navigation;
import serverconnection.ServerConnect;

public class FXMLLoginController implements Initializable {

    Image noWinner = new Image(getClass().getResourceAsStream("/resources/response.png"));

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    public static String userEmail;

    public FXMLLoginController() {

    }

    @FXML
    private void handleSignUpButton(ActionEvent event) {
        Navigation.nextPage("/auth/FXMLSignUp.fxml");
    }

    @FXML
    private void handleBackHButton(ActionEvent event) {
        Navigation.nextPage("/homepage/FXMLHome.fxml");
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
//            showAlert("Input Error", "Please enter both email and password.");
            Navigation.showAlert(null, "/alert/FXMLSingleButtonAlert.fxml", "Please enter both email and password.", "Input Error", noWinner);
            return;
        } else {
            ServerConnect.sendMessage("LOGIN:" + email + ":" + password);
        }

    }

//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code here
    }
}
