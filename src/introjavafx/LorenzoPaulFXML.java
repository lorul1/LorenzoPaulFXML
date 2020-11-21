package introjavafx;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

public class LorenzoPaulFXML extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {        
        
        Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLDocument.fxml"));        
        Scene scene = new Scene(root);        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
