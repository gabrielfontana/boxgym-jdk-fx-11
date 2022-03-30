package boxgym.controller;

import currencyfield.CurrencyField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;

public class StockEntryController implements Initializable {
    
    @FXML
    private AnchorPane stockEntryArea;

    @FXML
    private ComboBox<?> supplierComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private LimitedTextField fiscalNote;

    @FXML
    private Button addStockEntryButton;

    @FXML
    private AnchorPane productsEntryArea;

    @FXML
    private ComboBox<?> productComboBox;

    @FXML
    private LimitedTextField amountTextField;

    @FXML
    private CurrencyField costPriceTextField;

    @FXML
    private Button addProductEntryButton;

    @FXML
    private TableView<?> productsEntryTableView;

    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePicker.setEditable(false);
    }

    @FXML
    void addEntry(ActionEvent event) {
        stockEntryArea.setDisable(true);
        productsEntryArea.setDisable(false);
    }

    @FXML
    void clear(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {

    }

}
