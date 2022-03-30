package boxgym.controller;

import boxgym.helper.AlertHelper;
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
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
    void handleClients(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Clients.fxml");
    }

    @FXML
    void handleSuppliers(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Suppliers.fxml");
    }

    @FXML
    void handleEmployees(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Employees.fxml");
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
    void handleStock(ActionEvent event) throws IOException {
        changeContentArea("/boxgym/view/Stock.fxml");
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

}
