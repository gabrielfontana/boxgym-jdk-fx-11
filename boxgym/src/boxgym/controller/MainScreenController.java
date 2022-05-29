package boxgym.controller;

import boxgym.dao.CustomerDao;
import boxgym.dao.ProductDao;
import boxgym.dao.StockEntryDao;
import boxgym.dao.SupplierDao;
import boxgym.helper.AlertHelper;
import boxgym.helper.StageHelper;
import java.io.File;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class MainScreenController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private AnchorPane contentArea;

    AlertHelper alert = new AlertHelper();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            changeContentArea("/boxgym/view/Home.fxml");
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void changeContentArea(String path) throws IOException {
        AnchorPane anchorPane = (AnchorPane) FXMLLoader.load(getClass().getResource(path));
        contentArea.getChildren().setAll(anchorPane);

        /*Parent fxml = FXMLLoader.load(getClass().getResource(path));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxml);*/
    }

    @FXML
    void handleHome(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Home.fxml");
    }

    @FXML
    void handleLogout(ActionEvent event) throws IOException {
        alert.confirmationAlert("Tem certeza que deseja sair do sistema?", "");

        if (alert.getResult().get() == ButtonType.YES) {
            borderPane.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("/boxgym/view/FirstScreen.fxml"));
            JMetro jMetro = new JMetro(root, Style.LIGHT);
            StageHelper.openFirstScreenAfterLogout("Login", root);
        }
    }

    @FXML
    void handlePlans(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Plans.fxml");
    }

    @FXML
    void handleCustomers(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Customers.fxml");
    }

    @FXML
    void handleSuppliers(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Suppliers.fxml");
    }

    @FXML
    void handleBillsToPay(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/BillsToPay.fxml");
    }

    @FXML
    void handleBillsToReceive(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/BillsToReceive.fxml");
    }

    @FXML
    void handleBanks(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Banks.fxml");
    }

    @FXML
    void handleSales(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Sales.fxml");
    }

    @FXML
    void handleProducts(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Products.fxml");
    }

    @FXML
    void handleStockEntry(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/StockEntry.fxml");
    }

    @FXML
    void handleFiles(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Files.fxml");
    }

    @FXML
    void handleExercises(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Exercises.fxml");
    }

    @FXML
    void handleMeasurements(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Measurements.fxml");
    }

    @FXML
    void handleAbout(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/About.fxml");
    }

    @FXML
    void exportCustomersToExcel(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pasta de Trabalho do Excel", "*.xlsx"));
        File file = chooser.showSaveDialog(new Stage());

        CustomerDao customerDao = new CustomerDao();

        if (file != null) {
            customerDao.createExcelFile(file.getAbsolutePath());
            alert.customAlert(Alert.AlertType.WARNING, "Relatório gerado com sucesso!", "");
        }
    }

    @FXML
    void exportSuppliersToExcel(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pasta de Trabalho do Excel", "*.xlsx"));
        File file = chooser.showSaveDialog(new Stage());

        SupplierDao supplierDao = new SupplierDao();

        if (file != null) {
            supplierDao.createExcelFile(file.getAbsolutePath());
            alert.customAlert(Alert.AlertType.WARNING, "Relatório gerado com sucesso!", "");
        }
    }

    @FXML
    void exportSalesToExcel(ActionEvent event) {

    }

    @FXML
    void exportProductsToExcel(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pasta de Trabalho do Excel", "*.xlsx"));
        File file = chooser.showSaveDialog(new Stage());
        
        ProductDao productDao = new ProductDao();

        if (file != null) {
            productDao.createExcelFile(file.getAbsolutePath());
            alert.customAlert(Alert.AlertType.WARNING, "Relatório gerado com sucesso!", "");
        }
    }

    @FXML
    void exportStockEntriesToExcel(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pasta de Trabalho do Excel", "*.xlsx"));
        File file = chooser.showSaveDialog(new Stage());

        StockEntryDao stockEntryDao = new StockEntryDao();

        if (file != null) {
            stockEntryDao.createExcelFile(file.getAbsolutePath());
            alert.customAlert(Alert.AlertType.WARNING, "Relatório gerado com sucesso!", "");
        }
    }
}
