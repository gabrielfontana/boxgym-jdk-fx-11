package boxgym.controller;

import boxgym.dao.ProductDao;
import boxgym.dao.StockEntryDao;
import boxgym.dao.SupplierDao;
import boxgym.helper.ButtonHelper;
import boxgym.model.StockEntry;
import boxgym.model.StockEntryProduct;
import currencyfield.CurrencyField;
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.layout.AnchorPane;
import limitedtextfield.LimitedTextField;

public class StockEntryController implements Initializable {

    SupplierDao supplierDao = new SupplierDao();
    LinkedHashMap<Integer, String> supplierMap = supplierDao.getSupplierForHashMap();

    ProductDao produtDao = new ProductDao();
    LinkedHashMap<Integer, String> productMap = produtDao.getProductForHashMap();

    @FXML
    private AnchorPane stockEntryArea;

    @FXML
    private ComboBox<String> supplierComboBox;

    @FXML
    private DatePicker invoiceIssueDateDatePicker;

    @FXML
    private LimitedTextField invoiceNumberTextField;

    @FXML
    private Button addStockEntryButton;

    @FXML
    private AnchorPane productsEntryArea;

    @FXML
    private ComboBox<String> productComboBox;

    @FXML
    private LimitedTextField amountTextField;

    @FXML
    private CurrencyField costPriceTextField;

    @FXML
    private Button addProductEntryButton;

    @FXML
    private TableView<StockEntryProduct> productsEntryTableView;

    @FXML
    private TableColumn<StockEntryProduct, Integer> productTableColumn;

    @FXML
    private TableColumn<StockEntryProduct, Integer> amountTableColumn;

    @FXML
    private TableColumn<StockEntryProduct, BigDecimal> costPriceTableColumn;

    @FXML
    private TableColumn<StockEntryProduct, BigDecimal> totalTableColumn;

    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadSupplierNameComboBox();
        loadProductNameComboBox();
        productNameComboBoxListener();
        ButtonHelper.buttonCursor(addStockEntryButton, addProductEntryButton, saveButton, clearButton);
        tableViewListeners();
        initSupplierTableView();
    }

    private void loadSupplierNameComboBox() {
        ObservableList<String> obsList = FXCollections.observableArrayList();
        for (String s : supplierMap.values()) {
            obsList.add(s);
        }
        supplierComboBox.setPromptText("Selecione");
        supplierComboBox.setItems(obsList);
    }

    private int getKeyFromSupplierComboBox() {
        int key = 0;
        for (Map.Entry<Integer, String> entry : supplierMap.entrySet()) {
            if (supplierComboBox.getSelectionModel().getSelectedItem().equals(entry.getValue())) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    private void loadProductNameComboBox() {
        ObservableList<String> obsList = FXCollections.observableArrayList();
        for (String s : productMap.values()) {
            obsList.add(s);
        }
        productComboBox.setPromptText("Selecione");
        productComboBox.setItems(obsList);
    }

    private int getKeyFromProductComboBox() {
        int key = 0;
        for (Map.Entry<Integer, String> entry : productMap.entrySet()) {
            if (productComboBox.getSelectionModel().getSelectedItem().equals(entry.getValue())) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    private void productNameComboBoxListener() {
        productComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            ProductDao dao = new ProductDao();
            costPriceTextField.setPrice(dao.getProductCostPrice(getKeyFromProductComboBox()).doubleValue());
        }); 
    }

    private void initSupplierTableView() {
        productTableColumn.setCellValueFactory(new PropertyValueFactory("fkProduct"));
        amountTableColumn.setCellValueFactory(new PropertyValueFactory("amount"));
        costPriceTableColumn.setCellValueFactory(new PropertyValueFactory("costPrice"));
        totalTableColumn.setCellValueFactory(new PropertyValueFactory("total"));
    }

    @FXML
    void addEntry(ActionEvent event) {
        stockEntryArea.setDisable(true);
        productsEntryArea.setDisable(false);
        StockEntry stockEntry = new StockEntry(invoiceIssueDateDatePicker.getValue(), invoiceNumberTextField.getText(), getKeyFromSupplierComboBox());
        StockEntryDao stockEntryDao = new StockEntryDao();
        if(stockEntryDao.create(stockEntry)){
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

    @FXML
    void clear(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
        
    }

    private void tableViewListeners() {
        productsEntryTableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            final TableHeaderRow header = (TableHeaderRow) productsEntryTableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((o, oldVal, newVal) -> header.setReordering(false));
        });
    }

}
