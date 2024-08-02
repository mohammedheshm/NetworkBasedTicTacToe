

package splashpage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class SplashPage extends Application {
    
    public static Stage stage;
    
    @Override
    public void start(Stage stage) {
        
       
        this.stage = stage;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLSplash.fxml"));
            
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SplashPage.class.getName()).log(Level.SEVERE, null, ex);
        }
  
    
    }

   
    public static void main(String[] args) {
        launch(args);
    }
    
}
