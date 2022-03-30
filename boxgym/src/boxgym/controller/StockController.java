package boxgym.controller;

import boxgym.helper.StageHelper;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class StockController implements Initializable {

    @FXML
    private Button addButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    void add(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/StockEntry.fxml"));
            Parent root = (Parent) loader.load();
            JMetro jMetro = new JMetro(root, Style.LIGHT);

            StageHelper.createAddOrUpdateStage("Adicionando Entrada de Estoque", root);

        } catch (IOException ex) {
            Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
