package alert;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import localpage.FXMLLocalController;

public class PlayerNameDialogController {

    @FXML
    private TextField player1NameField;

    @FXML
    private TextField player2NameField;

    @FXML
    private TextField emptyTextFieldError;

    private Stage dialogStage;
    public boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public String getPlayer1Name() {
        return player1NameField.getText();
    }

    public String getPlayer2Name() {
        return player2NameField.getText();
    }

    @FXML
    private void handleOK() {
        String player1Name = player1NameField.getText();
        String player2Name = player2NameField.getText();
        if (!player1Name.isEmpty() && !player2Name.isEmpty()) {
            okClicked = true;
            dialogStage.close();
        } else {
            emptyTextFieldError.setText("Please Enter Players Names");
        }
    }

   
}
