package boxgym.controller;

import boxgym.dao.SaleDao;
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
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class SalesController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    private void sale(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/boxgym/view/SalesAdd.fxml"));
            Parent root = (Parent) loader.load();
            JMetro jMetro = new JMetro(root, Style.LIGHT);
            
            SalesAddController controller = loader.getController();
            
            StageHelper.createAddOrUpdateStage("Adicionando Venda", root);
            
            if (controller.isSaleCreationFlag() && !controller.isProductsEntryCreationFlag()) {
                SaleDao saleDao = new SaleDao();
                saleDao.deleteLastEntry();
            } else if (controller.isSaleCreationFlag() && controller.isProductsEntryCreationFlag()) {
                //refreshTableView();
                //saleTableView.getSelectionModel().selectLast();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
