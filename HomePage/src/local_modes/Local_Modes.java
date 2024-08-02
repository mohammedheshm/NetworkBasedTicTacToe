package local_modes;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Local_Modes extends Application {

    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("LocalModes.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setResizable(false); // Stop Resize of Screen 
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
