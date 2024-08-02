
package alert;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class FXMLSingleButtonAlertController implements Initializable {
   private Stage dialogStage;

    @FXML
    private Text messageLabel;

    @FXML
    private ImageView errorImageView;

    @FXML
    private Text titleLabel;

    @FXML
    private void handleOkButton() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setImage(Image image) {
        errorImageView.setImage(image);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code here
    }    
}

