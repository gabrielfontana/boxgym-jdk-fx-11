package boxgym;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class Main extends Application {

    public static Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/boxgym/view/FirstScreen.fxml"));
        JMetro jMetro = new JMetro(root, Style.LIGHT);
        Main.stage = stage;
        stage.getIcons().add(new Image("boxgym/img/dumbbell.png"));
        stage.setResizable(false);
        stage.setTitle("Login"); 
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
