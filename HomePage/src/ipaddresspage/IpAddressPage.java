
package ipaddresspage;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class IpAddressPage extends Application {
    
    public static Stage stage;
    
       @Override
    public void start(Stage stage) throws IOException {
        
       
        this.stage = stage;

  
    
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
