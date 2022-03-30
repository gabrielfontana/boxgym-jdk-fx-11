package boxgym.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class FirstScreenController implements Initializable {
    
    @FXML
    private AnchorPane content;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource("/boxgym/view/Login.fxml"));
            content.getChildren().removeAll();
            content.getChildren().setAll(fxml);
        } catch (IOException ex) {
            Logger.getLogger(FirstScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
