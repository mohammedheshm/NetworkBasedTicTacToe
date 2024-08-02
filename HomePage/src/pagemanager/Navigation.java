package pagemanager;

import alert.FXMLPlayAgainDialogController;
import alert.FXMLSingleButtonAlertController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import splashpage.SplashPage;

public class Navigation {

    public static void nextPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigation.class.getResource(fxmlPath));
            Parent root = loader.load();
            SplashPage.stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("FXML file not found: " + fxmlPath);
        }
    }

    public static void showAlert(ActionEvent event, String fxmlPath, String message, String title, Image image) {

        try {
            FXMLLoader loader = new FXMLLoader(Navigation.class.getResource(fxmlPath));
            Parent dialogRoot = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(dialogRoot));
            dialogStage.initStyle(StageStyle.UTILITY);

            if (event != null) {
                dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            }

            FXMLSingleButtonAlertController controller = loader.getController();
            if (controller != null) {
                try {
                    controller.getClass().getMethod("setDialogStage", Stage.class).invoke(controller, dialogStage);
                    controller.getClass().getMethod("setMessage", String.class).invoke(controller, message);
                    controller.getClass().getMethod("setTitle", String.class).invoke(controller, title);
                    controller.getClass().getMethod("setImage", Image.class).invoke(controller, image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean showQuitAlert(ActionEvent event, String message, String btnTextAccept,String btnTextRefuse, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(Navigation.class.getResource(fxmlPath));
            Parent dialogRoot = loader.load();

            FXMLPlayAgainDialogController controller = loader.getController();
            if (controller != null) {
                Stage dialogStage = new Stage();
                dialogStage.setScene(new Scene(dialogRoot));
                dialogStage.initStyle(StageStyle.UTILITY);

                if (event != null) {
                    dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());
                }

                controller.setDialogStage(dialogStage);
                controller.setMessage(message);
                controller.setApproveButtonText(btnTextAccept);
                controller.setRefuseButtonText(btnTextRefuse);
               

                dialogStage.showAndWait();

                return controller.isOkClicked();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
